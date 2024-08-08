package dprfMM;

import Tools.*;
import dprfMM.GGM;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Cuckoo_Hash implements Serializable {

    // TODO 该变量应该是随机生成的，不应该是固定的
    private static long K_d=456;// 以 GGM 结构为基础的哈希函数的密钥
    public static long Get_K_d(){ return K_d;}

    // TODO 该变量应该是随机生成的，不应该是固定的
    private static byte[] K_e = "7975922666f6eb02".getBytes(StandardCharsets.UTF_8);// AES 加密的密钥
    public static byte[] Get_K_e() { return K_e; }

    int table_size; // 哈希表的大小(单张表)
    public int Get_Table_Size(){ return table_size;}

    int level; // GGM 结构的层数
    public int Get_Level(){ return level;}

    static int[] cuckoo_table;//store raw data

    private byte[][] EMM;//store ciphertext
    public byte[][] Get_EMM(){
        return EMM;
    }

    private ArrayList<KV> stash = new ArrayList<>();//store evicted elements
    public ArrayList<KV> Get_Stash(){ return stash;}

    // 记录存进去的元素，一旦被踢出，可以避免重复计算
    // TODO 把该成员变量变为函数内部的变量，没必要保存，而且绝对不能静态
    private Map<String,Integer> leave_map = new HashMap<>();
    public void Leave_Map_Clear() { leave_map.clear();}

    public Cuckoo_Hash(){}
    public String Get(int index) throws Exception {
        if (index< 0 || index >= table_size * 2)
            System.out.println("Index out of bounds");
        else
            return new String(AESUtil.decrypt(K_e, EMM[index]));
        return null;
    }

    // 用于 dprfMM 的建立函数
    public void Setup(KV[] kv_list, int level) throws Exception {
        this.level = level;
        table_size = (int) Math.floor((kv_list.length * (1 + 0.3)));
        cuckoo_table = new int[table_size*2];
        Arrays.fill(cuckoo_table,-1);
        EMM = new byte[table_size*2][32];

        for (int i = 0; i < kv_list.length; i++) {
            this.InsertEntry(i,kv_list);
        }

        Random random = new Random();
        for(int i=0;i<cuckoo_table.length;i++) {
            if (cuckoo_table[i] == -1) {
                cuckoo_table[i] = random.nextInt(1000000);
                EMM[i] = AESUtil.encrypt(K_e,("dummy,dummy,"+cuckoo_table[i]).getBytes(StandardCharsets.UTF_8));
            }else{
                EMM[i]=AESUtil.encrypt(K_e,(kv_list[cuckoo_table[i]].key+","+kv_list[cuckoo_table[i]].value).getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    // 用于dpMM存储 l(key) 的建立函数
    public void Setup(KL[] kl_list) throws Exception {
        this.level = 1;
        table_size = (int) Math.floor((kl_list.length * (1 + 0.3)));
        cuckoo_table = new int[table_size*2];
        Arrays.fill(cuckoo_table,-1);
        EMM = new byte[table_size*2][32];

        for (int i = 0; i < kl_list.length; i++) {
            this.InsertEntry(i,kl_list);
        }
        Random random = new Random();
        for(int i=0;i<cuckoo_table.length;i++) {
            if (cuckoo_table[i] == -1) {
                cuckoo_table[i] = random.nextInt(1000000);
                EMM[i] = AESUtil.encrypt(K_e,("dummy,dummy,"+cuckoo_table[i]).getBytes(StandardCharsets.UTF_8));
            }else{
                EMM[i]=AESUtil.encrypt(K_e,(kl_list[cuckoo_table[i]].key+","+kl_list[cuckoo_table[i]].length).getBytes(StandardCharsets.UTF_8));
            }
        }
        Leave_Map_Clear();
    }

    // 用于 dprfMM 的建立函数
    public void InsertEntry(int index,KV[] kv_list) {
        int count = 0;
        int h;
        int Left_Node, Right_Node;
        while (count < 5*level) {
            String k = kv_list[index].key+","+kv_list[index].value.substring(5);
            String k0 = k+",0";
            if(leave_map.containsKey(k0)) {
                // 万一之前存的被踢出来，就不用重复计算了
                Left_Node = leave_map.get(k0);
            }else{
                // 利用 GGM 结构生成哈希值
                byte[] father_Node;
                String value = kv_list[index].value;
                father_Node = GGM.Doub_GGM_Path(Hash.Get_SHA_256((kv_list[index].key+K_d).getBytes(StandardCharsets.UTF_8)), level, tool.DecimalConversion(Integer.parseInt(value.substring(5)), 2, level));
                // 第一张表中的位置
                Left_Node = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9 ),table_size,0);
                leave_map.put(k0,Left_Node);
                // 第二张表中的位置
                Right_Node = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),table_size,1);
                leave_map.put(k+",1",Right_Node);
            }
            h = Left_Node;
            int temp = cuckoo_table[h];
            if (temp == -1) {
                cuckoo_table[h] = index;
                return;
            } else {
                cuckoo_table[h] = index;
                index = temp;
            }

            k = kv_list[index].key+","+kv_list[index].value.substring(5);
            String k1 =  k+",1";
            Right_Node = leave_map.get(k1);
            h = Right_Node;
            temp = cuckoo_table[h];
            if (temp == -1) {
                cuckoo_table[h] = index;
                return;
            } else {
                cuckoo_table[h] = index;
                index = temp;
            }
            ++count;
        }
//        System.out.println("add an element into the stash");
        stash.add(kv_list[index]);
    }


    // 用于dpMM存储 l(key) 的插入函数
    public void InsertEntry(int index,KL[] kl_list) {
        int count = 0;
        int h;
        int Left_Node, Right_Node;
        while (count < 5*level) {
            String k = kl_list[index].key;
            String k0 = k+",0";
            if(leave_map.containsKey(k0)) {
                // 万一之前存的被踢出来，就不用重复计算了
                Left_Node = leave_map.get(k0);
            }else{
                // 利用 GGM 结构生成哈希值
                byte[] father_Node = GGM.Doub_GGM_Path(Hash.Get_SHA_256((kl_list[index].key+K_d).getBytes(StandardCharsets.UTF_8)), level, tool.DecimalConversion(1, 2, level));
                // 第一张表中的位置
                Left_Node = GGM.Map2Range(Arrays.copyOfRange(father_Node, 1 , 9 ),table_size,0);
                leave_map.put(k0,Left_Node);
                // 第二张表中的位置
                Right_Node = GGM.Map2Range(Arrays.copyOfRange(father_Node, 17, 26),table_size,1);
                leave_map.put(k+",1",Right_Node);
            }
            h = Left_Node;
            int temp = cuckoo_table[h];
            if (temp == -1) {
                cuckoo_table[h] = index;
                return;
            } else {
                cuckoo_table[h] = index;
                index = temp;
            }

            k = kl_list[index].key;
            String k1 = k +",1";
            Right_Node = leave_map.get(k1);
            h = Right_Node;
            temp = cuckoo_table[h];
            cuckoo_table[h] = index;
            if (temp == -1) {
                return;
            } else {
                index = temp;
            }
            ++count;
        }
        System.out.println("add an element into the CT's stash");
        stash.add(kl_list[index].toKV());
    }
}

