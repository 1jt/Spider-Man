package dpMM;
import java.util.Random;

public class Laplace {
    static double sensitivity = 2;
    static double epsilon = 0.2;

    // 自定义参数生成拉普拉斯噪声
    public static double getNoise(double param)	{
        Random random = new Random();
        double randomDouble = random.nextDouble()-0.5;
        // Laplace分布的概率密度函数： X=\mu - b * sgn(U) * ln(1-2|U|)}
        // https://zh.wikipedia.org/wiki/%E6%8B%89%E6%99%AE%E6%8B%89%E6%96%AF%E5%88%86%E5%B8%83
        return - (param)*Math.signum(randomDouble)*Math.log(1-2*Math.abs(randomDouble));
    }

    public static double getNoise()	{
        Random random = new Random();
        double randomDouble = random.nextDouble()-0.5;

        return - (sensitivity/epsilon)*Math.signum(randomDouble)*Math.log(1-2*Math.abs(randomDouble));
    }

}