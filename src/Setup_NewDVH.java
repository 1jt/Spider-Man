import javafx.css.StyleableObjectProperty;
import jdk.internal.dynalink.beans.StaticClass;
import org.w3c.dom.NodeList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import Tools.*;
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Setup_NewDVH {
    public static ArrayList<NodeSet> Position = new ArrayList<>();//存储所有NodeSet

    public static void Test(String filePath) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {


//        System.out.println("----------" + filePath + " starts Setup calculation----------");

        int size = NewDVH_Tool.Size(filePath);
        KV[] kvs = NewDVH_Tool.Serial_Raw_In(filePath);//kv形式读取数据
        TreeNode<byte[]>[] roots = Roots.CreateRoots(size);//建立size个根节点
        String key = null; // 每次读取的关键词
        String value = null; // 每次读取关键词对应的值
        String kappa; // 每个关键词生成的密钥
        String dekey = NewDVH_Tool.EncryKey;//临时加密秘钥
        int root; // 关键词对应的根节点的编号
        Map<MMPoint,TreeNode<byte[]>> hashMap = new HashMap<>();//使用map能否更快
        for (int i = 0; i < kvs.length; i++) {
            key = kvs[i].key;
            value = kvs[i].value;
            String addValue = key + "=" + value;//存入数据仍然为Key**=Value**型
            // 计算kappa
            kappa = HashKit.sha1(key + 0 + 1);//这里kappa只用1这一个路径

            // 计算root编号
            BigInteger tmp = new BigInteger(kappa, 16);
            root = tmp.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();

            //加密模块
            byte[] envalue = NewDVH_Tool.Encrypt(dekey, addValue);

            // 如果对应根节点没有数据，则执行初始化操作
            if (roots[root].getData() == null) {
                roots[root].setData(envalue);
                //计算根节点在坐标轴的位置
                MMPoint NodePosition = new MMPoint(root, size - 1 - root);
                roots[root].setId(NodePosition);
                //位置和节点一起存入Nodeset中
                NodeSet node_temp = new NodeSet(NodePosition, roots[root]);
                Position.add(node_temp);
                hashMap.put(NodePosition,roots[root]);//hashmap记录坐标与节点对应关系
                continue;//开启下一次循环，读取下一个数据
            }

            // 根节点已经存在数据
            TreeNode<byte[]> node_tmp = roots[root];//node_temp表示当前节点
            int count = 0; // 关键词所在层数

            while (true) {
                String Pos = HashKit.sha384(kappa + root + count++);
                BigInteger tmp_2 = new BigInteger(Pos, 16);
                int pos = tmp_2.divideAndRemainder(BigInteger.valueOf(2))[1].intValue(); // 计算往左还是往右
                //往左
                if (pos == 0) {
                    //计算左孩子节点xy坐标
                    int child_x = node_tmp.getId().getX();
                    int child_y = node_tmp.getId().getY() + 1;
                    MMPoint NodePosition = new MMPoint(child_x, child_y);

                    //判断左孩子位置有人不，有人则把该节点变为你的左孩子
                    //这里可以优化，使用哈希表判断是否xy是否存在，能更快
                    if (node_tmp.getLeft() == null) {
                        if (hashMap.containsKey(NodePosition)){//hashset.contains(NodePosition)
                            node_tmp.setLeft(hashMap.get(NodePosition));//建立父子关系
                            node_tmp = node_tmp.getLeft();//当前节点迭代
                            //遍历Position，xy坐标匹配成功表示该位置存在节点，但没有与node_temp建立父子关系
                            continue;//跳出大循环，计算下一层位置
                        }
                        //位置没人，建立一个节点，并存入Position中
                        TreeNode<byte[]> node_left = new TreeNode<byte[]>(envalue);
                        node_tmp.setLeft(node_left);
                        node_tmp.setLeftId(NodePosition);
                        NodeSet node_cash = new NodeSet(NodePosition, node_left);
                        Position.add(node_cash);//插入成功，读取下一个数据
                        hashMap.put(NodePosition,node_left);
                        break;
                    } else {
                        node_tmp = node_tmp.getLeft();//左节点存在数据，当前节点转变为其左孩子
                    }
                }
                //  往右
                else if (pos == 1) {
                    //计算做右孩子xy坐标
                    int child_x = node_tmp.getId().getX() + 1;
                    int child_y = node_tmp.getId().getY();
                    MMPoint NodePosition = new MMPoint(child_x, child_y);

                    //左孩子为空，判断左孩子位置有人不，有人则把该节点变为你的左孩子，对象更迭，没人就初始化一个占据该位置
                    if (node_tmp.getRight() == null) {
                        if (hashMap.containsKey(NodePosition)){//hashset.contains(NodePosition)
                            node_tmp.setRight(hashMap.get(NodePosition));
                            node_tmp = node_tmp.getRight();
                            continue;
                        }

                        TreeNode<byte[]> node_right = new TreeNode<byte[]>(envalue);
                        node_tmp.setRight(node_right);
                        node_tmp.setRightId(NodePosition);
                        NodeSet node_cash = new NodeSet(NodePosition, node_right);
                        Position.add(node_cash);
                        //hashset.add(NodePosition);
                        hashMap.put(NodePosition,node_right);
                        break;
                    } else {
                        node_tmp = node_tmp.getRight();
                    }
                }
            }
        }

    }
}