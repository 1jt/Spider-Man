import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class QueryTest_VHDSSE {
    public static void Test(String filePath) throws IOException {
        System.out.println("----------" + filePath + " starts VHDSSE_Query calculation----------");

        int Query_num = 20;// 关键词数量
        int Query_cycle_time = 50;// 查询次数
        long Time_Total = 0;
        int Length_total = 0;

        // 计算数据库条目数
        int n = Tools.CalculateNumberOfDBEntries(filePath);
        // 统计数据库对应数目
        Map<String, Integer> DB = Tools.CalculateRealVolume(filePath);
        // 确定输入文件
        String[] FilePara = filePath.split("_");
        filePath = "VHDSSE_DB" + "/" + "VH_DB_" + FilePara[2] + "_" + FilePara[3];
        // beta
        double beta;

        // 计算beta和反序列化
        ArrayList<ArrayList<ArrayList<String>>> DB_result = new ArrayList<ArrayList<ArrayList<String>>>();
        beta = Tools.VHDSSE_Deserialize(filePath,DB_result);
        // 反序列化Stash
        ArrayList<String> Stash = new ArrayList<>();
        filePath = "VHDSSE_DB" + "/" + "Stash_" + FilePara[2] + "_" + FilePara[3];
        Tools.VHDSSE_Deserialize_Stash(filePath, Stash);
        // 反序列化buf
        ArrayList<String> buf = new ArrayList<>();
        filePath = "VHDSSE_DB" + "/" + "buf_" + FilePara[2] + "_" + FilePara[3];
        Tools.VHDSSE_Deserialize_Stash(filePath, buf);

        for (int Query_num_times = 0; Query_num_times < Query_num; Query_num_times++) {
            Random random = new Random();
            int index = random.nextInt(DB.keySet().size());

            String Query = "Key" + index;
            long Time_Single = 0;
            int Length_Single = 0;
            double Accuracy;

//            System.out.println("-----------------------Query parse begin-----------------------");
            for (int times = 0; times < Query_cycle_time; times++) {
                long start_Q = System.nanoTime();
                ArrayList<String> Result = new ArrayList<>();
                Result = Query_VHDSSE.Run(Query,DB_result,Stash,buf,beta);

                long end_Q = System.nanoTime();
                Time_Single += (end_Q - start_Q);
                if (times == 0){
                    Length_Single = Tools.getBytes(Result);
                    Accuracy = Tools.CalculationAccuracy(Query, Tools.Duplicate(Result), DB);
                    if (Accuracy!=1){
                        System.out.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！");
                    }
                }
//            System.out.println("-----------------------Query parse time:" + (end_Q - start_Q) +"-----------------------");
            }
//            System.out.println("Time_Single:" + Time_Single);
//            System.out.println("Time_ave:" + Time_Single / Query_cycle_time);
//            System.out.println("Length_Single:" + Length_Single);
            Time_Total += (Time_Single / Query_cycle_time);
            Length_total += Length_Single;
        }
//            System.out.println("------------------------------------------------------");
//        System.out.println("ALL_Time_total:" + Time_Total);
        System.out.println("ALL_Time_ave:" + Time_Total / Query_num/ 1_000_000.0 +" ms");
//        System.out.println("ALL_Length_total:" + Length_total);
        System.out.println("ALL_Length_ave:" + Length_total / Query_num);

    }
}
