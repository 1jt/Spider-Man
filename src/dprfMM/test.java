package dprfMM;
import DataGen.ZipfDistribution;
import Tools.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.sun.org.apache.xpath.internal.NodeSet;
import dpMM.*;
import VHDSSE.*;
import chFB.*;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;


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
        /*
        VHDSSE vh = SerialData.Serial_VHDSSE_In("Zipf_9_109.ser");
        assert vh != null;
        ArrayList<String> result = vh.VHDSSE_Query("lyf");
        System.out.println("\nFinal Result: ");
        for (String s : result) {
            System.out.print(s + " ");
        }
        System.out.println();
        vh.Update();

         */
//        // TODO 写一下多次查询的
//        ArrayList<String> result_2 = vh.VHDSSE_Query("lyf");
//        System.out.println("\nFinal Result: ");
//        for (String s : result_2) {
//            System.out.print(s + " ");
//        }

        /// test chFB
        // 生成方案并序列化写入文件
//        System.out.println("----------------------------------------------test chFB-------------------------------------------");
//        String filename = "Shuffle/DB_zipf/Zipf_10_189.ser";
//        chFB chfb = new chFB(filename);
//        SerialData.Serial_DB_Out(chfb,filename.split("/")[2]);

        // 序列化读出并运行

//        chFB chfb = SerialData.Serial_chFB_In("Zipf_9_109.ser");
//        assert chfb != null;
//        chfb.Query_Update();




//         test show
        String fileStart = "Shuffle/DB_zipf";//对文件夹下所有文件遍历测试

        ArrayList<String> fileList = GetFileList(fileStart);
        for (String s : fileList) {
            System.out.println("************************" + s + "*********************************");
            String filename = s;

            //Setup
            dprfMM dprf = new dprfMM(filename);
            dpMM dp = new dpMM(filename);
            chFB chfb = new chFB(filename);
            VHDSSE vhdsse = new VHDSSE(filename);
            System.out.println("dprf服务器存储开销为" + GetSeverCost(dprf.cuckoo));
            System.out.println("dpMM服务器存储开销为" + GetSeverCost(dp.Data));
            System.out.println("chfb服务器存储开销为" + GetSeverCost(chfb.twoChoiceHash));
            System.out.println("vhdsse服务器存储开销为" + GetSeverCost(vhdsse.EMM_buf) + GetSeverCost(vhdsse.EMM_stash));
            System.out.println("___________________________________________________________________________________________");

            //Query
            int querySizeDprf = 0;
            long queryTimeDprf = 0;

            int querySizeDp = 0;
            long queryTimeDp = 0;

            int querySizechfb = 0;
            long queryTimechfb = 0;

            int querySizevhdsse = 0;
            long queryTimevhdsse = 0;

            int testTimes = 1000;

            for (int i = 0; i < testTimes; i++) {
                Random rd = new Random();
                int index = rd.nextInt(dprf.DATA_SIZE / 8); //齐夫数据集 m = n /8
                String query_key = "Key" + index;
                //********************************************dprfMM*************************************
                long startTimeQueryDprf = System.nanoTime(); // 记录开始时间
                // 需要测试运行时间的代码段区间
                ArrayList<byte[]> dprfResult = dprf.DprfQueryCost(query_key);
                long endTimeQueryDprf = System.nanoTime(); // 记录结束时间
                long executionTimeQueryDprf = (endTimeQueryDprf - startTimeQueryDprf)/1000; // 计算代码段的运行时间（纳秒）
                if (dprfResult!=null){
                    querySizeDprf += dprfResult.size();
                    queryTimeDprf += executionTimeQueryDprf;
                }else i--;

            }
            for (int i = 0; i < testTimes; i++) {
                Random rd = new Random();
                int index = rd.nextInt(dp.DATA_SIZE / 8); //齐夫数据集 m = n /8
                String query_key = "Key" + index;

                //********************************************dpMM*************************************
                long startTimeQueryDp = System.nanoTime(); // 记录开始时间
                // 需要测试运行时间的代码段区间
                ArrayList<byte[]> dpResult = dp.DpQuery(query_key);
                long endTimeQueryDp = System.nanoTime(); // 记录结束时间
                long executionTimeQueryDp = (endTimeQueryDp - startTimeQueryDp)/1000; // 计算代码段的运行时间（纳秒）
                if (dpResult!=null){
                    querySizeDp += dpResult.size();
                    queryTimeDp += executionTimeQueryDp;
                }else i--;

            }
            for (int i = 0; i < testTimes; i++) {
                Random rd = new Random();
                int index = rd.nextInt(chfb.DATA_SIZE / 8); //齐夫数据集 m = n /8
                String query_key = "Key" + index;

                //********************************************chfb*************************************
                long startTimeQuerychfb = System.nanoTime(); // 记录开始时间
                // 需要测试运行时间的代码段区间
                ArrayList<byte[]> chfbResult = chfb.QueryCost(query_key);
                ArrayList<String> chfbResult2 = chfb.Query(query_key);
                long endTimeQuerychfb = System.nanoTime(); // 记录结束时间
                long executionTimeQuerychfb = (endTimeQuerychfb - startTimeQuerychfb)/1000; // 计算代码段的运行时间（纳秒）
                if (chfbResult2!=null){
                    querySizechfb += chfbResult2.size();
                    queryTimechfb += executionTimeQuerychfb;
                }else i--;

            }
            for (int i = 0; i < testTimes; i++) {
                Random rd = new Random();
                int index = rd.nextInt(vhdsse.DATA_SIZE/ 8); //齐夫数据集 m = n /8
                String query_key = "Key" + index;
                //********************************************vhdsse*************************************
                long startTimeQueryvhdsse = System.nanoTime(); // 记录开始时间
                // 需要测试运行时间的代码段区间

                ArrayList<byte[]> vhdsseResult = vhdsse.VHDSSE_Query_Server(query_key);
                long endTimeQueryvhdsse = System.nanoTime(); // 记录结束时间
                long executionTimeQueryvhdsse = (endTimeQueryvhdsse - startTimeQueryvhdsse)/1000; // 计算代码段的运行时间（纳秒）
                if (vhdsseResult!=null){
                    querySizevhdsse += vhdsseResult.size();
                    queryTimevhdsse += executionTimeQueryvhdsse;
                }else i--;


            }
            System.out.println("dprfMM查询通信开销为"+querySizeDprf/testTimes);
            System.out.println("dprfMM平均每次查询用时(毫秒)" +queryTimeDprf/testTimes );
            System.out.println("dpMM查询通信开销为"+querySizeDp/testTimes);
            System.out.println("dpMM平均每次查询用时(毫秒)" +queryTimeDp/testTimes );
            System.out.println("chfb查询通信开销为"+querySizechfb/testTimes);
            System.out.println("chfb平均每次查询用时(毫秒)" +queryTimechfb/testTimes );
            System.out.println("vhdsse查询通信开销为"+querySizevhdsse/testTimes);
            System.out.println("vhdsse平均每次查询用时(毫秒)" +queryTimevhdsse/testTimes );

        }




    }

    public static ArrayList<String> GetFileList(String s){
        Path startPath = Paths.get(s); // 替换为你的目录路径
        ArrayList<String> Filelist = new ArrayList<>();
        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Filelist.add(file.toString().replace('\\', '/')); // 将路径中的\替换为/
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Filelist;
    }
    //计算服务器存储开销
    public static int GetSeverCost(Object data){
        int datasize = 0;
        datasize = (int) (ObjectSizeCalculator.getObjectSize(data));
        return datasize;
    }


}
