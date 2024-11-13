import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import Tools.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class NewDVH_Tool {

    public static String EncryKey = "Key1000";//临时加解密秘钥
    //计算size大小
    public static int Size(String filePath) throws IOException {
        int size;
        KV[] kvs = Serial_Raw_In(filePath);
        int m = (int)Math.pow(2,getLastCharAsNumber(filePath));
        int n = kvs.length;
        double c = 1;
        size = (int) (c * n / m);
        return size;
    }


    public static TreeNode FindNode(int x,int y,ArrayList<NodeSet>Position){

        for (int i = 0; i < Position.size(); i++) {
            int aid_x = Position.get(i).getPosition().getX();
            int aid_y = Position.get(i).getPosition().getY();
            if (aid_x == x && aid_y == y) {
                TreeNode node = Position.get(i).getNode();
                return node;
            }
        }
        return null;
    }

    public static TreeNode<byte[]> FindNode(int x,int y,Map<MMPoint,TreeNode<byte[]>> hashMap){
        MMPoint mmPoint = new MMPoint(x,y);
        return hashMap.get(mmPoint);
    }

    public static int getLastCharAsNumber(String str) throws NumberFormatException {//通过文件名获取volume.max
        if (str == null || str.isEmpty()) {
            throw new NumberFormatException("The string is null or empty.");
        }
        char lastChar = str.charAt(str.length() - 5);
        return Character.getNumericValue(lastChar);
    }

    public static byte[] Encrypt(String key,String value) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] enkey = Hash.Get_SHA_256(key.getBytes(StandardCharsets.UTF_8));
        byte[] encrypted = AESUtil.encrypt(enkey, value.getBytes(StandardCharsets.UTF_8));
        return encrypted;
    }

    public static String Decrypt(String key,byte[] envalue) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] enkey = Hash.Get_SHA_256(key.getBytes(StandardCharsets.UTF_8));
        byte[] decrypted = AESUtil.decrypt(enkey, envalue);
        String s = new String(decrypted);
        return s;
    }

    //序列化存储
    public static void Serial_NewDVH_Out(ArrayList<NodeSet> position, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream("DB/NewDVH/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(position);

            oos.close();
            System.out.println("NewDVH generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }


    //序列化读取
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


    //为删除功能计算更新pos序列，需要输入目标key ，value ，查询pos序列，查询返回结果，尺寸size
    public static ArrayList<Integer> UpList(String value,String key, ArrayList<Integer> list, ArrayList<String>query_result,int size) {
        Random random = new Random();
        ArrayList<Integer> uplist = new ArrayList<>();
        int num = 0;//记录更新序列大小
        int num_1 = 0;//记录1的数量
        int num_0 = 0;//记录0的数量
        String val = key + "=Value" + value;
        //遍历查找返回结果，直到查找到目标数据为止
        for (String s : query_result) {
            num += 1;
            if (Objects.equals(val, s))
                break;
        }
        //遍历查询pos序列，记录0和1的数量
        for (int i = 1; i < num; i++) {
            if (list.get(i) == 1)
                num_1++;
            else num_0++;
            uplist.add(0);//初始化更新序列，全为0
        }
        //在原本根节点附近设定范围内随机生成一个根节点编号，作为更新路径的根节点
        int Query_root = list.get(0);
        int Update_root;
        while (true) {
            Update_root = random.nextInt(num_1 + num_0 + 1) + Query_root - num_0;
            if (Update_root >= 0 && Update_root <= size)
                break;
        }
        for (int j = 0; j < Query_root + num_1 - Update_root; j++) {
            uplist.set(j, 1);//生成固定个数1
        }
        Collections.shuffle(uplist);//打乱序列
        uplist.add(0, Update_root);//根节点编号放在序列第一个
        return uplist;
    }

    //为添加功能计算更新pos序列，需要输入目标key ，value ，查询pos序列，查询返回结果，尺寸size
    public static ArrayList<Integer> AddUpList(String value, String key ,ArrayList<Integer> list, ArrayList<String>query_result,int size) {
        Random random = new Random();
        ArrayList<Integer> uplist = new ArrayList<>();
        int num = 0;//记录更新序列大小
        int num_1 = 0;//记录1的数量
        int num_0 = 0;//记录0的数量
        String dummy ="Dummy";
        boolean next_pos = true;
        //遍历查找返回结果，判断是否有dummy，碰到dummy就停止
        for (String s : query_result) {
            num += 1;
            if (Objects.equals(dummy, s)) {
                next_pos = false;
                break;
            }
        }
        //遍历pos值序列
        int pos = list.get(list.size() - 1);//list最后一个值是找到空节点要用的pos
        list.remove(list.size() - 1);//为了找到最后一个存在数据的节点
        for (int i = 1; i < num; i++) {
            if (list.get(i) == 1)
                num_1++;
            else num_0++;
            uplist.add(0);
        }

        //生成随机更新路径
        int Query_root = list.get(0);
        int Update_root;
        while (true) {
            Update_root = random.nextInt(num_1 + num_0 + 1) + Query_root - num_0;
            if (Update_root >= 0 && Update_root <= size)
                break;
        }
        for (int j = 0; j < Query_root + num_1 - Update_root; j++) {
            uplist.set(j, 1);//生成固定个数1
        }
        Collections.shuffle(uplist);//打乱序列
        uplist.add(0, Update_root);
        //无dummy
        if (next_pos) {
            uplist.add(pos);
        }

        return uplist;
    }

}
