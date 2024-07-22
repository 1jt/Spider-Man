import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class UpdateTest_NewDVH {


    //第一步查询，测试后,结果正确
    public static void New_DVH_TestQuery(ArrayList<NodeSet> Position, int size) throws IOException {
        String query_key = "";
        ArrayList<String> Result = new ArrayList<>();

        int Query_num = 100;//查询次数
        int Query_cycle_time = 100;//查询关键字个数
        int avage_num = 0;

        for (int Query_num_times = 0; Query_num_times < Query_num; Query_num_times++) {
            int result_num = 0;
            int judge_num = 0;
            for (int times = 0; times < Query_cycle_time; times++) {
                Random rd = new Random();
                int index = rd.nextInt(120); //更新赋值
//                int index = 7;
                query_key = "Key" + index;
                Result = Update_Query_NewDVH.Run(query_key, size, Position);
                result_num += Result.size();
                //输出查到的值，判断是否无损
                double num = 0;
                for (String s : Result
                ) {
                    String[] keyValue = s.split("\\+");
                    if (Objects.equals(query_key, keyValue[0])) {
//                        System.out.println(query_key +  "value=" + keyValue[1]);
                        num += 1;
                    }
                }
                if (num == Result.size()) {
                    judge_num += 1;//记录暴露真实数量的查询
                }

            }
            System.out.println("平均volume = " + result_num / Query_cycle_time + " 暴露真实数量的查询有" + judge_num);
            avage_num += result_num / Query_cycle_time;
        }
        System.out.println("平均volume = " + avage_num / Query_num);

    }

    //测试更新操作
    public static void NewDVH_TestUpdate(ArrayList<NodeSet> Position,int size ) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("输入要更新的关键字");
        String index = br.readLine();
        String key = "Key" + index;
        boolean flag = true;
        while (flag){
            System.out.println("选择执行的更新操作：1删除，2添加，3退出");
            String option = br.readLine();
            //删除操作
            if (option.equals("1")) {
                ArrayList<String> first_result = Update_Query_NewDVH.Run(key,size,Position);
                System.out.println("一共返回结果数量：" + first_result.size());
                //先查询
                for (String s : first_result
                ) {
                    String[] keyValue = s.split("\\+");
                    if (Objects.equals(key, keyValue[0])) {
                        System.out.println(key +  "value=" + keyValue[1]);
                    }
                }
                //删除
                Update_NewDVH.DeleteUpdate(size,key,first_result,Position);

                //测试删除效果
                System.out.println("*******************删除完效果展示***********************");
                ArrayList<String> second_result = Update_Query_NewDVH.Run(key,size,Position);
                System.out.println("一共返回结果数量：" + second_result.size());
                for (String s : second_result
                ) {
                    String[] keyValue = s.split("\\+");
                    if (Objects.equals(key, keyValue[0])) {
                        System.out.println(key +  "value=" + keyValue[1]);
                    }
                }

            }else if (option.equals("2") ){//执行添加操作
                ArrayList<String> first_result = Update_Query_NewDVH.Run(key,size,Position);
                System.out.println("一共返回结果数量：" + first_result.size());
                //先查询
                for (String s : first_result
                ) {
                    String[] keyValue = s.split("\\+");
                    if (Objects.equals(key, keyValue[0])) {
                        System.out.println(key +  "value=" + keyValue[1]);
                    }
                }
                Update_NewDVH.AddUpdate(size,key,first_result,Position);

                //测试添加效果
                System.out.println("*******************添加后效果展示***********************");
                ArrayList<String> second_result = Update_Query_NewDVH.Run(key,size,Position);
                System.out.println("一共返回结果数量：" + second_result.size());
                for (String s : second_result
                ) {
                    String[] keyValue = s.split("\\+");
                    if (Objects.equals(key, keyValue[0])) {
                        System.out.println(key +  "value=" + keyValue[1]);
                    }
                }

            }else  {
                flag = false;
            }

        }


    }
}

