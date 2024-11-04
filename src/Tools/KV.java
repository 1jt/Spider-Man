package Tools;

import java.io.Serializable;
import java.util.Objects;

public  class KV implements Serializable {

    private static final long serialVersionUID = 1401358247399335467L;
    public String key;
    public String value;
    public String op;
//    public int counter;// 先不用管这个

    public KV() {}

    public KV(String key, String value) {
        this.key = key;
        this.value = value;
        this.op = "add";
    }
    public KV(String key, String value, String op) {
        this.key = key;
        this.value = value;
        this.op = op;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 如果是同一个对象，返回 true
        if (o == null || getClass() != o.getClass()) return false; //
        KV kv = (KV) o; // 强制类型转换
        return Objects.equals(key, kv.key) && Objects.equals(value, kv.value); // 判断 key 和 value 是否相等
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value); // 根据 key 和 value 生成哈希码
    }

    @Override
    public String toString() {
        return "{" +
                key + ',' +
                value +
                '}';
    }


}
