package chFB;

import Tools.AESUtil;
import Tools.Hash;
import Tools.KV;
import Tools.SerialData;
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

    public HashMap<byte[],byte[]> EMM_u = new HashMap<>();
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
        EMM_u.put(hash_y,encrypt_z);
        MM_st.put(key,new Pair<>(MM_st.get(key).getKey(),MM_st.get(key).getValue() + 1));
    }

    public ArrayList<String> Query(String key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        ArrayList<byte[]> result_server = new ArrayList<>();// 服务器搜索结果
        ArrayList<String> result_client = new ArrayList<>();// 客户端最终结果
        Set<Integer> bin = new java.util.HashSet<>();
        byte[] token =  Hash.Get_SHA_256((Arrays.toString(twoChoiceHash.Get_K()) + key).getBytes(StandardCharsets.UTF_8));
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

        Set<String> duplicate_set = new java.util.HashSet<>();
        for (byte[] et:result_server) {
            String string = new String(AESUtil.decrypt(twoChoiceHash.Get_K_enc(), et));
            String[] kv = string.split(",");
            if (kv[0].equals(key) && !duplicate_set.contains(kv[1])){
                result_client.add(kv[1]);
                duplicate_set.add(kv[1]);
            }
        }
        // Query stash
        for (KV kv:Stash)
            if (kv.key.equals(key))
                result_client.add(kv.value);

        return result_client;
    }

    public void Query_Update()throws Exception{
        System.out.println("Please enter the key value pair and operation you want to Query:");
        Scanner scanner = new Scanner(System.in);
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
    }


}
