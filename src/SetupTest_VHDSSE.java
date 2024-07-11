import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SetupTest_VHDSSE {
    public static void Test(String filePath) throws IOException {
        long Time_total = 0;
        int cycle_num = 10;  // 构建次数
        System.out.println("----------" + filePath + " starts VHDSSE_Setup calculation----------");
        for (int test_num = 0; test_num < cycle_num; test_num++) {
            // 计算数据库条目数
            int N = Tools.CalculateNumberOfDBEntries(filePath); // 条目数
            // 计算min
            int min = 0;
            double v = Math.log(N) / Math.log(2);
            for (int i = 0; i < 100; i++) {
                if (Math.pow(2, i) > v) {
                    min = i;
                    break;
                }
            }

            // 计算beta
            double beta = (double) 16 / N;

            // N的二进制表达
            String binary_N = Integer.toBinaryString(N);

            // Setup
            long start = System.nanoTime();
            int idx = 1;
            ArrayList<ArrayList<String>> DB = new ArrayList<ArrayList<String>>();
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));// 读取文件
            for (int i = (int) Math.floor(v); i >= min; i--) {
                ArrayList<String> DB_i = new ArrayList<>();
                if (binary_N.charAt(binary_N.length() - 1 - i) == '0') {
                    DB.add(new ArrayList<String>(DB_i));
                    continue;
                }

                for (int j = 0; j < Math.pow(2, i); j++) {
                    if ((line = reader.readLine()) == null) {
                        break;
                    }
                    String[] keyValue = line.split("=");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        DB_i.add(key + "+" + value);
                    } else {
                        System.out.println("Invalid key-value pair: " + line);
                    }
                    idx++;
                }
                DB.add(new ArrayList<String>(DB_i));
            }
//            System.out.println("DB:" + DB);
            ArrayList<String> buf = new ArrayList<>();
            for (int i = idx; i <= N; i++) {
                if ((line = reader.readLine()) == null) {
                    break;
                }
                buf.add(line);
            }
//            System.out.println("buf:" + buf);

            ArrayList<String> Stash = new ArrayList<>();
            ArrayList<ArrayList<ArrayList<String>>> DB_result = new ArrayList<ArrayList<ArrayList<String>>>();
            for (int i = 0; i < DB.size(); i++) {
                ArrayList<ArrayList<String>> DB_Setup = new ArrayList<ArrayList<String>>();
                DB_Setup = dprfMM.Setup(DB.get(i), Stash, String.valueOf(i));
                DB_result.add(DB_Setup);
//                System.out.println("T0:" + DB_result.get(i).get(0));
//                System.out.println("T1:" + DB_result.get(i).get(1));
//                System.out.println("Stash:" + Stash);
            }
            long end = System.nanoTime();
            System.out.println("-----------------------Setup parse time:" + (end - start)/ 1_000_000.0 +"ms-----------------------");

            Time_total += (end - start);

            // 存储
//            String filePath = "VH_DB_10_4.txt";
//            try (BufferedWriter writer_S = new BufferedWriter(new FileWriter(filePath))) {
//                writer_S.write(String.valueOf(beta));
//                writer_S.newLine();
//                writer_S.write(String.valueOf(DB_result.size()));
//                writer_S.newLine();
//                for (int i = 0; i < DB_result.size(); i++) {
//                    writer_S.write("new Table"); // 节点值
//                    writer_S.newLine();
//                    serializeTree(DB_result.get(i), writer_S); // 序列化并写入文件
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            filePath = "Stash_10_4.txt";
//            try (BufferedWriter writer_S = new BufferedWriter(new FileWriter(filePath))) {
//                for (int i = 0; i < Stash.size(); i++) {
//                    writer_S.write(Stash.get(i)); // 节点值
//                    writer_S.newLine();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            reader.close();
        }
//        System.out.println("Time_total:" + Time_total);
        System.out.println("Time_ave:" + Time_total/cycle_num/ 1_000_000.0 + "ms");
    }

}