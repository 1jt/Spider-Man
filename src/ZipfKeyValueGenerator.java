import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ZipfKeyValueGenerator {
    public static void main(String[] args) {
        int totalPairs = 1000; // 键值对的总数
        int numKeys = 250; // 键的数量
        int maxValues = 16; // 值的最大数量
        double s = 1; // parameter s (can be adjusted)
        ZipfDistribution zipf = new ZipfDistribution(s, maxValues);

        String fileName = "ljt.txt";

        generateRandomKeyValuePairs(totalPairs, numKeys, maxValues, fileName, zipf);
    }

    public static void generateRandomKeyValuePairs(int totalPairs, int numKeys, int maxValues, String fileName, ZipfDistribution zipf) {
        try {
            FileWriter writer = new FileWriter(fileName);

            Random random = new Random();

            int numPairs = totalPairs;
//            if (numKeys > 0) {
//                numPairs = Math.min(totalPairs, numKeys * (maxValues > 0 ? maxValues : 1));
//            }
            NUM:for (int num = 0; ;) {
                for (int i = 0; i < numPairs; i++) {
//                    String key = "Key" + (i % numKeys);
                    String key = "Key" + i;
                    int numValues = 0;
                    double rand = random.nextDouble();
                    double cumulativeProbability = 0.0;
                    for (int index = 0; index < maxValues; index++) {
                        cumulativeProbability += zipf.probabilities[index];
                        if (rand <= cumulativeProbability) {
                            numValues = index + 1;
                            // print numValues
                            System.out.println("numValues: " + numValues);
                            break;
                        }
                    }

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