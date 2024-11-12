package VHDSSE;

import Tools.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import dprfMM.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class VHDSSE implements Serializable {

    public int DATA_SIZE; // a.k.a. : N \tau
    // TODO 后期看能不能把这个变为非成员变量，反正存着没用
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

    public String filename;


    public VHDSSE(int numPairs, int maxVolume, String filename) throws Exception {
        this.DATA_SIZE = numPairs;
        this.MAX_VOLUME_LENGTH = maxVolume;
        this.filename = filename.split("/")[filename.split("/").length-1];
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
        this.filename = filename.split("/")[filename.split("/").length-1];
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
//        KV[] kv_list = Arrays.copyOfRange(SerialData.Serial_Raw_In(filename), 0, DATA_SIZE);
        KV[] kv_list = SerialData.Serial_Raw_In(filename);
        assert kv_list != null;
        //System.out.println("---------------------VHDSSE scheme on " + filename + "---------------------");
        // 8
        int idx = 0;
        // 10-16
        for (int i = (int) Math.floor(Math.log(DATA_SIZE) / Math.log(2.0)); i > min - 1 ; i--) {
            if (binary_N.charAt(binary_N.length() - 1 - i) == '0') {
                //System.out.println("there is no database");
                continue;
            }
//            System.out.println("---------------------database" + (binary_N.length() - 1 - i) + "---------------------");
            databases.add(new dprfMM((int) Math.ceil(beta*Math.pow(2,i)), Arrays.copyOfRange(kv_list, idx, idx + (int) Math.pow(2, i))));
            stash.addAll(databases.get(databases.size()-1).cuckoo.Get_Stash());
            databases.get(databases.size()-1).cuckoo.Get_Stash().clear();// clear stash
            idx += (int) Math.pow(2, i);
        }
        // 17-19
        buf.addAll(Arrays.asList(kv_list).subList(idx, DATA_SIZE));
        byte[][] EMM_tmp_buf = EncryptEMM(K_buf, buf);
        // 将 buf的大小强制设置为 2^min,非占用位置设置为 0
        EMM_buf = new byte[(int) Math.pow(2,min)][32];
        System.arraycopy(EMM_tmp_buf,0,EMM_buf,0,EMM_tmp_buf.length);
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
    // 加密 stash 与 buf
    public byte[][] EncryptEMM(byte[] K, ArrayList<KV> Key_Value) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[][] EMM = new byte[Key_Value.size()][32];
        for (int i = 0; i < Key_Value.size(); i++) {
            EMM[i] = AESUtil.encrypt(K, (Key_Value.get(i).key + "," + Key_Value.get(i).value + "," + Key_Value.get(i).op).getBytes(StandardCharsets.UTF_8));
        }
        return EMM;
    }
    // f(n) = O(log(n))
    public int f(int n){
        // f(n) = O(log(n))
        return 2 * (int) Math.ceil(Math.log(n) / Math.log(2.0));
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
        return Judge_result(result);
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
            // 如果是空的就跳过
            if (Arrays.equals(res, new byte[32]))
                continue;
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
            if (kv.length==1 || kv[1].equals("add")) {
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

    // 服务器端查询返回结果
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

    public byte[] GenSearchToken(String search_key){
        return Hash.Get_SHA_256((search_key + Cuckoo_Hash.Get_K_d()).getBytes(StandardCharsets.UTF_8));
    }

    // 更新函数
    // 原方案有无，原方案认为 |buf| == 2 * log(N), 2^(min) <= log(N) < 2^(min+1)
    // 但是不可能是2倍，不然第 j 个数据库容不下 2^(j-1) + ... + 2^(min) + 2^(min+1)个元素
    // 鉴于此，本复现方案将 buf 大小设置为 |buf| = 2 ^ (min)
    public void Update(KV kv) throws Exception {
        // 加密更新数据
        byte[] kv_encrypt = AESUtil.encrypt(K_buf, (kv.key + "," + kv.value + "," + kv.op).getBytes(StandardCharsets.UTF_8));
        // 更新方案参数
        DATA_SIZE++;
        binary_N = Integer.toBinaryString(DATA_SIZE);
        // 4
        // 判断buf是否已满，如果满了就更新数据库
        for (int i = 0; i < EMM_buf.length - 1; i++) {
            if (Arrays.equals(EMM_buf[i], new byte[32])) {
                EMM_buf[i] = kv_encrypt;
                return;
            }
        }
        EMM_buf[EMM_buf.length - 1] = kv_encrypt;// 如果放入最后一个元素意味着buf已满
        // 寻找对应的数据库
        ArrayList<dprfMM> EDBs = Find_EDB(DATA_SIZE - 1);
        // 提取所有的EDB中的数据
        ArrayList<KV> EDB_data = new ArrayList<>();
        for (dprfMM edb : EDBs) {
            byte[][] tmp_emm = edb.cuckoo.Get_EMM();
            for (byte[] ciphertext : tmp_emm) {
                String tmp = new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(), ciphertext));
                String[] kv_split = tmp.split(",");
                if (!kv_split[0].equals("dummy"))
                    EDB_data.add(new KV(kv_split[0], kv_split[1]));
            }
        }
        // 提取stash 和 buf中的数据
        Get_stash_buf(EDB_data);
        // neutralize add and del(中和添加和删除)
        ArrayList<KV> db = Neutralize_add_del(EDB_data);
        // 重新生成数据库
        dprfMM new_edb = Setup(db.toArray(new KV[0]));

        databases.add(new_edb);
    }
    // 提取需要返回的数据库
    public ArrayList<dprfMM> Find_EDB(int DATA_SIZE) {
        ArrayList<dprfMM> result = new ArrayList<>();
        String binary_ds = Integer.toBinaryString(DATA_SIZE);

        for (int i = min; i < binary_ds.length(); i++) {
            if (binary_ds.charAt(binary_ds.length() - 1 - i) == '1') {
                // 从databases中取出对应的数据库
                result.add(databases.get(databases.size()-1));
                // 删除databases中的数据库
                databases.remove(databases.size()-1);
            } else {
                break;
            }
        }
        return result;
    }
    // 提取stash 和 buf中的数据
    public void Get_stash_buf(ArrayList<KV> EDB_db) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for (byte[] res : EMM_stash) {
            String kv = new  String(AESUtil.decrypt(K_stash, res));
            String[] kv_split = kv.split(",");
            if (!kv_split[0].equals("dummy")) {
                EDB_db.add(new KV(kv_split[0], kv_split[1], kv_split[2]));
            }
        }
        for (byte[] res : EMM_buf) {
            if (Arrays.equals(res, new byte[32]))
                break;
            String kv = new  String(AESUtil.decrypt(K_buf, res));
            String[] kv_split = kv.split(",");
            if (!kv_split[0].equals("dummy")) {
                EDB_db.add(new KV(kv_split[0], kv_split[1], kv_split[2]));
            }
        }
        // 清空stash和buf
        Arrays.fill(EMM_stash, new byte[32]);
        Arrays.fill(EMM_buf, new byte[32]);
    }
    // 中和添加和删除
    public ArrayList<KV> Neutralize_add_del(ArrayList<KV> EDB_data) {
        ArrayList<KV> db = new ArrayList<>();
        HashMap<KV, Integer> add = new HashMap<>();
        HashMap<KV, Integer> del = new HashMap<>();
        for (KV kv : EDB_data) {
            if (kv.op.equals("add")) {
                if (del.containsKey(kv) && del.get(kv)>0)
                    del.put(kv, del.get(kv)-1);
                else if (add.containsKey(kv))
                    add.put(kv, add.get(kv)+1);
                else
                    add.put(kv, 1);
            } else if (kv.op.equals("del")) {
                if (add.containsKey(kv) && add.get(kv)>0)
                    add.put(kv, add.get(kv)-1);
                else if (del.containsKey(kv))
                    del.put(kv, del.get(kv)+1);
                else
                    del.put(kv, 1);
            }
        }
        for (KV a: add.keySet()) {
            if (add.get(a) > 1)
                System.out.println("Repeatedly adding " + a + " " + add.get(a) + " times");
            else if (add.get(a) == 0)
                continue;
            else if (add.get(a) < 0) {
                System.out.println("No possible theoretically (add) ");
                continue;
            }
            db.add(a);
        }
        // test delete
        for (KV d: del.keySet()) {
            if (del.get(d)>0){
                System.out.println("Too much del operation on " + d.key + " " + del.get(d) + " times");
            } else if (del.get(d)<0) {
                System.out.println("No possible theoretically (del)");
            }
        }
        // 填充 db
        Random random = new Random();
        for (int i = db.size(); i < EDB_data.size(); i++) {
            db.add(new KV("Dum", "Dummy" + random.nextInt(1000000)));// 注意Dummy是大写，可以防止后续呗忽略，同时起到占位的作用
        }
        return db;
    }
    // 重新生成数据库
    public dprfMM Setup(KV[] kv_list) throws Exception {
        dprfMM new_edb = new dprfMM((int) Math.ceil(beta* kv_list.length), kv_list);
        //store evicted elements
        ArrayList<KV> stash = new_edb.cuckoo.Get_Stash();
        // padding stash
        if (stash.size() < f(DATA_SIZE)) {
            Random random = new Random();
            for (int i = stash.size(); i < f(DATA_SIZE); i++)
                stash.add(new KV("dummy", "dummy" + random.nextInt(1000000)));
        }
        EMM_stash = EncryptEMM(K_stash, stash);
        new_edb.cuckoo.Get_Stash().clear();// clear stash

        return new_edb;
    }


    // TODO 可以修改为多次查询和更新的版本
    public void Update() throws Exception {
        System.out.println("Please enter the key value pair and operation you want to update:");
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Key:");
            String key = scanner.nextLine();
            if (key.equals("q"))
                break;
            System.out.print("Value:");
            String value = scanner.nextLine();
            if (value.equals("q"))
                break;
            System.out.print("Op:");
            String op = scanner.nextLine();
            if (op.equals("q"))
                break;
            this.Update(new KV(key,value,op));
            System.out.println("Update successfully!");
        }
        System.out.println("Update finished!\nDo you want to save the results?\nYes or No");
        String save = scanner.nextLine();
        if (save.equals("Yes")) {
            SerialData.Serial_DB_Out(this,filename);
        }
    }
}
