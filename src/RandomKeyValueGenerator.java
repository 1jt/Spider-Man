import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomKeyValueGenerator {
    public static void main(String[] args) {
        int totalPairs = 100; // 键值对的总数
        int numKeys = 20; // 键的数量
        int maxValues = 16; // 值的最大数量

        String fileName = "ljt.txt";

        generateRandomKeyValuePairs(totalPairs, numKeys, maxValues, fileName);
    }

    public static void generateRandomKeyValuePairs(int totalPairs, int numKeys, int maxValues, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);

            Random random = new Random();

            int numPairs = totalPairs;
            if (numKeys > 0) {
                numPairs = Math.min(totalPairs, numKeys * (maxValues > 0 ? maxValues : 1));
            }
            NUM:for (int num = 0; ;) {
                for (int i = 0; i < numPairs; i++) {
//                    String key = "Key" + (i % numKeys);
                    String key = "Key" + i;
                    int numValues = random.nextInt(maxValues) + 1;

                    for (int j = 0; j < numValues; j++) {
                        String value = "Value" + j;
                        writer.write(key + "=" + value);
                        writer.write(System.lineSeparator());
                        num ++;
                        if (num >= numPairs)
                            break NUM;
                    }
                }
            }

            writer.close();
            System.out.println("Random key-value pairs generated and saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to write key-value pairs to " + fileName);
            e.printStackTrace();
        }
    }
}