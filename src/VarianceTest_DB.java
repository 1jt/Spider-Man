import java.io.*;
import java.util.Map;

public class VarianceTest_DB {
    public static void Test(String filePath) throws IOException {
        System.out.println("----------" + filePath + " starts DB AccuracyTest----------");
        // 计算数据库条目数
        int n = Tools.CalculateNumberOfDBEntries(filePath);
        // 统计数据库对应数目
        Map<String, Integer> DB = Tools.CalculateRealVolume(filePath);

        double[] Result_num = new double[DB.keySet().size()];
        for (int i = 0; i < DB.keySet().size(); i++) {
            Result_num[i] = DB.get("Key" + i);
        }


        double[] Result_num_Normal = Tools.NormalizeResult(Result_num);

        double Var = MathUtil.variance(Result_num_Normal);
        System.out.println("variance_Normal:" + String.format("%.5f", Var * 100));
//        for (int i = 0; i < DB.keySet().size(); i++) {
//            System.out.println(DB.get("Key" + i).toString() + "     "+ (int)Result_num[i]);
//        }


        //第一步：设置输出的文件路径
        //如果该目录下不存在该文件，则文件会被创建到指定目录下。如果该目录有同名文件，那么该文件将被覆盖。
        File writeFile = new File("write.csv");

        try{
            //第二步：通过BufferedReader类创建一个使用默认大小输出缓冲区的缓冲字符输出流
            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile,true));

            //第三步：将文档的下一行数据赋值给lineData，并判断是否为空，若不为空则输出
            for(int i=1;i<=DB.keySet().size();i++){
                writeText.newLine();    //换行
                //调用write的方法将字符串写到流中
                writeText.write(DB.get("Key" + (i-1)).toString());
            }

            //使用缓冲区的刷新方法将数据刷到目的地中
            writeText.flush();
            //关闭缓冲区，缓冲区没有调用系统底层资源，真正调用底层资源的是FileWriter对象，缓冲区仅仅是一个提高效率的作用
            //因此，此处的close()方法关闭的是被缓存的流对象
            writeText.close();
        }catch (FileNotFoundException e){
            System.out.println("没有找到指定文件");
        }catch (IOException e){
            System.out.println("文件读写出错");
        }


    }
}
