import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Tools {
    public static ArrayList<String> PrepareReadFilesForSetup(String Dir) {
        ArrayList<String> filepath_list = new ArrayList<>();
        for (int i = 10; i < 21; i+=2) {
            filepath_list.add(Dir + "/database_shuffle_" + i + ".txt");
        }
        return filepath_list;
    }

    public static ArrayList<String> PrepareReadFilesForQuery(String Dir) {
        ArrayList<String> filepath_list = new ArrayList<>();
        for (int i = 10; i < 21; i+=2) {
            for (int j = 4; j < 9; j++) {
                filepath_list.add(Dir + "/database_shuffle_" + i + "_" + j + ".txt");
            }
        }

        return filepath_list;
    }

    public static int CalculateNumberOfDBEntries(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Stream<String> lines = Files.lines(path);
        int n = (int) lines.count(); // 条目数
        lines.close();

        return n;
    }

    public static Map<String, Integer> CalculateRealVolume(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line_statistics;
        Map<String, Integer> DB = new HashMap<>();
        while ((line_statistics = reader.readLine()) != null) {
            // 读取键值对
            String[] keyValue = line_statistics.split("=");
            String key = null;
            if (keyValue.length == 2) {
                key = keyValue[0].trim();
            } else {
                System.out.println("Invalid key-value pair: " + line_statistics);
            }
            if(DB.get(key) == null){
                DB.put(key, 1);
            }else {
                DB.replace(key, DB.get(key) + 1);
            }
        }
//        for (String i: DB.keySet()
//             ) {
//            System.out.println("key:" + i + " value:" + DB.get(i));
//        }
//        System.out.println(DB.keySet().size());

        return DB;
    }

    // 从文件中读取数据进行反序列化并构建二叉树
    public static <T> TreeNode<T> deserializeTree(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line.isEmpty()) {
            return null; // 空节点
        } else {
            T data = (T) line;
            TreeNode<T> node = new TreeNode<>(data);
            node.setLeft(deserializeTree(reader)); // 递归反序列化左子树
            node.setRight(deserializeTree(reader)); // 递归反序列化右子树
            return node;
        }
    }

    public static void DVH_Deserialize(String filePath, TreeNode<String>[] roots, int size ){
        try (BufferedReader reader_S = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < size; i++) {
                String line = reader_S.readLine();
                if (line.equals("new root")) {
                    line = reader_S.readLine();
                    String data = (String) line;
                    roots[i].setData(data);
                }else {
                    System.out.println("文件读取错误");
                }

                TreeNode<String> root_S = Tools.deserializeTree(reader_S); // 反序列化并构建二叉树
                roots[i].setLeft(root_S);
                if (roots[(i + size - 1) % size].getRight() == null) {
                    roots[(i + size - 1) % size].setRight(root_S);
                }
            }
            reader_S.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chFB_Deserialize(String filePath, TreeNode<String>[] roots, int size){
        try (BufferedReader reader_S = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < size; i++) {
                String line = reader_S.readLine();
//                String data = (String) line;
//                System.out.println(data);

                TreeNode<String> root_S = deserializeTree(reader_S); // 反序列化并构建二叉树
                roots[i] = root_S;
            }
            reader_S.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void chFB_Deserialize_Stash(String filePath, ArrayList<String> Stash){
        String line;
        try (BufferedReader reader_S = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader_S.readLine()) != null) {
                Stash.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double VHDSSE_Deserialize(String filePath, ArrayList<ArrayList<ArrayList<String>>> DB_result){
        double beta;
        String line;
        try (BufferedReader reader_S = new BufferedReader(new FileReader(filePath))) {
            beta = Double.parseDouble(reader_S.readLine());
            int size = Integer.parseInt(reader_S.readLine());
            for (int i = 0; i < size; i++) {
                line = reader_S.readLine();
                if (line.equals("new Table")) {
                    int DB_i_size = Integer.parseInt(reader_S.readLine());
                    ArrayList<ArrayList<String>> DB_i = new ArrayList<ArrayList<String>>();
                    ArrayList<String> T0 = new ArrayList<>();
                    ArrayList<String> T1 = new ArrayList<>();
                    DB_i.add(T0);
                    DB_i.add(T1);
                    for (int j = 0; j < DB_i_size; j++) {
                        T0.add(reader_S.readLine());
                        T1.add(reader_S.readLine());
                    }
                    DB_result.add(DB_i);
                }
                else {
                    System.out.println("读文件时出错了");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return beta;
    }

    public static void VHDSSE_Deserialize_Stash(String filePath,ArrayList<String> Stash){
        String line;
        try (BufferedReader reader_S = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader_S.readLine()) != null) {
                Stash.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static ArrayList<String> Duplicate(ArrayList<String> old){
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(old);
        return new ArrayList<>(hashSet);

    }

    public static int getBytes(ArrayList<String> result){
        int size = 0;
        for (String s : result) {
            size += s.getBytes().length;
        }
        return size;
    }

    public static double CalculationAccuracy(String key, ArrayList<String> Result, Map<String, Integer> DB){
        double num = 0;
        for (String s: Result
             ) {
            String[] keyValue = s.split("\\+");
            if (Objects.equals(key, keyValue[0]))
                num ++;
        }

        return (double) num/DB.get(key);
    }

    // 归一化处理
    public static double[] NormalizeResult(double[] Result){
        double[] Result_num_Normal = new double[Result.length];
        double[] Para = MaxMin(Result);
        double max = Para[0];
        double min = Para[1];
        double divisor = max - min;
        for (int i = 0; i < Result.length; i++) {
            Result_num_Normal[i] = (Result[i] - min)/divisor;
        }

        return Result_num_Normal;

    }
    public static double[] MaxMin(double[] Result){
        double max = Result[0];
        double min = Result[0];
        double[] Para = new double[2];
        for (double i:Result
             ) {
            if (i > max){
                max = i;
                continue;
            }
            if(i < min){
                min = i;
            }
        }
        Para[0] = max;
        Para[1] = min;
        return Para;
    }

    // 打印二叉树（前序遍历）
    public static <T> void printTree(TreeNode<T> node) {
        if (node == null) {
            return;
        }
        System.out.println(node.getData());
        printTree(node.getLeft());
        printTree(node.getRight());
    }

    public static TreeNode<String> FillTree(int height){
        if (height < 0) {
            return null;
        }
        TreeNode<String> node = new TreeNode<String>("n");
        node.setLeft(FillTree(height - 1));
        node.setRight(FillTree(height - 1));
        return node;
    }


}
