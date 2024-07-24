package dprfMM;

import Tools.Hash;
import Tools.tool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class GGM {
    private static Map<Long,byte[]> map = new HashMap<>();

    public static void clear(){
        map.clear();
    }

    public static byte[] Tri_GGM_Path(byte[] root, int level, int[] path){
        byte[] current_node = root;
        for(int i=0;i<level;i++){
            int temp = path[i];
            if(temp==0) {
                assert current_node != null; // 加一层保险
                current_node = Arrays.copyOfRange(current_node, 1, 10);
                long key = Hash.hash64(tool.bytesToLong(current_node),temp);
                if(map.containsKey(key)){
                    current_node = map.get(key);
                }else{
                    current_node = Hash.Get_SHA_256(current_node);
                    map.put(key,current_node);
                }
            }else if(temp==1){
                assert current_node != null;
                current_node = Arrays.copyOfRange(current_node, 11, 20);
                long key = Hash.hash64(tool.bytesToLong(current_node),temp);
                if(map.containsKey(key)){
                    current_node = map.get(key);
                }else{
                    current_node = Hash.Get_SHA_256(current_node);
                    map.put(key,current_node);
                }
            }else {
                assert current_node != null;
                current_node = Arrays.copyOfRange(current_node, 21, 30);
                long key = Hash.hash64(tool.bytesToLong(current_node),temp);
                if(map.containsKey(key)){
                    current_node = map.get(key);
                }else{
                    current_node = Hash.Get_SHA_256(current_node);
                    map.put(key,current_node);
                }
            }
        }
        return current_node;
    }


    public static byte[] Doub_GGM_Path(byte[] root, int level,int[] path){
        byte[] current_node = root;
        for(int i=0;i<level;i++){
            int temp = path[i];
            if(temp==0) {
                assert current_node != null;
                current_node = Arrays.copyOfRange(current_node, 1, 16);// 我觉得取8个字节就够了，15个字节正确但没有必要
                long key = Hash.hash64(tool.bytesToLong(current_node),temp);
                if(map.containsKey(key)){
                    current_node = map.get(key);
                }else{
                    current_node = Hash.Get_SHA_256(current_node);
                    map.put(key,current_node);
                }
            }else if(temp==1){
                assert current_node != null;
                current_node = Arrays.copyOfRange(current_node, 17,32);
                long key = Hash.hash64(tool.bytesToLong(current_node),temp);
                if(map.containsKey(key)){
                    current_node = map.get(key);
                }else{
                    current_node = Hash.Get_SHA_256(current_node);
                    map.put(key,current_node);
                }
            }
        }
        return current_node;
    }



    public static int GGM_Node(long volume, int index, long seed,int capacity) {
        long r = Long.rotateLeft(Hash.hash64(volume, seed), 21 * index);
        r = Hash.reduce((int) r,  capacity);
        r = r + (long) index * capacity;
        return (int) r;
    }


    // 利用 hash 值确定在哈希表中的位置，哈希表大小为 capacity，index 为第几张哈希表
    public static int Map2Range(byte[] hash,int capacity,int index) {
        long r = tool.bytesToLong(hash);
        r = Hash.reduce((int) r,  capacity);
        r = r + (long) index * capacity;
        return (int) r;
    }



}
