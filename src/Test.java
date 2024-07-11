import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws IOException {

         //测Setup
//        ArrayList<String> filepath_list_Setup = Tools.PrepareReadFilesForSetup("DB");
//        for (String element:filepath_list_Setup
//        ) {
//            SetupTest_DVH.Test((element));
//            System.out.println("---------------------------------------------------------");
//            SetupTest_chFB.Test(element);
//            System.out.println("---------------------------------------------------------");
//            SetupTest_VHDSSE.Test(element);
//            System.out.println("**************************************************************************************************");
//
//        }

        // 测Query前先生成一下
        ArrayList<String> filepath_list = Tools.PrepareReadFilesForQuery("DB");
        for (String element:filepath_list
             ) {
            if (!element.equals("DB/database_shuffle_10_5.txt"))
                continue;
            Setup_DVH.Test(element);
//            Setup_chFB.Test(element);
//            Setup_VHDSSE.Test(element);
//            Setup_NewDVH.Test(element);
//            break;
        }

//        for (String element:filepath_list
//             ) {
//            if (!element.equals("DB/database_shuffle_20_4.txt"))
//                continue;
//            System.out.println("************************************************************************************************");
//            QueryTest_DVH.Test(element);
//            QueryTest_chFB.Test(element);
//            QueryTest_VHDSSE.Test(element);
//            QueryTest_NewDVH.Test(element);

//            VarianceTest_DVH.Test(element);
//            VarianceTest_chFB.Test(element);
//            VarianceTest_VHDSSE.Test(element);
//            VarianceTest_NewDVH.Test(element);
//            VarianceTest_DB.Test(element);
//            AccuracyTest.Test(element);
//            break;
//        }



    }
}

