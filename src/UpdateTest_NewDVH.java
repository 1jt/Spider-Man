import java.io.IOException;
import java.util.*;

public class UpdateTest_NewDVH {


    //第一步查询，测试后,结果正确
    public static void UpdateTest(ArrayList<NodeSet> Position, String filepath) throws IOException {

        int size = NewDVH_Tool.Size(filepath);
        String query_key = "";
        ArrayList<String> Result = new ArrayList<>();

        int Query_num = 1;//查询次数
        int Query_cycle_time = 1;//查询关键字个数
        int avage_num = 0;

        for (int Query_num_times = 0; Query_num_times < Query_num; Query_num_times++) {
            int result_num = 0;
            int judge_num = 0;
            for (int times = 0; times < Query_cycle_time; times++) {
                int index = 5; //更新赋值
                query_key = "Key" + index;
                Result = Update_Query_NewDVH.Run(query_key, size, Position);
                result_num += Result.size();
                //输出查到的值，判断是否无损
                double num = 0;
                for (String s : Result
                ) {
                    String[] keyValue = s.split("\\+");
                    if (Objects.equals(query_key, keyValue[0])) {
                        System.out.println(query_key + "volume" + Result.size() + "value=" + keyValue[1]);
                        num += 1;
                    }
                }
                if (num == Result.size()) {
                    judge_num += 1;
                }

            }
            System.out.println("平均volume = " + result_num / Query_cycle_time + " 以真实数量返回值有" + judge_num);
            avage_num += result_num / Query_cycle_time;
        }
        System.out.println("平均volume = " + avage_num / Query_num);

    }
}
