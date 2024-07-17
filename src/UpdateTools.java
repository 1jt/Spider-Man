import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class UpdateTools {
    public static ArrayList<Integer> UpList(String value , ArrayList<Integer> list){
        Random random = new Random();
        ArrayList<Integer> uplist = new ArrayList<>();
        int num = 0;
        int num_1 = 0;
        int num_0 = 0;
        String val = UpdateTest_NewDVH.Query + "+Value" + value;
        for (String s:UpdateTest_NewDVH.Result){
            num += 1;
            if (Objects.equals(val,s ))
                break;
        }
        for (int i = 1 ; i < num ;i++){
            if(list.get(i) == 1)
                num_1 ++;
            else num_0 ++;
            uplist.add(0);
        }
        //生成随机更新路径
        int Query_root = list.get(0);
        int Update_root ;
        while (true){
            Update_root = random.nextInt(num_1+num_0+1) + Query_root -num_0;
            if (Update_root >= 0 && Update_root <= UpdateTest_NewDVH.size)
                break;
        }
        for (int j = 0 ; j < Query_root + num_1 - Update_root ; j++){
            uplist.set(j,1 );//生成固定个数1
        }
        Collections.shuffle(uplist);//打乱序列
        uplist.add(0,Update_root);
        return uplist;
    }
    public static ArrayList<Integer> AddUpList(String value , ArrayList<Integer> list){
        Random random = new Random();
        ArrayList<Integer> uplist = new ArrayList<>();
        int num = 0;
        int num_1 = 0;
        int num_0 = 0;
        String dummy = UpdateTest_NewDVH.Query+"+ValueDummy";
        boolean next_pos = true;
        //判断是否有假人
        for (String s:UpdateTest_NewDVH.Result){
            num += 1;
            if (Objects.equals(dummy,s)){
                next_pos = false;
                break;
            }
        }
        //遍历pos值序列
        int pos = list.get(list.size() -1);//list最后一个值是找到空节点要用的pos
        list.remove(list.size() -1);//为了找到最后一个数据节点
        for (int i = 1 ; i < num ;i++){
            if(list.get(i) == 1)
                num_1 ++;
            else num_0 ++;
            uplist.add(0);
        }

        //生成随机更新路径
        int Query_root = list.get(0);
        int Update_root ;
        while (true){
            Update_root = random.nextInt(num_1+num_0+1) + Query_root -num_0;
            if (Update_root >= 0 && Update_root <= UpdateTest_NewDVH.size)
                break;
        }
        for (int j = 0 ; j < Query_root + num_1 - Update_root ; j++){
            uplist.set(j,1 );//生成固定个数1
        }
        Collections.shuffle(uplist);//打乱序列
        uplist.add(0,Update_root);
        //无dummy
        if (next_pos){
            uplist.add(pos);
        }

        return uplist;
    }
}
