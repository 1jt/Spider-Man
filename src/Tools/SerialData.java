package Tools;

import java.io.*;
import java.util.ArrayList;

public class SerialData {
    public static void main(String[] args) {
        KV[] kvs = Serial_Raw_In("DB_random/Random_10_4.ser");
        // print kvs
        assert kvs != null;
        for (KV kv : kvs) {
            System.out.println(kv.key + " " + kv.value);
        }

    }

    // 原始数据集反序列化输出
    public static void Serial_Raw_Out(ArrayList<Integer> distribution,String fileName) {
        KV[] kv_list = generateKVs(distribution);
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(kv_list);

            oos.close();
            System.out.println("Random key-value pairs generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }
    private static KV[] generateKVs(ArrayList<Integer> distribution) {
        ArrayList<KV> kvs = new ArrayList<>();
        for (int i = 0; i < distribution.size(); i++) {
            for (int j = 0; j < distribution.get(i); j++) {
                kvs.add(new KV("Key" + i, "Value" + j));
            }
        }
        return kvs.toArray(new KV[0]);
    }

    // 原始数据集反序列化输入
    public static KV[] Serial_Raw_In(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            return (KV[]) in.readObject();
        } catch (EOFException e) {
            System.out.println("End of file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
