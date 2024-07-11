import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class VarianceTest_chFB {
    public static void Test(String filePath) throws IOException {
        System.out.println("----------" + filePath + " starts chFB AccuracyTest----------");
        // 计算数据库条目数
        int n = Tools.CalculateNumberOfDBEntries(filePath);
        // 统计数据库对应数目
        Map<String, Integer> DB = Tools.CalculateRealVolume(filePath);
        // 确定输入文件
        String[] FilePara = filePath.split("_");
        filePath = "chFB_DB/" + "tree_" + FilePara[2] + "_" + FilePara[3];

        // 树的数量
        int size;
        // 常数
        int c = 10;
        // 计算size
        size = (int) Math.ceil(n / ((Math.log(n) / Math.log(2)) * c));
        // 计算height
        int h = (int) Math.ceil(Math.log(c * Math.log(n) / Math.log(2)) / Math.log(2));
        // 初始化哈希环的根节点们
        TreeNode<String>[] roots = Roots.CreateRoots(size);
        // 反序列化
        Tools.chFB_Deserialize(filePath, roots, size);
        // 反序列化Stash
        ArrayList<String> Stash = new ArrayList<>();
        filePath = "chFB_DB" + "/" + "Stash_" + FilePara[2] + "_" + FilePara[3];
        Tools.chFB_Deserialize_Stash(filePath, Stash);

        double[] Result_num = new double[DB.keySet().size()];

        for (int Query_num = 0; Query_num < DB.keySet().size(); Query_num++) {
            String Query = "Key" + Query_num;
            double Accuracy;
            ArrayList<String> Result;

            // 计算方差不需要Stash
            Result = Query_chFB.Run(Query, size,roots, new ArrayList<>(),h);

            Accuracy = Tools.CalculationAccuracy(Query, Tools.Duplicate(Result), DB);
            if (Accuracy!=1){
                System.out.println(Query + ":出错啦！！！！！！！！！！！！！！！！！！！！！！！！");
            }
//            System.out.println("Query " + Query + "'s Accuracy is:" + Accuracy * 100 + "%");
            Result_num[Query_num] = (double) Result.size();
        }
        double Var = MathUtil.variance(Result_num);
        System.out.println("variance:" + Var);


    }
}
