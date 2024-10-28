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
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import dpMM.*;
import VHDSSE.*;
import chFB.*;


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
//        String filename = "Shuffle/DB_zipf/Zipf_9_109.ser";
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
//        System.out.println("----------------------------------------------test VHDSSE-------------------------------------------");
//        String filename = "Shuffle/DB_zipf/Zipf_9_109.ser";
//        int[] params = tool.Get_Total_Max_Num(filename);
//        VHDSSE vhdsse = new VHDSSE(params[0],params[1],filename);
//        SerialData.Serial_DB_Out(vhdsse,filename.split("/")[2]);
        VHDSSE vh = SerialData.Serial_VHDSSE_In("Zipf_9_109.ser");
        assert vh != null;
        ArrayList<String> result = vh.VHDSSE_Query("lyf");
        System.out.println("\nFinal Result: ");
        for (String s : result) {
            System.out.print(s + " ");
        }
        System.out.println();
        vh.Update();
//        // TODO 写一下多次查询的
//        ArrayList<String> result_2 = vh.VHDSSE_Query("lyf");
//        System.out.println("\nFinal Result: ");
//        for (String s : result_2) {
//            System.out.print(s + " ");
//        }

        /// test chFB
        // 生成方案并序列化写入文件
//        System.out.println("----------------------------------------------test chFB-------------------------------------------");
//        String filename = "Shuffle/DB_zipf/Zipf_9_109.ser";
//        chFB chfb = new chFB(filename);
//        SerialData.Serial_DB_Out(chfb,filename.split("/")[2]);

        // 序列化读出并运行
        chFB chfb = SerialData.Serial_chFB_In("Zipf_9_109.ser");
        assert chfb != null;
        chfb.Query_Update();


//         test show
        TreeNode<KV> root = new TreeNode<KV>(new KV("key0","value0"));
        TreeNode<KV> node1 = new TreeNode<KV>(new KV("key1","value1"));
        TreeNode<KV> node2 = new TreeNode<KV>(new KV("key2","value2"));
        TreeNode<KV> node3 = new TreeNode<KV>(new KV("key3","value3"));
        TreeNode<KV> node4 = new TreeNode<KV>(new KV("key4","value4"));
        TreeNode<KV> node5 = new TreeNode<KV>(new KV("key5","value5"));
        TreeNode<KV> node6 = new TreeNode<KV>(new KV("key6","value6"));
        TreeNode<KV> node7 = new TreeNode<KV>(new KV("key7","value7"));
        TreeNode<KV> node8 = new TreeNode<KV>(new KV("key8","value8"));
        TreeNode<KV> node9 = new TreeNode<KV>(new KV("key9","value9"));
        TreeNode<KV> node10 = new TreeNode<KV>(new KV("key10","value10"));
        TreeNode<KV> node11 = new TreeNode<KV>(new KV("key11","value11"));
        TreeNode<KV> node12 = new TreeNode<KV>(new KV("key12","value12"));
        TreeNode<KV> node13 = new TreeNode<KV>(new KV("key13","value13"));
        TreeNode<KV> node14 = new TreeNode<KV>(new KV("key14","value14"));
        TreeNode<KV> node15 = new TreeNode<KV>(new KV("key15","value15"));

        root.setLeft(node1);
        root.setRight(node2);
        node1.setLeft(node3);
        node1.setRight(node4);
        node2.setLeft(node5);
        node2.setRight(node6);
        node3.setLeft(node7);
        node3.setRight(node8);
        node4.setLeft(node9);
        node4.setRight(node10);
        node5.setLeft(node11);
        node5.setRight(node12);
        node6.setLeft(node13);
        node6.setRight(node14);
        System.out.println();
        TreeOperation.show(root);






    }
}
