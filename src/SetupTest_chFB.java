import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class SetupTest_chFB {
    public static void Test(String filePath) throws IOException {
        long Time_total = 0;
        int cycle_num = 10; //构建次数
        System.out.println("----------" + filePath + " starts chFB_Setup calculation----------");
        for (int test_num = 0; test_num < cycle_num; test_num++) {
            // 树的数量
            int size;
            // 常数
            int c = 10;
            // 计算数据库条目数
            int n = Tools.CalculateNumberOfDBEntries(filePath);
            // 计算size
            size = (int) Math.ceil(n / ((Math.log(n) / Math.log(2)) * c));
            // 计算height
            int h = (int) Math.ceil(Math.log(c * Math.log(n) / Math.log(2)) / Math.log(2));
            // 初始化哈希环的根节点们
            TreeNode<String>[] roots = Roots.CreateRoots(size);
            // 读取文件
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            String key = null; // 每次读取的关键词
            String value = null; // 每次读取关键词对应的值
            String kappa; // 每个关键词生成的密钥

            // Setup
//            System.out.println("-----------------------Setup parse begin-----------------------");
            long start = System.nanoTime();

            // 构建满二叉树
            for (int i = 0; i < size; i++) {
                TreeNode<String> node = Tools.FillTree(h);
                roots[i] = node;
            }

            // Stash
            ArrayList<String> Stash = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                // 读取键值对
                String[] keyValue = line.split("=");
                if (keyValue.length == 2) {
                    key = keyValue[0].trim();
                    value = keyValue[1].trim();
                } else {
                    System.out.println("Invalid key-value pair: " + line);
                }

                // kappa
                kappa = HashKit.sha1(key);
                String num = value.substring(5);

                // 计算两个根节点
                BigInteger tmp_r0 = new BigInteger(HashKit.sha256(kappa + num + 0), 16);
                BigInteger tmp_r1 = new BigInteger(HashKit.sha256(kappa + num + 1), 16);
                int root_0 = tmp_r0.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();// 关键词对应的根节点的编号
                int root_1 = tmp_r1.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();

                // 计算位置
                BigInteger tmp_0 = new BigInteger(HashKit.md5(kappa + num + 0), 16);
                BigInteger tmp_1 = new BigInteger(HashKit.md5(kappa + num + 1), 16);
                int pos_0 = tmp_0.divideAndRemainder(BigInteger.valueOf((long) Math.pow(2, h)))[1].intValue();
                int pos_1 = tmp_1.divideAndRemainder(BigInteger.valueOf((long) Math.pow(2, h)))[1].intValue();

                // 转化为二进制字符串
                String binary_pos_0 = Integer.toBinaryString(pos_0);
                String binary_pos_1 = Integer.toBinaryString(pos_1);

                // 补齐
                for (int i = binary_pos_0.length(); i < h; i++) {
                    binary_pos_0 = 0 + binary_pos_0;
                }
                for (int i = binary_pos_1.length(); i < h; i++) {
                    binary_pos_1 = 0 + binary_pos_1;
                }

                // 计算最深处
                int height_0 = emptyHeight(roots[root_0], binary_pos_0);
                int height_1 = emptyHeight(roots[root_1], binary_pos_1);

                // 放置
                if (height_0 == -1 && height_1 == -1) {
                    Stash.add(key + "+" + value);
                    break;
                }
                if (height_0 >= height_1) {
                    putNode(roots[root_0], binary_pos_0, height_0, key, value);
                } else {
                    putNode(roots[root_1], binary_pos_1, height_1, key, value);
                }
            }
            reader.close();
            long end = System.nanoTime();
            System.out.println("-----------------------Setup parse time:" + (end - start)/ 1_000_000.0 + "ms-----------------------");

            Time_total += (end - start);

        }
//        System.out.println("Time_total:" + Time_total);
        System.out.println("Time_ave:" + Time_total/cycle_num/ 1_000_000.0 + "ms");
    }
    private static <T> void printPreorder(TreeNode<T> node) {
        if (node == null) {
            return;
        }
        System.out.println(node.getData());
        printPreorder(node.getLeft());
        printPreorder(node.getRight());
    }

    private static int  emptyHeight(TreeNode<String> node, String path){
        int length = -1;
        int index = 0;
        if (Objects.equals(node.getData(), "n")){
            length = index;
        }
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '0'){
                if (Objects.equals(node.getLeft().getData(), "n")){
                    index++;
                    length = index;
                    node = node.getLeft();
                }else {
                    index++;
                    node = node.getLeft();
                }
            } else if (c == '1'){
                if (Objects.equals(node.getRight().getData(), "n")){
                    index++;
                    length = index;
                    node = node.getRight();
                }else {
                    index++;
                    node = node.getRight();
                }
            }
        }
        return length;
    }

    private static void putNode(TreeNode<String> node, String path, int height, String key, String value){
        for (int i = 0; i < height; i++) {
            char c = path.charAt(i);
            if (c == '0'){
                node = node.getLeft();
            }else {
                node = node.getRight();
            }
        }
        node.setData(key + "+" + value);
    }

    private static <T> void serializeTree(TreeNode<T> node, BufferedWriter writer) throws IOException {
        if (node == null) {
            writer.write(""); // 空节点
            writer.newLine();
        } else {
            writer.write(node.getData().toString()); // 节点值
            writer.newLine();
            serializeTree(node.getLeft(), writer); // 递归序列化左子树
            serializeTree(node.getRight(), writer); // 递归序列化右子树
        }
    }

    private static <T> void printTree(TreeNode<T> node) {
        if (node == null) {
            return;
        }
        System.out.println(node.getData());
        printTree(node.getLeft());
        printTree(node.getRight());
    }
}

