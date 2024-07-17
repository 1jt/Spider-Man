import java.io.IOException;
import java.util.*;

public class UpdateTest_NewDVH {
    public static ArrayList<String> Result = new ArrayList<>();
    public static int size;//为了后续传出size
    public static String Query;
    //第一步查询，测试后结果正确
    public static void UpdateTest(ArrayList<NodeSet> Roots, String filepath) throws IOException {

        size = NewDVH_Tool.Size(filepath);
        int Query_num = 1;//查询次数
        int Query_cycle_time = 1;//查询关键字个数
        int avage_num = 0 ;

        for (int Query_num_times = 0; Query_num_times < Query_num; Query_num_times++) {
            int result_num = 0;
            int judge_num = 0;
//        System.out.println("-----------------------Query parse begin-----------------------");
            for (int times = 0; times < Query_cycle_time; times++) {

                Random random = new Random();
//                int index = random.nextInt(size);
                int index = 5 ; //更新赋值

                Query = "Key" + index;

                Result = Update_Query_NewDVH.Run(Query, size, Roots);

                result_num += Result.size();

                //输出查到的值，判断是否无损
                double num = 0;
                for (String s: Result
                ) {
                    String[] keyValue = s.split("\\+");
                    if (Objects.equals(Query, keyValue[0])){
                        System.out.println(Query+"volume"+Result.size()+"value="+keyValue[1]);
                        num += 1;
                    }
                }
                if(num == Result.size()){
                    judge_num += 1;
                }

            }
            System.out.println("平均volume = "+result_num / Query_cycle_time+" 以真实数量返回值有" + judge_num);
            avage_num += result_num/Query_cycle_time;
        }
        System.out.println("平均volume = "+avage_num / Query_num);

    }
}
