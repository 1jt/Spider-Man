import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import Tools.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class UpdateTest_NewDVH {


    //第一步查询，测试后,结果正确
    public static void New_DVH_TestQuery(ArrayList<NodeSet> Position, int size) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        ArrayList<String> Result = new ArrayList<>();

        int Query_num = 100;//查询次数
        int Query_cycle_time = 100;//查询关键字个数
        int avage_num = 0;
        int max_num = 0;
        double runtime= 0;

        for (int Query_num_times = 0; Query_num_times < Query_num; Query_num_times++) {
            int result_num = 0;
            int judge_num = 0;
            for (int times = 0; times < Query_cycle_time; times++) {
                Random rd = new Random();
                int index = rd.nextInt(Position.size()/8); //齐夫数据集 m = n /8
//                int index = 1;
                String query_key = "Key" + index;

                long startTime = System.nanoTime(); // 记录开始时间
                // 需要测试运行时间的代码段区间
                Result = Update_Query_NewDVH.Run(query_key, size, Position);
                //测试运行时间代码段区间
                long endTime = System.nanoTime(); // 记录结束时间
                double executionTime = (endTime - startTime)/1000000.0; // 计算代码段的运行时间（纳秒）
                if (!Result.isEmpty()){
                    runtime += executionTime;
                    result_num += Result.size();
                }else times -- ;

                //输出查到的值，判断是否无损
                double num = 0;
                for (String s : Result) {
                    String[] keyValue = s.split("=");
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    if (key.equals(query_key)){
                        num ++ ;
                    }

                }
                if (num == Result.size()) {
                    judge_num += 1;//记录暴露真实数量的查询
                }

            }

            max_num = Math.max(max_num, judge_num);
            avage_num += result_num / Query_cycle_time;
        }
        System.out.println("平均volume = " + avage_num / Query_num + "，最大暴露真实数量查询个数占百分比" + max_num + "，平均查询一次所用时间（毫秒）" + runtime/10000);

    }

    //测试更新操作
    public static void NewDVH_TestUpdate(ArrayList<NodeSet> Position, int size) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("输入要更新的关键字");
        String index = br.readLine();
        String key = "Key" + index;

        boolean flag = true;
        while (flag) {
            System.out.println("选择执行的更新操作：1删除，2添加，3退出");
            String option = br.readLine();
            //删除操作
            if (option.equals("1")) {
                //先查询
                ArrayList<String> first_result = Update_Query_NewDVH.Run(key, size, Position);
                for (String s : first_result) {
                    System.out.println(s);
                }
                System.out.println("一共返回结果数量：" + first_result.size());
                //删除
                Update_NewDVH.DeleteUpdate(size, key, first_result, Position);

            } else if (option.equals("2")) {//执行添加操作
                //先查询
                ArrayList<String> first_result = Update_Query_NewDVH.Run(key, size, Position);
                for (String s : first_result) {
                    System.out.println(s);
                }
                System.out.println("一共返回结果数量：" + first_result.size());
                //添加
                Update_NewDVH.AddUpdate(size, key, first_result, Position);

            } else {
                flag = false;
            }

        }


    }
}

