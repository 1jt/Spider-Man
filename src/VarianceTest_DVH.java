import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class VarianceTest_DVH {
    public static void Test(String filePath) throws IOException {
        System.out.println("----------" + filePath + " starts DVH AccuracyTest----------");
        // 计算数据库条目数
        int n = Tools.CalculateNumberOfDBEntries(filePath);
        // 统计数据库对应数目
        Map<String, Integer> DB = Tools.CalculateRealVolume(filePath);
        // 确定输入文件
        String[] FilePara = filePath.split("_");
        filePath = "DVH_DB/" + "tree_" + FilePara[2] + "_" + FilePara[3];
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

        double[] Result_num = new double[DB.keySet().size()];


        for (int Query_num = 0; Query_num < DB.keySet().size(); Query_num++) {
            String Query = "Key" + Query_num;
            double Accuracy = 0;
            ArrayList<String> Result = new ArrayList<>();
            long start_Q = System.nanoTime();

            Result = Query_DVH.Run(Query, size, roots);

            long end_Q = System.nanoTime();
//                System.out.println("-----------------------Query time:" + (end_Q - start_Q) + "-----------------------");
//                System.out.println("----------------------------------------------------");
            Accuracy = Tools.CalculationAccuracy(Query, Tools.Duplicate(Result), DB);
            if (Accuracy!=1){
                    System.out.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！");
            }
//            System.out.println("Query " + Query + "'s Accuracy is:" + Accuracy * 100 + "%");
            Result_num[Query_num] = (double) Result.size();
        }

        double[] Result_num_Normal = Tools.NormalizeResult(Result_num);

        double Var = MathUtil.variance(Result_num);
//        System.out.println("variance:" + Var);
        for (int i = 0; i < DB.keySet().size(); i++) {
            System.out.println(DB.get("Key" + i).toString() + "     "+ (int)Result_num[i]);
        }
//        double Var = MathUtil.variance(Result_num_Normal);
//        System.out.println("variance_Normal:" + String.format("%.5f", Var * 100));

//        File writeFile = new File("write.csv");
//
//        try{
//            //第二步：通过BufferedReader类创建一个使用默认大小输出缓冲区的缓冲字符输出流
//            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile,true));
//
//            //第三步：将文档的下一行数据赋值给lineData，并判断是否为空，若不为空则输出
//            for(int i=1;i<=DB.keySet().size();i++){
//                writeText.newLine();    //换行
//                //调用write的方法将字符串写到流中
//                writeText.write("," + (int) Result_num[i - 1]);
//            }
//
//            //使用缓冲区的刷新方法将数据刷到目的地中
//            writeText.flush();
//            //关闭缓冲区，缓冲区没有调用系统底层资源，真正调用底层资源的是FileWriter对象，缓冲区仅仅是一个提高效率的作用
//            //因此，此处的close()方法关闭的是被缓存的流对象
//            writeText.close();
//        }catch (FileNotFoundException e){
//            System.out.println("没有找到指定文件");
//        }catch (IOException e){
//            System.out.println("文件读写出错");
//        }
    }
}
