package dprfMM;

import java.io.*;

public class ObjectOutputStreamDemo {
    public static void main(String[] args) {
        BinTree B_1 = new BinTree("10");
        BinTree B_2 = new BinTree("20");
        BinTree B_3 = new BinTree("30");
        BinTree B_4 = new BinTree("40");
        BinTree B_5 = new BinTree("50");
        BinTree B_6 = new BinTree("60");
        BinTree B_7 = new BinTree("70");
        BinTree B_8 = new BinTree("80");
        BinTree B_9 = new BinTree("90");
        B_1.left = B_2;
        B_2.left = B_3;
        B_3.left = B_4;
        B_4.left = B_5;
        B_5.right = B_6;
        B_6.right = B_7;
        B_7.right = B_8;
        B_8.right = B_9;

        try {
            FileOutputStream fos = new FileOutputStream("BT.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(B_1);
//            oos.writeObject(B_3);
//            oos.writeObject(B_2);
//            oos.writeObject(B_4);
//            oos.writeObject(B_5);
//            oos.writeObject(B_6);
//            oos.writeObject(B_7);
//            oos.writeObject(B_8);
//            oos.writeObject(B_9);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fileIn = new FileInputStream("BT.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            // 从指定的文件输入流中读取对象并反序列化
            BinTree obj_1 = (BinTree) in.readObject();
//            BinTree obj_2 = (BinTree) in.readObject();
//            BinTree obj_2 = (BinTree) obj_1.left;
//            System.out.println("Deserialized Object: " + obj_1.left.left.left.left.value);
            System.out.println("Deserialized Object: " + obj_1.left.left.value);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
class BinTree implements Serializable {
    String value;
    BinTree left;
    BinTree right;
    BinTree(String value) {
        this.value = value;
    }
}