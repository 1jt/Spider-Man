package DataGen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ZipfDistribution {
    private final int numPairs; // total number of elements
    private final int numKeys; // parameter s
    private final double s = 1; // parameter s
    //较大 s 值会导致高排名的元素具有更高的概率，而较小的 s 值则会导致更加均匀或扁平的分布形状。
    //当 s→1 时，Zipf 分布逐渐接近对数分布，即高排名元素的概率与其排名的对数成反比关系。
    // s→∞ 时，分布趋向于均匀分布，即所有元素的概率趋向于相等。
    public double[] probabilities; // probabilities array for each rank
    public ArrayList<Integer> distribution;

    public ZipfDistribution(int numPairs,int numKeys) {
        this.numPairs = numPairs;
        this.numKeys = numKeys;
        this.probabilities = new double[numKeys];
        this.distribution = new ArrayList<>();
        calculateProbabilities();
        GenDistribution();
    }

    // Method to calculate probabilities for each rank
    private void calculateProbabilities() {
        double denominator = 0.0;
        for (int i = 1; i <= numKeys; i++) {
            denominator += 1.0 / Math.pow(i, s);
        }
        for (int i = 0; i < numKeys; i++) {
            probabilities[i] = (1.0 / Math.pow(i + 1, s)) / denominator;
        }
    }

    // Method to generate a random rank based on Zipf distribution
    public int randomRank(Random random) {
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < numKeys; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i + 1; // return rank (1-based index)
            }
        }
        // This should theoretically never happen
        throw new IllegalStateException("Unexpected state: No rank selected.");
    }
    private void GenDistribution() {
        Random random = new Random();
        int[] statistics = new int[numKeys];
        Arrays.fill(statistics, 0);
        for (int i = 0; i < numPairs; i++) {
            int numValues = randomRank(random);
            statistics[numValues-1]++;
        }
        // print statistics
        for (int statistic : statistics) {
            if (statistic != 0) // 去掉数量为 0 的项
                distribution.add(statistic);
        }
    }

    public static void main(String[] args) {

        ZipfDistribution zipf = new ZipfDistribution(1024, 128);
        // print zipf.distribution
        for (int i = 0; i < zipf.distribution.size(); i++) {
            System.out.print(zipf.distribution.get(i) + " ");
        }

        System.out.println();
    }
}