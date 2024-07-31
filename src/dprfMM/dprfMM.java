package dprfMM;
import Tools.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


public class dprfMM {

    public Cuckoo_Hash cuckoo;

    public int DATA_SIZE;
    public int MAX_VOLUME_LENGTH;
    public int CUCKOO_LEVEL;
    public double alpha = 0.3;// 注意和Cuckoo_Hash中的alpha保持一致
    public int STORAGE_CUCKOO;

    public dprfMM(int numPairs, int maxVolume, String filename) throws Exception {
        this.DATA_SIZE = numPairs;
        this.MAX_VOLUME_LENGTH = maxVolume;
        this.CUCKOO_LEVEL = (int) Math.ceil(Math.log(MAX_VOLUME_LENGTH) / Math.log(2.0));
        Setup(filename);
    }
    // 直接从文件名中获取数据大小和最大volume(非通用)
    public dprfMM(String filename) throws Exception {
        int[] params = tool.Get_Total_Max_Num(filename);
        this.DATA_SIZE = params[0];
        this.MAX_VOLUME_LENGTH = params[1];
        this.CUCKOO_LEVEL = (int) Math.ceil(Math.log(MAX_VOLUME_LENGTH) / Math.log(2.0));
        Setup(filename);
    }

    public void Setup(String filename) throws Exception {
        //storage size for dprfMM
        STORAGE_CUCKOO = (int) Math.floor((DATA_SIZE * (1 + alpha)));

        KV[] kv_list = SerialData.Serial_Raw_In(filename);
        System.out.println("---------------------cuckoo hash scheme(dprfMM CCS'19) on " + filename + "---------------------");
        //setup phase
        cuckoo = new Cuckoo_Hash();
        assert kv_list != null;
        cuckoo.Setup(kv_list, CUCKOO_LEVEL);
    }

    public ArrayList<String> DprfQuery(String search_key) throws Exception {
        System.out.println("\nClient is generating token ...\nkeywords : >>>   " + (search_key) + "   <<<");
        byte[] tk_key = GenSearchToken(search_key);

        System.out.println("\nServer is searching and then Client decrypts ... ");
        // 服务器返回结果
        ArrayList<byte[]> ServerResult = Query_Cuckoo(tk_key);

        // 解密结果
        ArrayList<String> ClientResult = DecryptResult(ServerResult,search_key);

        // 搜索stash
        SearchStash(search_key,ClientResult);

        return ClientResult;
    }

    // 提供静态与非静态的token生成方法
    public byte[] GenSearchToken(String search_key){
        return Hash.Get_SHA_256((search_key + cuckoo.Get_K_d()).getBytes(StandardCharsets.UTF_8));
    }
    public static byte[] GenSearchToken(String search_key,long K_d){
        return Hash.Get_SHA_256((search_key + K_d).getBytes(StandardCharsets.UTF_8));
    }

    // 提供静态和非静态的 Server_Query算法
    public ArrayList<byte[]> Query_Cuckoo(byte[] token){
        ArrayList<byte[]> ServerResult = new ArrayList<>();
        //GGM.clear();
        for (int i = 0;i < MAX_VOLUME_LENGTH;i++ ) {
            byte[] father_Node = GGM.Doub_GGM_Path(token, CUCKOO_LEVEL, tool.DecimalConversion(i, 2, CUCKOO_LEVEL));
            int t0 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9),STORAGE_CUCKOO,0);
            int t1 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),STORAGE_CUCKOO,1);
            ServerResult.add(cuckoo.Get_EMM()[t0]);
            ServerResult.add(cuckoo.Get_EMM()[t1]);
        }
        return ServerResult;
    }
    public static ArrayList<byte[]> Query_Cuckoo(byte[] token,Cuckoo_Hash cuckoo,int MAX_VOLUME_LENGTH){
        ArrayList<byte[]> ServerResult = new ArrayList<>();
        //GGM.clear();
        for (int i = 0;i < MAX_VOLUME_LENGTH;i++ ) {
            byte[] father_Node = GGM.Doub_GGM_Path(token, cuckoo.Get_Level(), tool.DecimalConversion(i, 2, cuckoo.Get_Level()));
            int t0 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9),cuckoo.Get_Table_Size(),0);
            int t1 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),cuckoo.Get_Table_Size(),1);
            ServerResult.add(cuckoo.Get_EMM()[t0]);
            ServerResult.add(cuckoo.Get_EMM()[t1]);
        }
        return ServerResult;
    }

    // 客户端解密算法
    public static ArrayList<String> DecryptResult(ArrayList<byte[]> ServerResult, String search_key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        ArrayList<String> ClientResult = new ArrayList<>();
        for (int i = 0; i < ServerResult.size(); ) {
            String s0 = new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(),ServerResult.get(i)));
            String[] s0_list = s0.split(",");
            if(s0_list[0].equals(search_key)){
                ClientResult.add(s0_list[1]);
            }
            String s1 = new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(),ServerResult.get(i + 1)));
            String[] s1_list = s1.split(",");
            if(s1_list[0].equals(search_key)){
                ClientResult.add(s1_list[1]);
            }
            i = i + 2;
        }

        return ClientResult;
    }

    public void SearchStash(String search_key, ArrayList<String> ClientResult){
        ArrayList<KV> stash = cuckoo.Get_Stash();//Get stash
        for (KV res : stash) {
            if (res.key.equals(search_key)) {
                ClientResult.add(res.value);
                System.out.println("Stash Result:" + res);
            }
        }
    }
}