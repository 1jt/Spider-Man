package VHDSSE;

import Tools.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import dprfMM.dprfMM;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class VHDSSE implements Serializable {

    public int DATA_SIZE;
    public int MAX_VOLUME_LENGTH;
    String binary_N;
    public int min;
    public double beta;

    private static final byte[] K_stash = "6975922666f6eb02".getBytes(StandardCharsets.UTF_8);// AES 加密的密钥
    public static byte[] Get_K_stash() { return K_stash; }
    private static final byte[] K_buf = "5975922666f6eb02".getBytes(StandardCharsets.UTF_8);// AES 加密的密钥
    public static byte[] Get_K_buf() { return K_buf; }

    ArrayList<dprfMM> databases = new ArrayList<>();

    public byte[][] EMM_stash;//store stash ciphertext
    public byte[][] EMM_buf;//store buf ciphertext


    public VHDSSE(int numPairs, int maxVolume, String filename) throws Exception {
        this.DATA_SIZE = numPairs;
        this.MAX_VOLUME_LENGTH = maxVolume;
        // 8
        binary_N = Integer.toBinaryString(DATA_SIZE);
        // 9
        min = getMin();
        // 23
        beta = (double) MAX_VOLUME_LENGTH / DATA_SIZE;
        Setup(filename);
    }
    // 直接从文件名中获取数据大小和最大volume(非通用)
    public VHDSSE(String filename) throws Exception {
        int[] params = tool.Get_Total_Max_Num(filename);
        this.DATA_SIZE = params[0];
        this.MAX_VOLUME_LENGTH = params[1];
        // 8
        binary_N = Integer.toBinaryString(DATA_SIZE);
        // 9
        min = getMin();
        // 23
        beta = (double) MAX_VOLUME_LENGTH / DATA_SIZE;
        Setup(filename);
    }
    public int getMin() {
        double log_N = Math.log(DATA_SIZE) / Math.log(2);
        for (int i = 0;;)
            if (Math.pow(2, i++) > log_N)
                return i-2;
    }


    public void Setup(String filename) throws Exception {
        // 1
        ArrayList<KV> stash = new ArrayList<>();//store evicted elements
        ArrayList<KV> buf = new ArrayList<>();//store remain elements
        // 4-7
        KV[] kv_list = Arrays.copyOfRange(SerialData.Serial_Raw_In(filename), 0, DATA_SIZE);
        for (KV kv : kv_list) {
            kv.value = kv.value + ",add";
        }
        System.out.println("---------------------VHDSSE scheme on " + filename + "---------------------");
        // 8
        int idx = 0;
        // 10-16
        for (int i = (int) Math.floor(Math.log(DATA_SIZE) / Math.log(2.0)); i > min - 1 ; i--) {
            if (binary_N.charAt(binary_N.length() - 1 - i) == '0') {
                System.out.println("there is no database");
                continue;
            }
//            System.out.println("---------------------database" + (binary_N.length() - 1 - i) + "---------------------");
            databases.add(new dprfMM((int) (beta*Math.pow(2,i)), Arrays.copyOfRange(kv_list, idx, idx + (int) Math.pow(2, i)-1)));
            stash.addAll(databases.get(databases.size()-1).cuckoo.Get_Stash());
            databases.get(databases.size()-1).cuckoo.Get_Stash().clear();// clear stash
            idx += (int) Math.pow(2, i);
        }
        // 17-19
        buf.addAll(Arrays.asList(kv_list).subList(idx, DATA_SIZE));
        EMM_buf = EncryptEMM(K_buf, buf);
        buf.clear();
        // 20-22
        if (stash.size() < f(DATA_SIZE)) {
            Random random = new Random();
            for (int i = stash.size(); i < f(DATA_SIZE); i++)
                stash.add(new KV("dummy", "dummy" + random.nextInt(1000000)));
        }
        EMM_stash = EncryptEMM(K_stash, stash);
        stash.clear();
    }

    // 查询函数
    public ArrayList<String> VHDSSE_Query(String search_key) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        System.out.println("\nClient is searching token ...\nkeywords : >>>   " + (search_key) + "   <<<");
        for (dprfMM database : databases) {
            result.addAll(database.DprfQuery(search_key));
        }
        Search_stash_buf(search_key, result, EMM_stash, EMM_buf);
        // 判断结果并进行错误校验
        ArrayList<String> final_result = Judge_result(result);
        for (String s : final_result) {
            System.out.println(s + " ");
        }
        return final_result;
    }
    // 客户端查询返回结果
    public ArrayList<byte[]> VHDSSE_Query_Server(String search_key){
        // 用户生成token
        byte[] tk_key = GenSearchToken(search_key);
        // 服务器查询
        ArrayList<byte[]> ServerResult = new ArrayList<>();
        for (dprfMM database : databases) {
            ServerResult.addAll(dprfMM.Query_Cuckoo(tk_key, database.cuckoo,database.MAX_VOLUME_LENGTH));
        }

        return ServerResult;
    }
    // 加密 stash 与 buf
    public byte[][] EncryptEMM(byte[] K, ArrayList<KV> Key_Value) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[][] EMM = new byte[Key_Value.size()][32];
        for (int i = 0; i < Key_Value.size(); i++) {
            EMM[i] = AESUtil.encrypt(K, (Key_Value.get(i).key + "," + Key_Value.get(i).value).getBytes(StandardCharsets.UTF_8));
        }
        return EMM;
    }
    // f(n) = O(log(n))
    public int f(int n){
        // f(n) = O(log(n))
        return 2 * (int) Math.ceil(Math.log(n) / Math.log(2.0));
    }


    public void Search_stash_buf(String search_key, ArrayList<String> ClientResult,byte[][] stash,byte[][] buf) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for (byte[] res : stash) {
            String kv = new  String(AESUtil.decrypt(K_stash, res));
            String[] kv_split = kv.split(",");
            if (kv_split[0].equals(search_key)) {
                ClientResult.add(kv_split[1] + "," + kv_split[2]);
            }
        }
        for (byte[] res : buf) {
            String kv = new  String(AESUtil.decrypt(K_buf, res));
            String[] kv_split = kv.split(",");
            if (kv_split[0].equals(search_key)) {
                ClientResult.add(kv_split[1] + "," + kv_split[2]);
            }
        }
    }

    // 判断结果并进行错误校验
    public ArrayList<String> Judge_result(ArrayList<String> result) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        ArrayList<String> final_result = new ArrayList<>();
        HashMap<String, Integer> add = new HashMap<>();
        HashMap<String, Integer> del = new HashMap<>();
        for (String s : result) {
            String[] kv = s.split(",");
            if (kv[1].equals("add")) {
                if (del.containsKey(kv[0]) && del.get(kv[0])>0)
                    del.put(kv[0], del.get(kv[0])-1);
                else if (add.containsKey(kv[0]))
                    add.put(kv[0], add.get(kv[0])+1);
                else
                    add.put(kv[0], 1);
            } else if (kv[1].equals("del")) {
                if (add.containsKey(kv[0]) && add.get(kv[0])>0)
                    add.put(kv[0], add.get(kv[0])-1);
                else if (del.containsKey(kv[0]))
                    del.put(kv[0], del.get(kv[0])+1);
                else
                    del.put(kv[0], 1);
            }
        }
        for (String s: add.keySet()) {
            if (add.get(s) > 1)
                System.out.println("Repeatedly adding " + s + " " + add.get(s) + " times");
            else if (add.get(s) == 0)
                continue;
            else if (add.get(s) < 0) {
                System.out.println("No possible theoretically (add) ");
                continue;
            }
            final_result.add(s);
        }
        // test delete
        for (String d: del.keySet()) {
            if (del.get(d)>0){
                System.out.println("Too much del operation on " + d + " " + del.get(d) + " times");
            } else if (del.get(d)<0) {
                System.out.println("No possible theoretically (del)");
            }
        }
        return final_result;
    }
    public byte[] GenSearchToken(String search_key){
        return Hash.Get_SHA_256((search_key + Cuckoo_Hash.Get_K_d()).getBytes(StandardCharsets.UTF_8));
    }
}
