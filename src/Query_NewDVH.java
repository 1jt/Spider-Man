import java.math.BigInteger;
import java.util.ArrayList;

public class Query_NewDVH {
    public static ArrayList<String> Run(String Query, int size,TreeNode<String>[] roots){
        ArrayList<String> Result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            String kappa = HashKit.sha1(Query + 0 + i);
            BigInteger tmp_3 = new BigInteger(kappa, 16);
            int root = tmp_3.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();
            TreeNode<String> node_tmp = roots[root];
//                kappa = HashKit.sha1(str + 0);
            int count = 0;
            while (node_tmp != null) {
                Result.add(node_tmp.getData());
//                System.out.println(node_tmp.getData());
                String Pos = HashKit.sha384(kappa + root + count++);
                BigInteger tmp_2 = new BigInteger(Pos, 16);
                int pos = tmp_2.divideAndRemainder(BigInteger.valueOf(2))[1].intValue();
                if (pos == 0)
                    node_tmp = node_tmp.getLeft();
                else if (pos == 1)
                    node_tmp = node_tmp.getRight();
            }
        }


        return Result;

    }
}
