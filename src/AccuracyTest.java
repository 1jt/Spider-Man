import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class AccuracyTest {
    public static void Test(String filePath) throws IOException {
        System.out.println("----------" + filePath + " starts VHDSSE AccuracyTest----------");
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

        double[] Result_num_real = new double[DB.keySet().size()];
        double Real_Return = 0;
        double Actual_Quantity =0;

        for (int Query_num = 0; Query_num < DB.keySet().size(); Query_num++) {
            String Query = "Key" + Query_num;
            double Accuracy;
            ArrayList<String> Result;

            Result = Query_VHDSSE.Run(Query,DB_result,Stash,buf,beta);

            Accuracy = Tools.CalculationAccuracy(Query, Tools.Duplicate(Result), DB);
//            System.out.println("Query " + Query + "'s Accuracy is:" + Accuracy * 100 + "%");
            Real_Return += (double) DB.get(Query) * Accuracy;
            Actual_Quantity += (double)DB.get(Query);
            Result_num_real[Query_num] = (double) DB.get(Query) * Accuracy;
        }
//        System.out.println(Arrays.toString(Result_num_real));

        // 计算加权准确率（真实返回/实际数量）
        System.out.println("Weighted Accuracy is:" + Real_Return/Actual_Quantity * 100 + "%");
    }
}
