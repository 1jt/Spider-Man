import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

public class dprfMM {
    public static ArrayList<ArrayList<String>> Setup(ArrayList<String> DB_i, ArrayList<String> Stash, String seed){
        ArrayList<ArrayList<String>> DB= new ArrayList<ArrayList<String>>();
        ArrayList<String> T0 = new ArrayList<>();
        ArrayList<String> T1 = new ArrayList<>();
        DB.add(T0);
        DB.add(T1);
        int size = DB_i.size();
        if (size == 0)
            return DB;

        double alpha = 0.05;
        int t = (int) Math.ceil ((1 + alpha) * size);

        // fill T table
        for (int i = 0; i < t; i++) {
            T0.add("dummy");
            T1.add("dummy");
        }

        // eviction num
        int eviction = 20;

        // PRF密钥
        String K_PRF = HashKit.md5(seed);
        String key = null; // 每次读取的关键词
        String value = null; // 每次读取关键词对应的值
        String index = null;

        for (int i = 0; i < size; i++) {
            String[] keyValue = DB_i.get(i).split("\\+");
            if (keyValue.length == 2) {
                key = keyValue[0].trim();
                value = keyValue[1].trim();
                index = value.substring(5);
            } else {
                System.out.println("Invalid key-value pair: " + DB_i.get(i));
            }

            boolean flag = false;
            int num = 0;
            int T_id = 0;
            while (num < eviction){
                // 计算位置
                BigInteger tmp_0 = new BigInteger(HashKit.sha1(K_PRF + key + index + 0),16);
                BigInteger tmp_1 = new BigInteger(HashKit.sha1(K_PRF + key + index + 1),16);
                int pos_0 = tmp_0.divideAndRemainder(BigInteger.valueOf((long) t))[1].intValue();
                int pos_1 = tmp_1.divideAndRemainder(BigInteger.valueOf((long) t))[1].intValue();
                if(Objects.equals(T0.get(pos_0), "dummy")){
                    T0.set(pos_0, key + "+" + value);
                    flag = true;
                    break;
                }else if (Objects.equals(T1.get(pos_1), "dummy")){
                    T1.set(pos_1, key + "+" + value);
                    flag = true;
                    break;
                }else if (T_id == 0){
                    String tmp = T1.get(pos_1);
                    T1.set(pos_1, key + "+" + value);
                    String[] keyValue_tmp = tmp.split("\\+");
                    if (keyValue_tmp.length == 2) {
                        key = keyValue_tmp[0].trim();
                        value = keyValue_tmp[1].trim();
                        index = value.substring(5);
                    } else {
                        System.out.println("Invalid key-value pair: " + tmp);
                    }
                    T_id = 1;
                    num++;
                    continue;
                }else if(T_id == 1){
                    String tmp = T0.get(pos_0);
                    T0.set(pos_0, key + "+" + value);
                    String[] keyValue_tmp = tmp.split("\\+");
                    if (keyValue_tmp.length == 2) {
                        key = keyValue_tmp[0].trim();
                        value = keyValue_tmp[1].trim();
                        index = value.substring(5);
                    } else {
                        System.out.println("Invalid key-value pair: " + tmp);
                    }
                    T_id = 0;
                    num++;
                }
            }
            if (!flag){
                Stash.add(key + "+" + value);
            }
        }
//        System.out.println("T0:" + T0);
//        System.out.println("T1:" + T1);
//        System.out.println("Stash:" + Stash);

        return DB;
    }
}
