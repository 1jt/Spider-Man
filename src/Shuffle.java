import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shuffle {
    public static void main(String[] args) {
//        char model = 'r'; // 'r' for random, 'z' for zipf
        char model = 'z';
        String fileName = "Zipf_10.txt";
        String in_filePath; // 输入文件
        String out_filePath; // 输出文件
        List<String> lines; // 读取txt文件内容到List中
        switch (model) {
            case 'r':
                in_filePath = "DB_random/" + fileName; // txt文件路径
                out_filePath = "DB_random_shuffle/" + fileName;; // txt文件路径
                lines = readLinesFromFile(in_filePath); // 读取txt文件内容到List中
                Collections.shuffle(lines); // 随机打乱List中的数据
                writeLinesToFile(lines, out_filePath); // 将打乱后的数据写回到txt文件
                System.out.println("数据 " + in_filePath + " 已成功打乱并写回到文件。");
                break;
            case 'z':
                in_filePath = "DB_zipf/" + fileName; // txt文件路径
                out_filePath = "DB_zipf_shuffle/" + fileName;; // txt文件路径
                lines = readLinesFromFile(in_filePath); // 读取txt文件内容到List中
                Collections.shuffle(lines); // 随机打乱List中的数据
                writeLinesToFile(lines, out_filePath); // 将打乱后的数据写回到txt文件
                System.out.println("数据 " + in_filePath + " 已成功打乱并写回到文件。");
                break;
            default:
                System.out.println("Invalid model");
        }
    }

    // 读取txt文件的每行数据到List中
    private static List<String> readLinesFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
        return lines;
    }

    // 将List中的数据写回到txt文件中
    private static void writeLinesToFile(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }
}