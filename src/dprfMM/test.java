package dprfMM;
import DataGen.ZipfDistribution;
import Tools.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import dpMM.*;
import VHDSSE.*;


public class test {
    public static void main(String[] args) throws Exception {
//        ZipfDistribution zipf = new ZipfDistribution(1024, 128);
//        // print zipf.distribution
//        for (int i = 0; i < zipf.distribution.size(); i++) {
//            System.out.print(zipf.distribution.get(i) + " ");
//        }
//
//        System.out.println();
//        System.out.println("Hello World!");
//        System.out.println(Hash.reduce(-5,5));
//
//        long num = -1516514651;
//        byte[] current_node = tool.longToBytes(num);
//        // 16进制输出 num
//        System.out.println(Long.toHexString(num));
//
//
//        for (int i = 0; i < current_node.length; i++) {
//            System.out.print(Integer.toHexString(current_node[i] & 0xff) + " ");
//        }
//        current_node = Arrays.copyOfRange(current_node, 0, 8);
//        System.out.println();
//        System.out.println("current_node.length: " + current_node.length);
//        for (byte b : current_node) {
//            System.out.print(Integer.toHexString(b & 0xff) + " ");
//        }
//        current_node = Hash.Get_SHA_128(current_node);
//        System.out.println();
//        System.out.println("current_node.length: " + current_node.length);
//        for (byte b : current_node) {
//            System.out.print(Integer.toHexString(b & 0xff) + " ");
//        }
//        System.out.println();

//         test AESUtil.encrypt
//        System.out.println("----------------------------------------------test AESUtil.encrypt-------------------------------------------");
//        byte[] key = Hash.Get_SHA_256("123".getBytes());
//        byte[] value = "hello".getBytes();
//        System.out.println("value.length: " + value.length);
//        for (byte b : value) {
//            System.out.print(Integer.toHexString(b & 0xff) + " ");
//        }
//        System.out.println();
//        byte[] encrypted = AESUtil.encrypt(key, value);
//        System.out.println("encrypted.length: " + encrypted.length);
//        for (byte b : encrypted) {
//            System.out.print(Integer.toHexString(b & 0xff) + " ");
//        }
//
////         test AESUtil.decrypt
//        System.out.println("----------------------------------------------test AESUtil.decrypt-------------------------------------------");
//        byte[] decrypted = AESUtil.decrypt(key, encrypted);
//        System.out.println();
//        System.out.println("decrypted.length: " + decrypted.length);
//        for (byte b : decrypted) {
//            System.out.print(Integer.toHexString(b & 0xff) + " ");
//        }

        // test Cuckoo_Hash
//        System.out.println("----------------------------------------------test Cuckoo_Hash-------------------------------------------");
//
//        KV[] kv_list = SerialData.Serial_Raw_In("DB_random/Random_10_4.ser");
//        kv_list[1].key = "key" + 0;
//
//        int MAX_VOLUME_LENGTH = (int) Math.pow(2, 4);
//
//        int CUCKOO_LEVEL = (int) Math.ceil(Math.log(MAX_VOLUME_LENGTH) / Math.log(2.0));//GGM Tree level for cuckoo hash
//
//        Cuckoo_Hash cuckoo = new Cuckoo_Hash();
//        cuckoo.Setup(kv_list, CUCKOO_LEVEL);
//        for (int i = 0; i < cuckoo.Get_Table_Size()*2; i++) {
//            System.out.println(cuckoo.Get(i));
//        }

        // test dprfMM
//        System.out.println("----------------------------------------------test dprfMM-------------------------------------------");
//        String filename = "Shuffle/DB_zipf/Zipf_9_113.ser";
//        int[] params = tool.Get_Total_Max_Num(filename);
//
//        // 不要求中间结果的情况下，以下两行已经包含了所有操作
//        dprfMM dprf = new dprfMM(params[0],params[1],filename);
//        // dprfMM dprf = new dprfMM(filename); // 简化构造
//        ArrayList<String> result = dprf.DprfQuery("Key0");
//
//        System.out.println("\nFinal Result: ");
//        for (String s : result) {
//            System.out.print(s + " ");
//        }
//        // 测试token
//        byte[] tk_key = dprfMM.GenSearchToken("Key0", Cuckoo_Hash.Get_K_d());
//        System.out.println("\nGenerate token by static method:\n" + Arrays.toString(tk_key));
//        // 测试服务器返回结果
//        ArrayList<byte[]> ServerResult = dprfMM.Query_Cuckoo(tk_key,dprf.cuckoo,params[1]);
//        System.out.println("\nCiphertext and corresponding plaintext returned by the server: ");
//        for (byte[] ciphertext : ServerResult) {
//            System.out.print(Arrays.toString(ciphertext) + " ");
//            System.out.println(new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(), ciphertext)));
//        }
//


        // test KL
//        String filename = "DB_zipf/Zipf_15_3688.ser";
//        KV[] kv_list = SerialData.Serial_Raw_In(filename);
//        assert kv_list != null;
//        KL[] kl_list = tool.KV_2_KL(kv_list);
//        // print kl_list
////        for (KL kl : kl_list) {
////            System.out.println(kl.key + " " + kl.length);
////        }
//
        // test Laplace
//        double sensitivity = 2;
//        double epsilon = 0.2;
//        int l_lambda = 541;
//
//        for (KL kl : kl_list) {
//            double noise = Laplace.getNoise(sensitivity/epsilon);
//            System.out.println("The noise Client added:"+noise);
//            int pv_key = (int) (kl.length + l_lambda + noise);
//            System.out.println(pv_key);
//            break;
//        }

        // test dpMM
//        System.out.println("----------------------------------------------test dpMM-------------------------------------------");
//        String filename = "DB_zipf/Zipf_9_113.ser";
//        dpMM dp = new dpMM(filename);
//        ArrayList<String> result = dp.DpQuery("Key1049");
//
//        System.out.println("\nFinal Result: ");
//        for (String s : result) {
//            System.out.print(s + " ");
//        }
//        // 测试token
//        byte[] tk_key = dprfMM.GenSearchToken("Key45", Cuckoo_Hash.Get_K_d());
//        System.out.println("\nGenerate token by static method:\n" + Arrays.toString(tk_key));
//        // 测试服务器查询l(key)
//        ArrayList<byte[]> l_key = dpMM.Query_l_key(tk_key, dp.CT);
//        System.out.println("\nl(key) returned by the server: ");
//        for (byte[] ciphertext : l_key) {
//            System.out.print(Arrays.toString(ciphertext) + " ");
//            System.out.println(new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(), ciphertext)));
//        }
//        // 测试服务器返回结果
//        ArrayList<byte[]> ServerResult = dpMM.Query_Data(tk_key,10,dp.Data);
//        System.out.println("\nCiphertext and corresponding plaintext returned by the server: ");
//        for (byte[] ciphertext : ServerResult) {
//            System.out.print(Arrays.toString(ciphertext) + " ");
//            System.out.println(new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(), ciphertext)));
//        }

        // test VHDSSE
        System.out.println("----------------------------------------------test VHDSSE-------------------------------------------");
//        String filename = "Shuffle/DB_zipf/Zipf_9_117.ser";
//        int[] params = tool.Get_Total_Max_Num(filename);
//        VHDSSE vhdsse = new VHDSSE(params[0],params[1],filename);
//        SerialData.Serial_DB_Out(vhdsse,filename.split("/")[2]);
        VHDSSE vh = SerialData.Serial_VHDSSE_In("Zipf_9_117.ser");
        assert vh != null;
        vh.Update();
        // TODO 写一下多次查询的
        ArrayList<String> result = vh.VHDSSE_Query("Key10");
        System.out.println("\nFinal Result: ");
        for (String s : result) {
            System.out.print(s + " ");
        }

        // test Serial
//        dprfMM dprf = new dprfMM(params[0],params[1],filename);
//        SerialData.Serial_DB_Out(dprf,filename.split("/")[2]);
//        dprfMM dprf = SerialData.Serial_dprfMM_In("Zipf_9_118.ser");
//        // dprfMM dprf = new dprfMM(filename); // 简化构造
//        assert dprf != null;
//        ArrayList<String> result = dprf.DprfQuery("Key0");
//
//        System.out.println("\nFinal Result: ");
//        for (String s : result) {
//            System.out.print(s + " ");
//        }
//        dpMM dp = SerialData.Serial_dpMM_In("Zipf_15_3688.ser");
//        ArrayList<String> result = dp.DpQuery("Key1049");
//
//        System.out.println("\nFinal Result: ");
//        for (String s : result) {
//            System.out.print(s + " ");
//        }





    }
}
