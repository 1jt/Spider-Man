package dprfMM;
import DataGen.ZipfDistribution;
import Tools.Hash;
import Tools.tool;

import java.util.Arrays;


public class test {
    public static void main(String[] args) {
        ZipfDistribution zipf = new ZipfDistribution(1024, 128);
        // print zipf.distribution
        for (int i = 0; i < zipf.distribution.size(); i++) {
            System.out.print(zipf.distribution.get(i) + " ");
        }

        System.out.println();
        System.out.println("Hello World!");
        System.out.println(Hash.reduce(-5,5));

        long num = -1516514651;
        byte[] current_node = tool.longToBytes(num);
        // 16进制输出 num
        System.out.println(Long.toHexString(num));


        for (int i = 0; i < current_node.length; i++) {
            System.out.print(Integer.toHexString(current_node[i] & 0xff) + " ");
        }
        current_node = Arrays.copyOfRange(current_node, 0, 8);
        System.out.println();
        System.out.println("current_node.length: " + current_node.length);
        for (int i = 0; i < current_node.length; i++) {
            System.out.print(Integer.toHexString(current_node[i] & 0xff) + " ");
        }
        current_node = Hash.Get_SHA_128(current_node);
        System.out.println();
        System.out.println("current_node.length: " + current_node.length);
        for (int i = 0; i < current_node.length; i++) {
            System.out.print(Integer.toHexString(current_node[i] & 0xff) + " ");
        }
    }
}
