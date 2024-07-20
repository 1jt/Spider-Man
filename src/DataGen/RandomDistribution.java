package DataGen;

import java.util.ArrayList;
import java.util.Random;

public class RandomDistribution {
    private final int numPairs; // total number of elements
    private final int maxVolume;
    public ArrayList<Integer> distribution; //

    public RandomDistribution(int numPairs, int maxVolume) {
        this.numPairs = numPairs;
        this.maxVolume = maxVolume;
        this.distribution = new ArrayList<>();
        GenDistribution();
    }

    // Method to calculate probabilities for each rank
    private void GenDistribution() {
        Random random = new Random();
        distribution.add(maxVolume);
        int remainder = numPairs - maxVolume;
        while(true){
            int numValues = random.nextInt(maxVolume) + 1;
            if (remainder < numValues){
                distribution.add(remainder);
                break;
            } else {
                distribution.add(numValues);
                remainder -= numValues;
            }
        }
    }

    public static void main(String[] args) {

        RandomDistribution rd = new RandomDistribution(1024, 16);
        // print rd.distribution
        for (int i = 0; i < rd.distribution.size(); i++) {
            System.out.print(rd.distribution.get(i) + " ");
        }

        System.out.println();
    }
}