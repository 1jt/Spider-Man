import java.util.ArrayList;
import java.io.*;

public class Update_NewDVH {

    public static void DeleteUpdate(int size, String key, ArrayList<String> query_result, ArrayList<NodeSet> database) throws IOException {

        //从键盘读取要删除的值
        BufferedReader index = new BufferedReader(new InputStreamReader(System.in));
        String dummy = "Dummy";
        System.out.println("输入要删除的value值");
        String value = index.readLine();

        //通过查询获得的查询pos序列，生成更新pos序列
        ArrayList<Integer> query_list = Update_Query_NewDVH.list;
        ArrayList<Integer> update_list = NewDVH_Tool.UpList(value, key, query_list, query_result, size);


        //找到更新路径对应根节点，通过pos序列计算（x,y）坐标，直接找到目标节点
        int x = update_list.get(0), y = size - 1 - update_list.get(0);
        for (int j = 1; j < update_list.size(); j++) {
            if (update_list.get(j) == 0)
                y++;
            else x++;
        }

        //在节点库中找到目标节点后，执行删除
        for (NodeSet node : database) {
            if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
                node.getNode().setData(key + "+Value" + dummy);
            }
        }

        //清空更新序列，为下次更新做准备
        Update_Query_NewDVH.list.clear();

    }

    public static ArrayList<NodeSet> AddUpdate(int size, String key, ArrayList<String> query_result, ArrayList<NodeSet> database) throws IOException {

        //从键盘读取要添加的值，形式为 Key** + Value**
        BufferedReader index = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("输入要添加的value值");
        String value = index.readLine();
        String data = key + "+Value" + value;//因为更新之前对目标进行过查询，知道key


//        long startTime = System.nanoTime(); // 记录开始时间
        // 需要测试运行时间的代码段区间

        //通过查询获得的查询pos序列，生成更新pos序列
        ArrayList<Integer> query_list = Update_Query_NewDVH.list;
        ArrayList<Integer> update_list = NewDVH_Tool.AddUpList(value, key, query_list, query_result, size);

        //当更新队列长度>=查询队列长度时，表示不存在dummy数据，添加需要重新创建一个节点
        if (update_list.size() >= query_list.size()) {

            //使用更新pos序列，计算目标节点父节点(x,y)坐标
            int x = update_list.get(0), y = size - 1 - update_list.get(0);
            for (int j = 1; j < update_list.size() - 1; j++) {//这里只计算到其父节点，因为该位置没节点，要初始化一个
                if (update_list.get(j) == 0)
                    y++;
                else x++;
            }

            //遍历数据库找到父亲节点
            for (NodeSet node : database) {
                if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
                    TreeNode<String> new_node = new TreeNode<>(data);

                    //更新序列最后一位表示目标几点和其父亲节点的关系
                    if (update_list.get(update_list.size() - 1) == 0) {
                        //左孩子，y++
                        y++;
                        node.getNode().setLeft(new_node);
                    } else {
                        //右孩子，x++
                        x++;
                        node.getNode().setRight(new_node);
                    }

                    //设置该节点对应的所有数据，把他加入数据库里
                    MMPoint new_point = new MMPoint(x, y);
                    new_node.setId(new_point);
                    NodeSet new_nodeset = new NodeSet(new_point, new_node);
                    database.add(new_nodeset);
                    break;
                }
            }
        } else {//否则，说明路线中有个节点是dummy，将其覆盖即可
            //使用更新pos序列，计算目标节点(x,y)坐标
            int x = update_list.get(0), y = size - 1 - update_list.get(0);//
            for (int j = 1; j < update_list.size(); j++) {
                if (update_list.get(j) == 0)
                    y++;
                else x++;
            }
            //找到对应节点，覆盖数据
            for (NodeSet node : database) {
                if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
                    node.getNode().setData(data);
                }
            }
        }


        //测试运行时间代码段区间
//        long endTime = System.nanoTime(); // 记录结束时间
//        long executionTime = (endTime - startTime) / 1000; // 计算代码段的运行时间（毫秒）
//        System.out.println("代码段的运行时间为: " + executionTime + " 毫秒");

        //清空更新序列，为下次更新做准备
        Update_Query_NewDVH.list.clear();
        return database;

    }
}
