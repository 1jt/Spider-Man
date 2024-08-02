package dpMM;

import Tools.*;
import dprfMM.GGM;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class dpMM implements Serializable {
    public Cuckoo_Hash Data; // 用于存储数据
    public Cuckoo_Hash CT; // 用于存储 l(key)

    public int DATA_SIZE;

    public int MAX_VOLUME_LENGTH;

    public int CUCKOO_LEVEL;

    public double alpha = 0.3;// 注意和Cuckoo_Hash中的alpha保持一致

    public int STORAGE_Data;
    public int STORAGE_CT;

    public int l_lambda = 541;

    public dpMM(int numPairs, int maxVolume, String filename) throws Exception {
        this.DATA_SIZE = numPairs;
        this.MAX_VOLUME_LENGTH = maxVolume;
        this.CUCKOO_LEVEL = (int) Math.ceil(Math.log(MAX_VOLUME_LENGTH) / Math.log(2.0));
        Setup(filename);
    }
    // 直接从文件名中获取数据大小和最大volume(非通用)
    public dpMM(String filename) throws Exception {
        int[] params = tool.Get_Total_Max_Num(filename);
        this.DATA_SIZE = params[0];
        this.MAX_VOLUME_LENGTH = params[1];
        this.CUCKOO_LEVEL = (int) Math.ceil(Math.log(MAX_VOLUME_LENGTH) / Math.log(2.0));
        Setup(filename);
    }


    public void Setup(String filename) throws Exception {
        KV[] kv_list = SerialData.Serial_Raw_In(filename);
        assert kv_list != null;
        KL[] kl_list = tool.KV_2_KL(kv_list);

        System.out.println("---------------------cuckoo hash scheme(dprfMM CCS'19) on " + filename + "---------------------");

        // 存储 l(key)
        CT = new Cuckoo_Hash();
        CT.Setup(kl_list);
        STORAGE_CT = CT.Get_Table_Size();

        // 存储数据
        Data = new Cuckoo_Hash();
        Data.Setup(kv_list, CUCKOO_LEVEL);
        STORAGE_Data = Data.Get_Table_Size();
    }

    public ArrayList<String> DpQuery(String search_key) throws Exception {

        System.out.println("\nClient is generating token ...\nkeywords : >>>   " + (search_key) + "   <<<");
        byte[] tk_key = GenSearchToken(search_key);

        // 服务器返回 l(key)
        System.out.println("\nServer is searching l(key) ... ");
        ArrayList<byte[]> l_key_server = Query_l_key(tk_key);

        // 客户端获取 l(key)
        int l_key_client = Decrypt_l_key(l_key_server, search_key);
        if (l_key_client == -1){
            l_key_client = SearchCTStash(search_key);
        }
        if (l_key_client == -1){
            System.out.println("l(key) not found!");
            return null;
        }
        // 客户端添加噪声
        int l_key = Add_Noise(l_key_client);

        // 服务器返回结果
        ArrayList<byte[]> ServerResult = Query_Data(tk_key,l_key);

        // 解密结果
        ArrayList<String> ClientResult = dprfMM.dprfMM.DecryptResult(ServerResult,search_key);

        // 搜索stash
        SearchDataStash(search_key,ClientResult);

        return ClientResult;
    }

    // 提供非静态的token生成方法 (静态方法参考dprfMM方案)
    public byte[] GenSearchToken(String search_key){
        return Hash.Get_SHA_256((search_key + Cuckoo_Hash.Get_K_d()).getBytes(StandardCharsets.UTF_8));
    }

    // 服务器查询 l(key)（静态与非静态）
    public ArrayList<byte[]> Query_l_key(byte[] token) throws Exception {
        ArrayList<byte[]> l_key = new ArrayList<>();

        byte[] father_Node = GGM.Doub_GGM_Path(token, 1, tool.DecimalConversion(1, 2,1));
        int t0 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9),STORAGE_CT,0);
        int t1 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),STORAGE_CT,1);
        l_key.add(CT.Get_EMM()[t0]);
        l_key.add(CT.Get_EMM()[t1]);

        return l_key;
    }
    public static ArrayList<byte[]> Query_l_key(byte[] token,Cuckoo_Hash CT) throws Exception {
        ArrayList<byte[]> l_key = new ArrayList<>();

        byte[] father_Node = GGM.Doub_GGM_Path(token, 1, tool.DecimalConversion(1, 2,1));
        int t0 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9),CT.Get_Table_Size(),0);
        int t1 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),CT.Get_Table_Size(),1);
        l_key.add(CT.Get_EMM()[t0]);
        l_key.add(CT.Get_EMM()[t1]);

        return l_key;
    }


    // 客户端解密获取 l(key)
    public static int Decrypt_l_key(ArrayList<byte[]> ServerResult, String search_key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int l_key;

        String s0 = new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(),ServerResult.get(0)));
        String[] s0_list = s0.split(",");
        if(s0_list[0].equals(search_key)){
            l_key = Integer.parseInt(s0_list[1]);
            return l_key;
        }
        String s1 = new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(),ServerResult.get(1)));
        String[] s1_list = s1.split(",");
        if(s1_list[0].equals(search_key)){
            l_key = Integer.parseInt(s1_list[1]);
            return l_key;
        }
        // 如果没有找到
        return -1;
    }

    // 客户端添加噪声（静态与非静态）
    public int Add_Noise(int l_key) {
        return (int) (l_key + Laplace.getNoise() + l_lambda);
    }
    public static int Add_Noise(int l_key, int l_lambda) {
        return (int) (l_key + Laplace.getNoise() + l_lambda);
    }

    // 提供静态和非静态的 Server_Query算法
    public ArrayList<byte[]> Query_Data(byte[] token,int l_key){
        ArrayList<byte[]> ServerResult = new ArrayList<>();
        //GGM.clear();
        for (int i = 0;i < l_key;i++ ) {
            byte[] father_Node = GGM.Doub_GGM_Path(token, CUCKOO_LEVEL, tool.DecimalConversion(i, 2, CUCKOO_LEVEL));
            int t0 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9),STORAGE_Data,0);
            int t1 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),STORAGE_Data,1);
            ServerResult.add(Data.Get_EMM()[t0]);
            ServerResult.add(Data.Get_EMM()[t1]);
        }
        return ServerResult;
    }
    public static ArrayList<byte[]> Query_Data(byte[] token,int l_key,Cuckoo_Hash Data){
        ArrayList<byte[]> ServerResult = new ArrayList<>();
        //GGM.clear();
        for (int i = 0;i < l_key;i++ ) {
            byte[] father_Node = GGM.Doub_GGM_Path(token, Data.Get_Level(), tool.DecimalConversion(i, 2, Data.Get_Level()));
            int t0 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9),Data.Get_Table_Size(),0);
            int t1 = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),Data.Get_Table_Size(),1);
            ServerResult.add(Data.Get_EMM()[t0]);
            ServerResult.add(Data.Get_EMM()[t1]);
        }
        return ServerResult;
    }

    public int SearchCTStash(String search_key){
        for (KV item: CT.Get_Stash()) {
            if (item.key.equals(search_key)) {
                return Integer.parseInt(item.value);
            }
        }
        return -1;
    }
    public void SearchDataStash(String search_key, ArrayList<String> ClientResult){
        ArrayList<KV> stash = Data.Get_Stash();//Get stash
        for (KV res : stash) {
            if (res.key.equals(search_key)) {
                ClientResult.add(res.value);
                System.out.println("Stash Result:" + res);
            }
        }
    }
}
