import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Query_NewDVH {
    public static ArrayList<Integer> list = new ArrayList<>();//存储查询返回pos序列
    public static ArrayList<String> queryHashmap (String key, int size, Map<MMPoint,TreeNode<byte[]>> hashMap) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        ArrayList<byte[]> Result = new ArrayList<>();//Result中记录查询到的数据，形式为加密形式
        ArrayList<String> resultString = new ArrayList<>();//记录解密后的数据
        String kappa = HashKit.sha1(key + 0 + 1);
        BigInteger tmp_3 = new BigInteger(kappa, 16);
        int root = tmp_3.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();
        list.clear();//由于是全局变量，每次使用前清理一下防止bug
        list.add(root);//序列第一个是根结点编号
        MMPoint mmPointRoot = new MMPoint(root , size - 1 - root);
        TreeNode<byte[]> node_tmp = hashMap.get(mmPointRoot);//记录当前节点
        int count = 0;
        while (node_tmp != null) {
            Result.add(node_tmp.getData());//记录当前节点数据
            int x = node_tmp.getId().getX();
            int y = node_tmp.getId().getY();

            String Pos = HashKit.sha384(kappa + root + count++);//记录下一层pos值
            BigInteger tmp_2 = new BigInteger(Pos, 16);
            int pos = tmp_2.divideAndRemainder(BigInteger.valueOf(2))[1].intValue();
            list.add(pos);//添加pos值

            if (pos == 0){
                if (node_tmp.getLeft()!=null){//判断左孩子节点是否为空，不为空，直接迭代节点
                    node_tmp = node_tmp.getLeft();
                }else {                       //为空，存在两种可能，1该位置真没有节点，2该位置有节点但没与当前节点建立父子关系
                    y = y + 1;
                    //在数据库中找到坐标为(x,y)的点
                    node_tmp = NewDVH_Tool.FindNode(x,y,hashMap);
                }
            }

            else if (pos == 1){
                if (node_tmp.getRight()!=null){//判断左孩子节点是否为空，不为空，直接迭代节点
                    node_tmp = node_tmp.getRight();
                }else {                       //为空，存在两种可能，1该位置真没有节点，2该位置有节点但没与当前节点建立父子关系
                    x = x + 1;
                    //在数据库中找到坐标为(x,y)的点
                    node_tmp = NewDVH_Tool.FindNode(x,y,hashMap);
                }

            }

        }
        String dekey = NewDVH_Tool.EncryKey;
        for (int i = 0; i < Result.size(); i++) {
            String de = NewDVH_Tool.Decrypt(dekey,Result.get(i));
            resultString.add(de);
        }
        return resultString;


    }
}
