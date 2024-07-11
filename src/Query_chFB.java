import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

public class Query_chFB {
    public static ArrayList<String> Run(String Query, int size, TreeNode<String>[] roots, ArrayList<String> Stash, int h){
        ArrayList<String> result = new ArrayList<>();
        String kappa = HashKit.sha1(Query);
        for (int num = 0; num < 256; num++) {
            BigInteger tmp_r0 = new BigInteger(HashKit.sha256(kappa + num + 0), 16);
            BigInteger tmp_r1 = new BigInteger(HashKit.sha256(kappa + num + 1), 16);
            int root_0 = tmp_r0.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();
            int root_1 = tmp_r1.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();
            BigInteger tmp_0 = new BigInteger(HashKit.md5(kappa + num + 0), 16);
            BigInteger tmp_1 = new BigInteger(HashKit.md5(kappa + num + 1), 16);
            int pos_0 = tmp_0.divideAndRemainder(BigInteger.valueOf((long) Math.pow(2, h)))[1].intValue();
            int pos_1 = tmp_1.divideAndRemainder(BigInteger.valueOf((long) Math.pow(2, h)))[1].intValue();
            String binary_pos_0 = Integer.toBinaryString(pos_0);
            String binary_pos_1 = Integer.toBinaryString(pos_1);
            for (int i = binary_pos_0.length(); i < h; i++) {
                binary_pos_0 = 0 + binary_pos_0;
            }
            for (int i = binary_pos_1.length(); i < h; i++) {
                binary_pos_1 = 0 + binary_pos_1;
            }
            TreeNode<String> node_0 = roots[root_0];
            TreeNode<String> node_1 = roots[root_1];
            result.add(node_0.getData());
            for (int i = 0; i < h; i++) {
                char index = binary_pos_0.charAt(i);
                if (index == '0') {
                    node_0 = node_0.getLeft();
                    result.add(node_0.getData());
                } else if (index == '1') {
                    node_0 = node_0.getRight();
                    result.add(node_0.getData());
                }
            }
            result.add(node_1.getData());
            for (int i = 0; i < h; i++) {
                char index = binary_pos_1.charAt(i);
                if (index == '0') {
                    node_1 = node_1.getLeft();
                    result.add(node_1.getData());
                } else if (index == '1') {
                    node_1 = node_1.getRight();
                    result.add(node_1.getData());
                }
            }
            // Find Stash
            for (int i = 0; i < Stash.size(); i++) {
                String tmp = Stash.get(i);
                String[] keyValue_tmp = tmp.split("\\+");
                if (Objects.equals(Query, keyValue_tmp[0])) {
                    result.add(tmp);
                }
            }
        }
        return result;
    }
}
