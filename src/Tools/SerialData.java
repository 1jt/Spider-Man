package Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeSet;

import VHDSSE.*;
import dprfMM.*;
import dpMM.*;
import chFB.*;


public class SerialData {
    public static void main(String[] args) {

        KV[] kvs = Serial_Raw_In("Shuffle/DB_zipf/Zipf_16_6908.ser");
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
    // 如果已经生成 KV[]，可以直接写入文件
    public static void Serial_Raw_Out(KV[] kv_list,String fileName) {
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



    // 数据集序列化存储（dprfMM）
    public static void Serial_DB_Out(dprfMM dprf,String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream("DB/dprfMM/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(dprf);

            oos.close();
            System.out.println("dprfMM generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }

    public static dprfMM Serial_dprfMM_In(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream("DB/dprfMM/" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            return (dprfMM) in.readObject();
        } catch (EOFException e) {
            System.out.println("End of file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 数据集序列化存储（dprfMM）
    public static void Serial_DB_Out(dpMM dp, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream("DB/dpMM/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(dp);

            oos.close();
            System.out.println("dp generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }
    public static dpMM Serial_dpMM_In(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream("DB/dpMM/" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            return (dpMM) in.readObject();
        } catch (EOFException e) {
            System.out.println("End of file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 数据集序列化存储（dprfMM）
    public static void Serial_DB_Out(VHDSSE vhdsse, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream("DB/VHDSSE/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(vhdsse);

            oos.close();
            System.out.println("VHDSSE generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }
    public static VHDSSE Serial_VHDSSE_In(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream("DB/VHDSSE/" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            return (VHDSSE) in.readObject();
        } catch (EOFException e) {
            System.out.println("End of file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void Serial_DB_Out(chFB chfb, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream("DB/chFB/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(chfb);

            oos.close();
            System.out.println("VHDSSE generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }
    public static chFB Serial_chFB_In(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream("DB/chFB/" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            return (chFB) in.readObject();
        } catch (EOFException e) {
            System.out.println("End of file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
