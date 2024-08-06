import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class NewDVH_Test {
    public static void main(String[] args) throws IOException {
        String element = "DB_zipf_shuffle/Zipf_10.txt";
        int size = NewDVH_Tool.Size(element);
        Setup_NewDVH.Test(element);

        long startTime = System.nanoTime(); // 记录开始时间
        // 需要测试运行时间的代码段区间

        UpdateTest_NewDVH.New_DVH_TestQuery(Setup_NewDVH.Position,size);//测查询通信开销

        //测试运行时间代码段区间
        long endTime = System.nanoTime(); // 记录结束时间
        long executionTime = (endTime - startTime) / 1000000; // 计算代码段的运行时间（毫秒）

        System.out.println("代码段的运行时间为: " + executionTime + " 毫秒");


//        UpdateTest_NewDVH.NewDVH_TestUpdate(Setup_NewDVH.Position,size); //测试更新功能实现

    }
}
