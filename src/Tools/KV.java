package Tools;

import java.io.Serializable;

public class KV implements Serializable {
    public String key;
    public String value;
//    public int counter;// 先不用管这个

    public KV() {}

    public KV(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
