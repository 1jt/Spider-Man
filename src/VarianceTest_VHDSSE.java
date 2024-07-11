import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class VarianceTest_VHDSSE {
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

        double[] Result_num = new double[DB.keySet().size()];

        for (int Query_num = 0; Query_num < DB.keySet().size(); Query_num++) {
            String Query = "Key" + Query_num;
            ArrayList<String> Result;
            // 计算方差不需要考虑Stas和buf
            Result = Query_VHDSSE.Run(Query,DB_result,new ArrayList<>(),new ArrayList<>(),beta);
            Result_num[Query_num] = (double) Result.size();
        }
        double Var = MathUtil.variance(Result_num);
        System.out.println("variance:" + Var);


    }
}
