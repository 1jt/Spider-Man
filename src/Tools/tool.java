package Tools;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class tool {
    public static long bytesToLong(byte[] bytes) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (bytes[ix] & 0xff);
        }
        return num;
    }

    public static byte[] longToBytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static boolean Xor_Empty(byte[] xor) {
        for (int i = 0; i < xor.length; i++) {
            if (xor[i] == 0) {
                continue;
            } else
                return false;
        }
        return true;
    }


    public static byte[] Xor(byte[] x, byte[] y) {
        int min =0;
        if(x.length>y.length){
            min = y.length;
        }else{
            min = x.length;
        }
        byte[] temp = new byte[min];
        for (int i = 0; i < min; i++) {
            byte xx = x[i];
            byte yy = y[i];
            temp[i] = (byte) (x[i] ^ y[i]);
        }
        return temp;
    }



    // 将一个十进制数inNum转换为index进制，并将转换后的每一位数字存储在一个整数数组中，数组长度为level
    public static int[] DecimalConversion(int inNum, int index, int level) {
        int[] result = new int[level];
        int i = 0;
        while (i < level) {
            result[i] = (inNum % index);
            inNum = inNum / index;
            i++;
        }
        return result;
    }

    // 从文件名中获取数据集的大小和maxVolume
    public static int[] Get_Total_Max_Num(String filename){
        int[] result = new int[2];

        String[] temp = filename.split("_|\\.|/");
        if (Objects.equals(temp[2], "Random") || Objects.equals(temp[3], "Random")){
            result[0] = (int)Math.pow(2, Integer.parseInt(temp[temp.length-3]));
            result[1] = (int)Math.pow(2, Integer.parseInt(temp[temp.length-2]));
        }else if(Objects.equals(temp[2], "Zipf") || Objects.equals(temp[3], "Zipf")){
            result[0] = (int) Math.pow(2, Integer.parseInt(temp[temp.length-3]));
            result[1] = Integer.parseInt(temp[temp.length-2]);
        }else {
            System.out.println("The filename is not correct!");
        }

        return result;
    }

    // 从数据集文件中获取每个关键词对应值的数量
    public static KL[] KV_2_KL(KV[] kv_list){
        ArrayList<KL> kl_list = new ArrayList<KL>();
        kl_list.add(new KL(kv_list[0].key, 1));
        for (int i = 1; i < kv_list.length; i++) {
            int flag = 0;
            if (kl_list.get(kl_list.size() - 1).key.equals(kv_list[i].key)) {
                flag = 1;
                kl_list.get(kl_list.size() - 1).length++;
            }
            if (flag == 0) {
                kl_list.add(new KL(kv_list[i].key, 1));
            }
        }
        return kl_list.toArray(new KL[0]);
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