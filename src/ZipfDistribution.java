import java.util.Random;

public class ZipfDistribution {
    private double s; // parameter s
    private int N; // total number of elements
    public double[] probabilities; // probabilities array for each rank

    public ZipfDistribution(double s, int N) {
        this.s = s;
        this.N = N;
        this.probabilities = new double[N];
        calculateProbabilities();
    }

    // Method to calculate probabilities for each rank
    private void calculateProbabilities() {
        double denominator = 0.0;
        for (int i = 1; i <= N; i++) {
            denominator += 1.0 / Math.pow(i, s);
        }
        for (int i = 0; i < N; i++) {
            probabilities[i] = (1.0 / Math.pow(i + 1, s)) / denominator;
        }
    }

    // Method to generate a random rank based on Zipf distribution
    public int randomRank(Random random) {
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < N; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i + 1; // return rank (1-based index)
            }
        }
        // This should theoretically never happen
        throw new IllegalStateException("Unexpected state: No rank selected.");
    }

    public static void main(String[] args) {
        double s = 1; // parameter s (can be adjusted)
        //较大 s 值会导致高排名的元素具有更高的概率，而较小的 s 值则会导致更加均匀或扁平的分布形状。
        //当 s→1 时，Zipf 分布逐渐接近对数分布，即高排名元素的概率与其排名的对数成反比关系。
        // s→∞ 时，分布趋向于均匀分布，即所有元素的概率趋向于相等。
        int N = 100; // total number of elements (can be adjusted)
        ZipfDistribution zipf = new ZipfDistribution(s, N);
        Random random = new Random();

        // Generate random ranks and print them
        int num_1 = 0;
        int num_2 = 0;
        int num_3 = 0;
        int num_4 = 0;
        for (int i = 0; i < 1000; i++) { // generate 20 random numbers
            int rank = zipf.randomRank(random);
            if(rank==1)
                num_1++;
            else if(rank==2)
                num_2++;
            else if(rank==3)
                num_3++;
            System.out.print(rank + " ");
        }
        // print num_1 and num_2
        System.out.println();
        System.out.println(num_1);
        System.out.println(num_2);
        System.out.println(num_3);

        System.out.println();
    }
}