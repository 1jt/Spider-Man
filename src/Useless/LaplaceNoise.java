package Useless;

import java.math.BigInteger;
import java.util.Random;


public class LaplaceNoise {

    public static double addLaplaceNoise(double input, double b, int x) {
        Random random = new Random(x); // 使用 x 作为种子值
        double u = random.nextDouble() * b - (b / 2); // 生成均匀分布的随机数
        double noise = -b * Math.signum(u) * Math.log(2 * b * Math.abs(u)); // 计算拉普拉斯噪声
        System.out.println("噪声大小" + noise);
        return input + noise;
    }
    private static double getlLamda() {
        double lamda = 256;//lamda大小
        double l_lamda = 2.5 * lamda * Math.log(lamda) / Math.log(2);//参数选择2.5倍
        return l_lamda;
    }

    public static void main(String[] args) {
//        double input = 10.0; // 输入的数字
//        double scale = 0.2;//安全参数
//        double l_lamda = getlLamda();
//        double b = 2 / scale;
//        String key = "key399";
//        BigInteger temp = new BigInteger(HashKit.sha256(key), 16);
//        int x = temp.intValue();// 输入变量 x，指用key生成的随机序列
//        double noisyInput2 = addLaplaceNoise(input, b, x); // 使用 x 生成噪声的结果
//        int result = (int) (noisyInput2+l_lamda);
//
//        System.out.println("输入数字: " + input);
//        System.out.println("输入关键字 key: " + key);
//        System.out.println("计算lamda大小" + l_lamda);
//        System.out.println("添加与key 相关的拉普拉斯噪声后的结果: " + result);
    }

}
