import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

public class Query_VHDSSE {
    public static ArrayList<String> Run(String Query, ArrayList<ArrayList<ArrayList<String>>> DB_result,ArrayList<String> Stash,ArrayList<String> buf, double beta){
        ArrayList<String> Result = new ArrayList<>();

        for (int i = 0; i < DB_result.size(); i++) {
            String K_PRF = HashKit.md5(String.valueOf(i));
            int lmax = (int) Math.ceil(DB_result.get(i).get(0).size() * beta);
            for (int j = 0; j < lmax; j++) {
                BigInteger tmp_0 = new BigInteger(HashKit.sha1(K_PRF + Query + j + 0), 16);
                BigInteger tmp_1 = new BigInteger(HashKit.sha1(K_PRF + Query + j + 1), 16);
                int pos_0 = tmp_0.divideAndRemainder(BigInteger.valueOf((long) DB_result.get(i).get(0).size()))[1].intValue();
                int pos_1 = tmp_1.divideAndRemainder(BigInteger.valueOf((long) DB_result.get(i).get(0).size()))[1].intValue();
                Result.add(DB_result.get(i).get(0).get(pos_0));
                Result.add(DB_result.get(i).get(1).get(pos_1));
            }
        }

        // Find Stash
        for (String tmp : Stash) {
            String[] keyValue_tmp = tmp.split("\\+");
            if (Objects.equals(Query, keyValue_tmp[0])) {
                Result.add(tmp);
            }
        }
        // Find buf
        for (String tmp : buf) {
            String[] keyValue_tmp = tmp.split("\\+");
            if (Objects.equals(Query, keyValue_tmp[0])) {
                Result.add(tmp);
            }
        }
        return Result;
    }
}
