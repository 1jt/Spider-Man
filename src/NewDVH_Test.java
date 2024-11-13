import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

public class NewDVH_Test {
    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String fileStart = "Shuffle/DB_random";//对文件夹下所有文件遍历测试
        ArrayList<String> fileList = GetFileList(fileStart);
        for (String s : fileList) {
            System.out.println("************************" + s + "*********************************");

            //Setup
            long startTime = System.nanoTime(); // 记录开始时间
            // 需要测试运行时间的代码段区间
            Setup_NewDVH.Test(s);
            //测试运行时间代码段区间
            long endTime = System.nanoTime(); // 记录结束时间
            long executionTime = (endTime - startTime) / 1000000; // 计算代码段的运行时间（毫秒）
            System.out.println("setup代码段的运行时间为: " + executionTime + " 毫秒");
            System.out.println("服务器存储开销为KB: " + GetSeverCost(Setup_NewDVH.Position));

            //Query
            int size = NewDVH_Tool.Size(s);
            UpdateTest_NewDVH.New_DVH_TestQuery(Setup_NewDVH.Position, size);//测查询通信开销

            Setup_NewDVH.Position.clear();

            //Update
            //UpdateTest_NewDVH.NewDVH_TestUpdate(Setup_NewDVH.Position,size); //测试更新功能实现
        }

    }


    //遍历该目录下的所有文件
    public static ArrayList<String> GetFileList(String s){
        Path startPath = Paths.get(s); // 替换为你的目录路径
        ArrayList<String> Filelist = new ArrayList<>();
        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Filelist.add(file.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Filelist;
    }
    //计算服务器存储开销
    public static int GetSeverCost(ArrayList<NodeSet> data){
        int datasize = 0;
        for (NodeSet nodeSet : data) {
            datasize += (int)(ObjectSizeCalculator.getObjectSize(nodeSet.getNode().getData()));;
        }
        return datasize;
    }

}
