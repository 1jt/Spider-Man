import java.math.BigInteger;
import java.util.ArrayList;

public class Update_Query_NewDVH {


    public static ArrayList<Integer> list = new ArrayList<>();

    public static ArrayList<String> Run(String key, int size, ArrayList<NodeSet> Position) {
        ArrayList<String> Result = new ArrayList<>();
        String kappa = HashKit.sha1(key + 0 + 1);
        BigInteger tmp_3 = new BigInteger(kappa, 16);
        int root = tmp_3.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();

        list.add(root);
        TreeNode<String> node_tmp = null;
        for (int j = 0; j < Position.size(); j++) {
            node_tmp = Position.get(j).getNode();
            if (node_tmp.getId().getX() == root && node_tmp.getId().getY() == size - 1 - root) {
                break;
            }
        }
        int count = 0;
        while (node_tmp != null) {
            Result.add(node_tmp.getData());
            String Pos = HashKit.sha384(kappa + root + count++);
            BigInteger tmp_2 = new BigInteger(Pos, 16);
            int pos = tmp_2.divideAndRemainder(BigInteger.valueOf(2))[1].intValue();
            list.add(pos);
            if (pos == 0)
                node_tmp = node_tmp.getLeft();
            else if (pos == 1)
                node_tmp = node_tmp.getRight();
        }
        return Result;

    }

}
