public class MathUtil {

    /**
     * 传入一个数列x计算平均值
     *
     * @param x
     * @return 平均值
     */
    public static double average(double[] x) {
        int n = x.length;            //数列元素个数
        double sum = 0;
        for (double i : x) {        //求和
            sum += i;
        }
        return sum / n;
    }

    /**
     * 传入一个数列x计算方差
     * 方差s^2=[（x1-x）^2+（x2-x）^2+......（xn-x）^2]/（n）（x为平均数）
     *
     * @param x 要计算的数列
     * @return 方差
     */
    public static double variance(double[] x) {
        int n = x.length;            //数列元素个数
        double avg = average(x);    //求平均值
        double var = 0;
        for (double i : x) {
            var += (i - avg) * (i - avg);    //（x1-x）^2+（x2-x）^2+......（xn-x）^2
        }
        return var / n;
    }

    /**
     * 传入一个数列x计算标准差
     * 标准差σ=sqrt(s^2)，即标准差=方差的平方根
     *
     * @param x 要计算的数列
     * @return 标准差
     */
    public static double standardDiviation(double[] x) {
        return Math.sqrt(variance(x));
    }
}