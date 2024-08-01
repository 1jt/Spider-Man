package DataGen;

import Tools.KV;
import Tools.tool;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Tools.SerialData;

public class Shuffle_ser {
    public static void main(String[] args) {
        String fileName = "DB_zipf/Zipf_10_182.ser";
        String[] temp = fileName.split("_|\\.|/");
        // shuffle ser file
        if (temp[temp.length-1].equals("ser")) {
            String out_filePath = "Shuffle/" + fileName;  // 输出文件
            KV[] kvs = SerialData.Serial_Raw_In(fileName);
            // shuffle
            assert kvs != null;
            List<KV> kvlist = KVtoList(kvs);// KV to List
            Collections.shuffle(kvlist); // 随机打乱List中的数据
            KV[] kvs_shuffle = ListtoKV(kvlist); // List to KV
            SerialData.Serial_Raw_Out(kvs_shuffle, out_filePath);
        }   //  shuffle txt file
        else if (temp[temp.length-1].equals("txt")) {
            List<KV> lines = readLinesFromFile(fileName); // 读取txt文件内容到List中
            String out_filePath = "Shuffle/" + fileName.substring(0,fileName.length()-4)+"_"+tool.KV_2_KL(ListtoKV(lines))[1].length+".ser"; // ser文件路径
            // shuffle
            Collections.shuffle(lines); // 随机打乱List中的数据
            KV[] kvs_shuffle = ListtoKV(lines); // List to KV
            SerialData.Serial_Raw_Out(kvs_shuffle, out_filePath);
        } else {
            System.out.println("Invalid file type");
        }
    }

    // KV to List
    private static List<KV> KVtoList(KV[] kvs) {
        return new ArrayList<>(Arrays.asList(kvs));
    }
    // List to KV
    private static KV[] ListtoKV(List<KV> list) {
        KV[] kvs = new KV[list.size()];
        for (int i = 0; i < list.size(); i++) {
            kvs[i] = list.get(i);
        }
        return kvs;
    }

    // 读取txt文件的每行数据到List中(以KV形式)
    private static List<KV> readLinesFromFile(String fileName) {
        List<KV> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] temp = line.split("=");
                lines.add(new KV(temp[0], temp[1]));
            }
        } catch (IOException e) {
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
        return lines;
    }
}
