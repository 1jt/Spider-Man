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

        int Query_num = 1;//查询次数
        int Query_cycle_time = 1;//查询关键字个数
        int avage_num = 0;
        int max_num = 0;

        for (int Query_num_times = 0; Query_num_times < Query_num; Query_num_times++) {
            int result_num = 0;
            int judge_num = 0;

            for (int times = 0; times < Query_cycle_time; times++) {
                Random rd = new Random();
//                int index = rd.nextInt(120); //更新赋值
                int index = 100;
                String query_key = "Key" + index;

                Result = Update_Query_NewDVH.Run(query_key, size, Position);
                result_num += Result.size();
                //输出查到的值，判断是否无损
                double num = 0;
                for (String s : Result
                ) {

                    System.out.println(s);
                }
//                if (num == Result.size()) {
//                    judge_num += 1;//记录暴露真实数量的查询
//                }

            }
            max_num = Math.max(max_num,judge_num);
//            System.out.println("平均volume = " + result_num / Query_cycle_time + " 暴露真实数量的查询有" + judge_num);
            avage_num += result_num / Query_cycle_time;
        }
        System.out.println("平均volume = " + avage_num / Query_num + "，最大暴露真实数量查询个数为"+ max_num);

    }

    //测试更新操作
    public static void NewDVH_TestUpdate(ArrayList<NodeSet> Position,int size ) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("输入要更新的关键字");
        String index = br.readLine();
        String key = "Key" + index;
        byte[] enkey = Hash.Get_SHA_256(key.getBytes("ISO-8859-1"));//计算解密秘钥
        ArrayList<String> decresult = new ArrayList<>();

        boolean flag = true;
        while (flag){
            System.out.println("选择执行的更新操作：1删除，2添加，3退出");
            String option = br.readLine();
            //删除操作
            if (option.equals("1")) {
                ArrayList<String> first_result = Update_Query_NewDVH.Run(key,size,Position);

                System.out.println("一共返回结果数量：" + first_result.size());
                //先查询
                for (String s : first_result//解密过程
                ) {
                    byte[] en = s.getBytes("ISO-8859-1");
                    byte[] decrypted = AESUtil.decrypt(enkey, en);
                    String s2 = new String(decrypted,"ISO-8859-1");
                    System.out.println(s2);
                    decresult.add(s2);
                }
                //删除
                Update_NewDVH.DeleteUpdate(size,key,decresult,Position);

            }else if (option.equals("2") ){//执行添加操作
                ArrayList<String> first_result = Update_Query_NewDVH.Run(key,size,Position);
                System.out.println("一共返回结果数量：" + first_result.size());
                //先查询
                for (String s : first_result//解密过程
                ) {
                    byte[] en = s.getBytes("ISO-8859-1");
                    byte[] decrypted = AESUtil.decrypt(enkey, en);
                    String s2 = new String(decrypted,"ISO-8859-1");
                    System.out.println(s2);
                    decresult.add(s2);
                }
                Update_NewDVH.AddUpdate(size,key,decresult,Position);

            }else  {
                flag = false;
            }

        }


    }
}

