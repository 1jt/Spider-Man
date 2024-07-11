import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class QueryTest_NewDVH {
    public static void Test(String filePath) throws IOException {
        System.out.println("----------" + filePath + " starts NewDVH_Query calculation----------");

        int Query_num = 20; // 关键词数量
        int Query_cycle_time = 50; // 查询次数
        long Time_Total = 0;
        int Length_total = 0;


        // 计算数据库条目数
        int n = Tools.CalculateNumberOfDBEntries(filePath);
        // 统计数据库对应数目
        Map<String, Integer> DB = Tools.CalculateRealVolume(filePath);
        // 确定输入文件
        String[] FilePara = filePath.split("_");
        filePath = "NewDVH_DB" + "/" + "tree_" + FilePara[2] + "_" + FilePara[3];
//        System.out.println(filePath);
        // 哈希环的slot数
        int size;
        // 常数
        int c = 5;
        // 计算size
        size = (int) Math.ceil(n / ((Math.log(n) / Math.log(2)) * c));
        // 初始化哈希环的根节点们
        TreeNode<String>[] roots = Roots.CreateRoots(size);
        // 反序列化
        Tools.DVH_Deserialize(filePath,roots,size);

        for (int Query_num_times = 0; Query_num_times < Query_num; Query_num_times++) {
            Random random = new Random();
            int index = random.nextInt(DB.keySet().size());

            String Query = "Key" + index;
            long Time_Single = 0;
            int Length_Single = 0;
            double Accuracy;

//        System.out.println("-----------------------Query parse begin-----------------------");
            for (int times = 0; times < Query_cycle_time; times++) {
                ArrayList<String> Result;
                long start_Q = System.nanoTime();

                Result = Query_NewDVH.Run(Query, size, roots);

                long end_Q = System.nanoTime();

                Time_Single += (end_Q - start_Q);
                if (times == 0){
                    Length_Single = Tools.getBytes(Result);
                    Accuracy = Tools.CalculationAccuracy(Query, Tools.Duplicate(Result), DB);
                    if (Accuracy!=1){
                        System.out.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！");
                    }
                }
            }

            Time_Total += (Time_Single / Query_cycle_time);
            Length_total += Length_Single;
//            System.out.println("Accuracy:" + Accuracy);
        }
//        System.out.println("ALL_Time_total:" + Time_Total);
        System.out.println("ALL_Time_Total:" + (Time_Total / Query_num)/ 1_000_000.0 +" ms");
//        System.out.println("ALL_Length_total:" + Length_total);
        System.out.println("ALL_Length_ave:" + Length_total / Query_num);

    }
}
