import java.io.IOException;
import java.net.BindException;
import java.util.Map;
import java.util.Random;

public class NewDVH_Tool {
    public static int Size(String filePath)throws IOException {
        //计算size大小
        int size ;
        int n = Tools.CalculateNumberOfDBEntries(filePath);
        double c = 1;
        size = (int) (c * n /16) ;
        return size;
    }

    public static int M(String filePath)throws IOException{
        Map<String, Integer> DB = Tools.CalculateRealVolume(filePath);
        Random random = new Random();
        int index = random.nextInt(DB.keySet().size());
        return index;
    }

}
