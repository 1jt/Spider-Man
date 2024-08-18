package chFB;

import Tools.KV;
import Tools.SerialData;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class chFB implements Serializable {
    public int DATA_SIZE;// 数据库大小
    public int n;// upper bound on the total number of values
    public int MAX_VOLUME_LENGTH;// 数据库的最大volume
    public int l;// upper bound on the maximum volume

    private int s; // 满二叉树的数量
    private int h; // 每颗二叉树的高度
    private int c = 2; // 参数c

    private final byte[] K_enc;// AES 加密的密钥
    public byte[] Get_K_enc() { return K_enc; }

    public chFB(int DATA_SIZE, int MAX_VOLUME_LENGTH,String filename) {
        // 配置参数，(n,l) 均先简单设置为原数据库的2倍
        this.DATA_SIZE = DATA_SIZE;
        this.n = DATA_SIZE * 2;
        this.MAX_VOLUME_LENGTH = MAX_VOLUME_LENGTH;
        this.l = MAX_VOLUME_LENGTH * 2;
        // s = n/(clog(n))
        s = (int) Math.ceil((double) n / c / (Math.log(n) / Math.log(2)));
        // h = log(clog(n))
        h = (int) Math.ceil(Math.log(c * Math.log(n)/Math.log(2)) / Math.log(2));
        // 随机生成 K_enc
        K_enc = new byte[16];
        new Random().nextBytes(K_enc);
        KV[] kv_list = SerialData.Serial_Raw_In(filename);
        TwoChoiceHash twoChoiceHash = new TwoChoiceHash(kv_list,s,h);

    }

}
