package chFB;

import Tools.*;
import javafx.util.Pair;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class chFB implements Serializable {
    public int DATA_SIZE;// 数据库大小
    public int n;// upper bound on the total number of values
    public int MAX_VOLUME_LENGTH;// 数据库的最大volume
    public int l;// upper bound on the maximum volume

    private final int s; // 满二叉树的数量
    private final int h; // 每颗二叉树的高度
    private final int c = 2; // 参数c
    public int Get_c(){return c;}
    public TwoChoiceHash twoChoiceHash;
    public ArrayList<KV> Stash;

    private final byte[] K_u;
    public byte[] Get_K_u() { return K_u; }

    public HashMap<String,byte[]> EMM_u = new HashMap<>();// 不能将 byte[] 作为 key(https://blog.csdn.net/heihaozi/article/details/130398856)
    public HashMap<String, Pair<Integer, Integer>> MM_st = new HashMap<>();


    public chFB(int DATA_SIZE, int MAX_VOLUME_LENGTH,String filename) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // 配置参数，(n,l) 均先简单设置为原数据库的2倍
        this.DATA_SIZE = DATA_SIZE;
        this.n = DATA_SIZE / 2;
        this.MAX_VOLUME_LENGTH = MAX_VOLUME_LENGTH;
        this.l = MAX_VOLUME_LENGTH * 2;
        // s = n/(clog(n))
        s = (int) Math.ceil((double) n / c / (Math.log(n) / Math.log(2)));
        // h = log(clog(n))
        h = (int) Math.ceil(Math.log(c * Math.log(n)/Math.log(2)) / Math.log(2));
        K_u = new byte[16];
        new Random().nextBytes(K_u);

        KV[] kv_list = SerialData.Serial_Raw_In(filename);
        twoChoiceHash = new TwoChoiceHash(kv_list,s,h);
        Stash = twoChoiceHash.Get_Stash();
    }
    public chFB(String filename) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int[] params = tool.Get_Total_Max_Num(filename);
        this.DATA_SIZE = params[0];
        this.MAX_VOLUME_LENGTH = params[1];
        // 配置参数，(n,l) 均先简单设置为原数据库的2倍
        this.n = DATA_SIZE / 2;
        this.l = MAX_VOLUME_LENGTH * 2;
        // s = n/(clog(n))
        s = (int) Math.ceil((double) n / c / (Math.log(n) / Math.log(2)));
        // h = log(clog(n))
        h = (int) Math.ceil(Math.log(c * Math.log(n)/Math.log(2)) / Math.log(2));
        K_u = new byte[16];
        new Random().nextBytes(K_u);

        KV[] kv_list = SerialData.Serial_Raw_In(filename);
        twoChoiceHash = new TwoChoiceHash(kv_list,s,h);
        Stash = twoChoiceHash.Get_Stash();
    }

    public void Update(String key,ArrayList<String> v,String op) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!MM_st.containsKey(key)){
            MM_st.put(key,new Pair<>(0,0));
        }
        String x = Arrays.toString(K_u) + key + MM_st.get(key).getKey();
        byte[] hash_x = Hash.Get_SHA_256(x.getBytes(StandardCharsets.UTF_8));
        String y = Arrays.toString(hash_x) + MM_st.get(key).getValue();
        byte[] hash_y = Hash.Get_SHA_256(y.getBytes(StandardCharsets.UTF_8));
        StringBuilder z = new StringBuilder(op);
        for (String string : v) z.append(",").append(string);
        for (int i = v.size();i<l;i++) z.append(",").append("null");
        byte[] encrypt_z = AESUtil.encrypt(twoChoiceHash.Get_K_enc(), z.toString().getBytes(StandardCharsets.UTF_8));
        EMM_u.put(Arrays.toString(hash_y), encrypt_z);
        MM_st.put(key,new Pair<>(MM_st.get(key).getKey(),MM_st.get(key).getValue() + 1));
    }

    public ArrayList<String> Query(String key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // search bins
        byte[] token =  Hash.Get_SHA_256((Arrays.toString(twoChoiceHash.Get_K()) + key).getBytes(StandardCharsets.UTF_8));
        ArrayList<byte[]> result_server = new ArrayList<>();// 服务器搜索结果
        ArrayList<String> result_client = new ArrayList<>();// 客户端最终结果
        ArrayList<KV> unwanted = new ArrayList<>();// 写回的结果
        Set<Integer> bin = new java.util.HashSet<>();
        for(int i =0;i<l;i++){
            String token_0 = Arrays.toString(token) + i + 0;
            String token_1 = Arrays.toString(token) + i + 1;
            byte[] hash_0 = Hash.Get_SHA_256(token_0.getBytes(StandardCharsets.UTF_8));
            byte[] hash_1 = Hash.Get_SHA_256(token_1.getBytes(StandardCharsets.UTF_8));
            bin.add(TwoChoiceHash.Map2Range(hash_0, (int) (s * Math.pow(2,h))));
            bin.add(TwoChoiceHash.Map2Range(hash_1, (int) (s * Math.pow(2,h))));
        }
        for (int i:bin) {
            twoChoiceHash.Query((int) (i /  Math.pow(2,h)), (int) (i %  Math.pow(2,h)),result_server);
        }

        for (byte[] et:result_server) {
            String string = new String(AESUtil.decrypt(twoChoiceHash.Get_K_enc(), et));
            String[] kv = string.split(",");
            if (kv[0].equals(key)){
                result_client.add(kv[1]);
            }else if (!kv[0].equals("dummy")){
                unwanted.add(new KV(kv[0],kv[1]));
            }
        }
        // Query stash and delete the key-value pair
        Iterator<KV> iterator = Stash.iterator();
        while (iterator.hasNext()) {
            KV kv = iterator.next();
            if (kv.key.equals(key)) {
                result_client.add(kv.value);
                iterator.remove();
            }
        }

        if (!MM_st.containsKey(key) || (MM_st.get(key).getValue() == 0)){
            twoChoiceHash.WriteBack(key,result_client,unwanted,bin);
            Stash = twoChoiceHash.Get_Stash();
            return result_client;
        }

        String x = Arrays.toString(K_u) + key + MM_st.get(key).getKey();
        byte[] hash_x = Hash.Get_SHA_256(x.getBytes(StandardCharsets.UTF_8));
        for (int i = 0;i<MM_st.get(key).getValue();i++){
            String y = Arrays.toString(hash_x) + i;
            byte[] hash_y = Hash.Get_SHA_256(y.getBytes(StandardCharsets.UTF_8));
            String string = new String(AESUtil.decrypt(twoChoiceHash.Get_K_enc(), EMM_u.get(Arrays.toString(hash_y))));
            String[] kv = string.split(",");
            String op = kv[0];
            switch (op) {
                case "app":
                    for (int j = 1; j < kv.length; j++) {
                        if (kv[j].equals("null"))
                            break;
                        result_client.add(kv[j]);
                    }
                    break;
                case "edit":
                    result_client.clear();
                    for (int j = 1; j < kv.length; j++) {
                        if (kv[j].equals("null"))
                            break;
                        result_client.add(kv[j]);
                    }
                    break;
                case "rm":
                    result_client.clear();
                    break;
                case "del":
                    for (int j = 1; j < kv.length; j++) {
                        if (kv[j].equals("null"))
                            break;
                        result_client.remove(kv[j]);
                    }
                    break;
                default:
                    System.out.println("Invalid Operation");
                    break;
            }

        }
        twoChoiceHash.WriteBack(key,result_client,unwanted,bin);
        Stash = twoChoiceHash.Get_Stash();
        MM_st.put(key,new Pair<>(MM_st.get(key).getKey() + 1,0));
        return result_client;
    }

    public void Query_Update()throws Exception{
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Do you want to query(q) or update(u) or exit(e)");
            char OPT = scanner.nextLine().charAt(0);
            switch (OPT) {
                case 'q': {
                    System.out.println("Please enter the key you want to Query('q' for quit):");
                    while (true){
                        System.out.print("Key:");
                        String key = scanner.nextLine();
                        if (key.equals("q"))
                            break;
                        ArrayList<String> result = this.Query(key);
                        System.out.println("Query Result:" + result.size() + " items");
                        // print the result
                        for (String s:result) {
                            System.out.println(s);
                        }
                    }
                    break;
                }
                case 'u':{
                    System.out.println("Please enter the key-values and operation you want to Query('q' for quit in everywhere):");
                    while (true){
                        System.out.print("Key:");
                        String key = scanner.nextLine();
                        if (key.equals("q"))
                            break;

                        System.out.print("Value(Separated by \",\"):");
                        String value = scanner.nextLine();
                        if (value.equals("q"))
                            break;
                        ArrayList<String> v = new ArrayList<>();
                        String[] values = value.split(",");
                        Collections.addAll(v, values);

                        System.out.print("Operation(app/edit/rm/del):");
                        String op = scanner.nextLine();
                        if (op.equals("q"))
                            break;

                        this.Update(key,v,op);
                    }
                    break;
                }
                case 'e':
                    return;
                default:
                    System.out.println("Invalid Input");
            }
        }

    }


}
