import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import Tools.*;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

public class NewDVH_Test {
    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String element = "DB_zipf_shuffle/Zipf_15.txt";
        String elementSer = "DB_random/Random_9_4.ser";
        String elemenTxt = "DB/database_shuffle_16_6.txt";
        int datasize = 0;
        int size = NewDVH_Tool.Size(elemenTxt);


        long startTime = System.nanoTime(); // 记录开始时间
        // 需要测试运行时间的代码段区间

//        UpdateTest_NewDVH.New_DVH_TestQuery(Setup_NewDVH.Position,size);//测查询通信开销
        Setup_NewDVH.Test(elemenTxt);

        ArrayList<NodeSet> database = new ArrayList<NodeSet>(Setup_NewDVH.Position);

        for (int i = 0; i < database.size(); i++) {

            datasize = (int) (datasize + ObjectSizeCalculator.getObjectSize(database.get(i)));
        }

        System.out.println(datasize);


        //测试运行时间代码段区间
        long endTime = System.nanoTime(); // 记录结束时间
        long executionTime = (endTime - startTime) / 1000000; // 计算代码段的运行时间（毫秒）

        System.out.println("代码段的运行时间为: " + executionTime + " 毫秒");


//        UpdateTest_NewDVH.NewDVH_TestUpdate(Setup_NewDVH.Position,size); //测试更新功能实现

    }
}
