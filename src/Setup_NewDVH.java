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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Setup_NewDVH {
    //    public static ArrayList<TreeNode> Rroots = new ArrayList<>();//测试用，装有所有根节点
    public static ArrayList<NodeSet> Position = new ArrayList<>();//存储所有NodeSet


    public static ArrayList<TreeNode> Test(String filePath) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {


//        System.out.println("----------" + filePath + " starts Setup calculation----------");

        int size = NewDVH_Tool.Size(filePath);
        TreeNode<String>[] roots = Roots.CreateRoots(size);//建立size个根节点
        ArrayList<TreeNode> nodeList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String key = null; // 每次读取的关键词
        String value = null; // 每次读取关键词对应的值
        String kappa; // 每个关键词生成的密钥
        int root; // 关键词对应的根节点的编号

        while ((line = reader.readLine()) != null) {
            // 读取键值对
            String[] keyValue = line.split("=");
            if (keyValue.length == 2) {
                key = keyValue[0].trim();
                value = keyValue[1].trim();
            } else {
                System.out.println("Invalid key-value pair: " + line);
            }

            // 计算kappa
            kappa = HashKit.sha1(key + 0 + 1);//这里kappa只用1这一个路径

            // 计算root编号
            BigInteger tmp = new BigInteger(kappa, 16);
            root = tmp.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();

            //加密模块
            byte[] envalue = NewDVH_Tool.Encrypt(key, value);

            // 如果对应根节点没有数据，则执行初始化操作
            if (roots[root].getData() == null) {
                roots[root].setData(key + "+" + value);
                //计算根节点在坐标轴的位置
                MMPoint NodePosition = new MMPoint(root, size - 1 - root);
                roots[root].setId(NodePosition);
                //位置和节点一起存入Nodeset中
                NodeSet node_temp = new NodeSet(NodePosition, roots[root]);
                Position.add(node_temp);
                nodeList.add(roots[root]);
                continue;//开启下一次循环，读取下一个数据
            }

            // 根节点已经存在数据
            TreeNode<String> node_tmp = roots[root];//node_temp表示当前节点
            int count = 0; // 关键词所在层数
            boolean flag = false; // 放入成功指示符
            String input = key + "+" + value;

            STOP:
            while (!flag) {
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
                        for (int pos_num = 0; pos_num < Position.size(); pos_num++) {
                            int aid_x = Position.get(pos_num).getPosition().getX();
                            int aid_y = Position.get(pos_num).getPosition().getY();
                            if (aid_x == child_x && aid_y == child_y) {
                                node_tmp.setLeft(Position.get(pos_num).getNode());
                                node_tmp = node_tmp.getLeft();
                                //遍历Position，xy坐标匹配成功表示该位置存在节点，但没有与node_temp建立父子关系
                                continue STOP;//跳出大循环，计算下一层位置
                            }
                        }
                        //位置没人，建立一个节点，并存入Position中
                        TreeNode<String> node_left = new TreeNode<String>(input);
                        node_tmp.setLeft(node_left);
                        node_tmp.setLeftId(NodePosition);
                        NodeSet node_cash = new NodeSet(NodePosition, node_left);
                        Position.add(node_cash);//插入成功，读取下一个数据
                        nodeList.add(node_left);
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
                        for (int pos_num = 0; pos_num < Position.size(); pos_num++) {
                            int aid_x = Position.get(pos_num).getPosition().getX();
                            int aid_y = Position.get(pos_num).getPosition().getY();
                            if (aid_x == child_x && aid_y == child_y) {
                                node_tmp.setRight(Position.get(pos_num).getNode());
                                node_tmp = node_tmp.getRight();
                                continue STOP;
                            }
                        }
                        TreeNode<String> node_right = new TreeNode<String>(input);
                        node_tmp.setRight(node_right);
                        node_tmp.setRightId(NodePosition);
                        NodeSet node_cash = new NodeSet(NodePosition, node_right);
                        Position.add(node_cash);
                        nodeList.add(node_right);
                        break;
                    } else {
                        node_tmp = node_tmp.getRight();
                    }
                }
            }
        }
        return nodeList;
    }
}