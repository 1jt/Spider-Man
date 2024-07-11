import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.swing.*;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.math.BigInteger;
import java.util.*;
import java.io.*;
import java.lang.instrument.Instrumentation;
import java.util.stream.Stream;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Setup_DVH {

    public static void Test(String filePath) throws IOException {

        int cycle_num = 1;  // 构建次数
//        System.out.println("----------" + filePath + " starts Setup calculation----------");
        for (int test_num = 0; test_num < cycle_num; test_num++) {
            int size;
            int c = 5;
            int n = Tools.CalculateNumberOfDBEntries(filePath);
            size = (int) Math.ceil( n / ( (Math.log(n) / Math.log(2)) * c));
            TreeNode<String>[] roots = Roots.CreateRoots(size);
            Map<String, MMPoint> MM_cach = new HashMap<String, MMPoint>();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            String key = null; // 每次读取的关键词
            String value = null; // 每次读取关键词对应的值
            String kappa; // 每个关键词生成的密钥
            int root; // 关键词对应的根节点的编号

            // Setup
//            System.out.println("-----------------------Setup parse begin-----------------------");
            while ((line = reader.readLine()) != null) {
                // 读取键值对
                String[] keyValue = line.split("=");
                if (keyValue.length == 2) {
                    key = keyValue[0].trim();
                    value = keyValue[1].trim();
                } else {
                    System.out.println("Invalid key-value pair: " + line);
                }

                // MM_cach
                if(MM_cach.get(key) == null){
                    MM_cach.put(key,new MMPoint(0,0));
                }

                // 计算kappa
                kappa = HashKit.sha1(key + MM_cach.get(key).getX());

                // 计算root
                BigInteger tmp= new BigInteger(kappa, 16);
                root = tmp.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();

                // 如果对应根节点没有初始化，则执行初始化操作，并初始化第二层节点
                if (roots[root].getData() == null){
                    roots[root].setData(key + "+" + value);
                    TreeNode<String> node_left = new TreeNode<String>(null);
                    TreeNode<String> node_right = new TreeNode<String>(null);
                    if (roots[root].getLeft() == null) {
                        roots[root].setLeft(node_left);
                    }
                    if (roots[root].getRight() == null) {
                        roots[root].setRight(node_right);
                    }
                    if (roots[(root + 1) % size].getLeft() == null) {
                        roots[(root + 1) % size].setLeft(node_right);
                    }
                    if (roots[(root + size -1) % size].getRight() == null) {
                        roots[(root + size -1) % size].setRight(node_left);
                    }
                    continue;
                }


                // 如果不踢
                TreeNode<String> node_tmp = roots[root];
                int count = 0; // 关键词所在层数
                boolean flag = false; // 放入成功指示符
                String input = key + "+" + value;
//                long test1 = System.nanoTime();
                while (!flag){
                    String Pos = HashKit.sha384(kappa + root + count++);
                    BigInteger tmp_2 = new BigInteger(Pos, 16);
                    int pos = tmp_2.divideAndRemainder(BigInteger.valueOf(2))[1].intValue(); // 计算往左还是往右
                    if (pos == 0){
                        if (node_tmp.getLeft()!=null){
                            node_tmp = node_tmp.getLeft();
                            if(node_tmp.getData() == null){
                                node_tmp.setData(input);
                                flag = true;
                            }
                            continue;
                        }
                        else {
                            TreeNode<String> node_left = new TreeNode<String>(input);
                            node_tmp.setLeft(node_left);
                            flag = true;

                        }
                    }
                    //  往右
                    else if (pos == 1){
                        if (node_tmp.getRight()!=null){
                            node_tmp = node_tmp.getRight();
                            if(node_tmp.getData() == null){
                                node_tmp.setData(input);
                                flag = true;
                            }
                            continue;
                        }
                        else {
                            TreeNode<String> node_right = new TreeNode<String>(input);
                            node_tmp.setRight(node_right);
                            flag = true;
                        }
                    }
                }
//                long test2 = System.nanoTime();
//                System.out.println("-----------------------这步用了:" + (test2 - test1) +"-----------------------");
//                Time_total += (test2 - test1);

            }

//            System.out.println("-----------------------Setup parse time:" + (end - start)/ 1_000_000.0 +" ms-----------------------");


            // 序列化存储
            if (test_num == 0) {
                String[] FilePara = filePath.split("_");
                filePath = "DVH_DB/" + "tree_" + FilePara[2] + "_" + FilePara[3];
                try (BufferedWriter writer_S = new BufferedWriter(new FileWriter(filePath))) {
                    for (int i = 0; i < size; i++) {
                        writer_S.write("new root"); // 节点值
                        writer_S.newLine();
                        if (roots[i].getData() != null) {
                            writer_S.write(roots[i].getData().toString()); // 节点值
                        } else {
                            writer_S.write("null");
                        }

                        writer_S.newLine();
                        serializeTree(roots[i].getLeft(), writer_S); // 序列化并写入文件
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            reader.close();
        }
//        System.out.println("Time_total:" + Time_total);
//        System.out.println("Time_ave:" + Time_total/cycle_num/ 1_000_000.0 + "ms");
        System.out.println("----------" + filePath + " DVH_Setup has done----------");

    }

    private static <T> void serializeTree(TreeNode<T> node, BufferedWriter writer) throws IOException {
        if (node == null) {
            writer.write(""); // 空节点
            writer.newLine();
        } else {
            if (node.getData()!=null){
                writer.write(node.getData().toString()); // 节点值
            }else {
                writer.write("null");
            }

            writer.newLine();
            serializeTree(node.getLeft(), writer); // 递归序列化左子树
            serializeTree(node.getRight(), writer); // 递归序列化右子树
        }
    }
}