import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class KeyValueGenerator {
    public static void main(String[] args) {
//        char model = 'r'; // 'r' for random, 'z' for zipf
        char model = 'z';
        int numPairs = 1024 * 2 * 2 * 2 * 2 * 2; // 键值对的总数
        int numKeys = numPairs/8; // 键的数量
        int maxVolume = 16; // 值的最大数量
        String fileName = "Zipf_15.txt";
        switch (model) {
            case 'r':
                RandomDistribution rd = new RandomDistribution(numPairs, maxVolume);
                // print rd.distribution
                for (int i = 0; i < rd.distribution.size(); i++) {
                    System.out.print(rd.distribution.get(i) + " ");
                }
                System.out.println();
                generateRandomKeyValuePairs(rd.distribution, "DB/" + fileName);
                break;
            case 'z':
                ZipfDistribution zipf = new ZipfDistribution(numPairs, numKeys);
                // print zipf.distribution
                for (int i = 0; i < zipf.distribution.size(); i++) {
                    System.out.print(zipf.distribution.get(i) + " ");
                }
                System.out.println();
                generateRandomKeyValuePairs(zipf.distribution, "DB_zipf/" + fileName);
                break;
            default:
                System.out.println("Invalid model");
        }


    }

    private static void generateRandomKeyValuePairs(ArrayList<Integer> distribution, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            for (int i = 0; i < distribution.size(); i++) {
                String key = "Key" + i;

                for (int j = 0; j < distribution.get(i); j++) {
                    String value = "Value" + j;
                    writer.write(key + "=" + value);
                    writer.write(System.lineSeparator());
                }
            }
            writer.close();
            System.out.println("Random key-value pairs generated and saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to write key-value pairs to " + fileName);
        }
    }
}