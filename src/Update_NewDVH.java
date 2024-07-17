import java.util.ArrayList;
import java.io.*;

public class Update_NewDVH {

    public static ArrayList<NodeSet> Database = new ArrayList<>();

    public static void getdata(ArrayList<NodeSet> Data){
        Database = Data ;
    }
    public static void DeleteUpdate()throws IOException{
        BufferedReader index = new BufferedReader(new InputStreamReader(System.in));
        String value;
        String dummy = "Dummy";
        System.out.println("输入要删除的value值");
        value = index.readLine();
        ArrayList<Integer>uplist = new ArrayList<>();
        ArrayList<Integer>list = new ArrayList<>();
        list = Update_Query_NewDVH.list;

        uplist = UpdateTools.UpList(value,list);
        int x = uplist.get(0), y = UpdateTest_NewDVH.size - 1 - uplist.get(0);//bound就是size
        for (int j = 1 ; j < uplist.size() ; j ++){
            if(uplist.get(j) == 0)
                y ++;
            else x++;
        }
//        MMPoint Nodemap = new MMPoint(x,y);

        for (NodeSet node : Database) {
            if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
                node.getNode().setData(UpdateTest_NewDVH.Query+ "+Value"+ dummy);
            }
        }
        Update_Query_NewDVH.list.clear();
    }

    public static void AddUpdate()throws IOException{
        BufferedReader index = new BufferedReader(new InputStreamReader(System.in));
        String value;
        System.out.println("输入要添加的value值");
        value = index.readLine();
        String Value = UpdateTest_NewDVH.Query+ "+Value"+ value;
        ArrayList<Integer>uplist = new ArrayList<>();
        ArrayList<Integer>list = new ArrayList<>();
        list = Update_Query_NewDVH.list;
        uplist = UpdateTools.AddUpList(value,list);
        if (uplist.size() >= list.size()){
            int x = uplist.get(0), y = UpdateTest_NewDVH.size - 1 - uplist.get(0);//bound就是size
            for (int j = 1 ; j < uplist.size()-1 ; j ++){
                if(uplist.get(j) == 0)
                    y ++;
                else x++;
            }
            for (NodeSet node : Database) {
                if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
                    TreeNode<String> new_node = new TreeNode<>(Value);
                    if (uplist.get(uplist.size()-1) == 0){
                        y ++;
                        node.getNode().setLeft(new_node);
                    }else {
                        x ++;
                        node.getNode().setRight(new_node);
                    }
                    MMPoint new_point = new MMPoint(x,y);
                    new_node.setId(new_point);
                    NodeSet new_nodeset = new NodeSet(new_point,new_node);
                    Database.add(new_nodeset);
                    break;
                }
            }
        }
        else {
            //只需要更改dummy
            int x = uplist.get(0), y = UpdateTest_NewDVH.size - 1 - uplist.get(0);//bound就是size
            for (int j = 1 ; j < uplist.size() ; j ++){
                if(uplist.get(j) == 0)
                    y ++;
                else x++;
            }
            for (NodeSet node : Database) {
                if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
                    node.getNode().setData(Value);
                }
            }
        }
        Update_Query_NewDVH.list.clear();

    }
}
