package Tools;
import java.io.Serializable;

public class KL implements Serializable{
    public String key;
    public int length;

    public KL() {}

    public KL(String key, int length) {
        this.key = key;
        this.length = length;
    }

    // 将 KL 对象转换为 KV 对象，以便于部分代码的处理
    public KV toKV() {
        return new KV(key, String.valueOf(length));
    }

}
