import java.io.IOException;
import java.util.ArrayList;

public class NewDVH_Test {
    public static void main(String[] args) throws IOException {
        String element = "DB_zipf_shuffle/Zipf_10.txt";
        ArrayList<NodeSet> database = new ArrayList<>();
        int size = NewDVH_Tool.Size(element);
        Setup_NewDVH.Test(element);
//        UpdateTest_NewDVH.New_DVH_TestQuery(Setup_NewDVH.Position,size);//测查询通信开销


        UpdateTest_NewDVH.NewDVH_TestUpdate(Setup_NewDVH.Position,size); //测试更新功能实现





    }
}
