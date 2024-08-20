package chFB;

import Tools.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class TwoChoiceHash implements Serializable {
    private final int s; // 满二叉树的数量
    private final int h; // 每颗二叉树的高度
    private final int Last_layer; // 最后一行元素的数量，这个值经常用到

    public TreeNode<byte[]>[] B;// 存储加密数据库

    private final byte[] K;// AES 加密的密钥
    public byte[] Get_K() { return K; }

    private final byte[] K_enc;// AES 加密的密钥
    public byte[] Get_K_enc() { return K_enc; }

    public ArrayList<KV> stash = new ArrayList<>();//store evicted elements
    public ArrayList<KV> Get_Stash(){ return stash;}

    public TwoChoiceHash(KV[] kv_list, int s, int h) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.s = s;
        this.h = h;
        this.Last_layer = (int) Math.pow(2,h);
        this.B = new TreeNode[s];
        TreeNode<KV>[] forest = new TreeNode[s]; // 存储KV对的森林

        // 随机生成 K,K_enc
        K = new byte[16];
        new Random().nextBytes(K);
        K_enc = new byte[16];
        new Random().nextBytes(K_enc);

        Setup(kv_list,forest);
    }
    public void Setup(KV[] kv_list,TreeNode<KV>[] forest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for (int i = 0; i < s; i++) {
            // 初始化深度为h的满二叉树
            forest[i] = TreeOperation.createFullBinaryTree(h);
            B[i] = TreeOperation.createFullBinaryTree(h);
        }
        // 将 kv 插入到 forest 中
        InsertAll(kv_list,forest);
        // 将每个 forest 进行加密
        for (int i = 0; i < s; i++) {
            Encry_B(forest[i],B[i]);
        }
    }
    public void InsertAll(KV[] kv_list,TreeNode<KV>[] forest) {
        for (KV kv:kv_list) {
            int G_0 = G(kv, 0);
            int dep_0 = CheckDeepest((G_0 % Last_layer),forest[G_0 / Last_layer]);
            int G_1 = G(kv, 1);
            int dep_1 = CheckDeepest((G_1 % Last_layer),forest[G_1 / Last_layer]);
            if (dep_0 == -1 && dep_1 == -1)
                stash.add(kv);
            else if (dep_0 >= dep_1) {
                Insert(kv,forest[G_0 / Last_layer],dep_0,G_0 % Last_layer);
            } else {
                Insert(kv, forest[G_1 / Last_layer], dep_1, G_1 % Last_layer);
            }
        }
    }
    // 将KV 沿着路径 loc 插入到 node 深为 depth 的节点
    public void Insert(KV kv, TreeNode<KV> node,int depth,int loc) {
        StringBuilder binary_loc = new StringBuilder(Integer.toBinaryString(loc));
        if (binary_loc.length() < h) {
            int diff = h - binary_loc.length();
            for (int i = 0; i < diff; i++)
                binary_loc.insert(0, "0");
        }
        TreeNode<KV> tmp = node;
        for (int i = 0; i < depth; i++) {
            if (binary_loc.charAt(i) == '0')
                tmp = tmp.getLeft();
            else
                tmp = tmp.getRight();
        }
        tmp.setData(kv);
    }
    // token = F_K(key) G_token(j||0) & G_token(j||1)
    public int G(KV kv, int index) {
        if (index != 0 && index != 1) {
            System.out.println("index error");
        }
        byte[] token =  Hash.Get_SHA_256((Arrays.toString(K) + kv.key).getBytes(StandardCharsets.UTF_8));
        String key = Arrays.toString(token) + kv.value.substring(5) + index;
        byte[] hash = Hash.Get_SHA_256(key.getBytes(StandardCharsets.UTF_8));

        return Map2Range(hash, s * Last_layer);
    }
    public static int Map2Range(byte[] hash,int capacity) {
        long r = tool.bytesToLong(hash);
        return Hash.reduce((int) r, capacity);
    }
    // 计算树的非空最深处
    public int CheckDeepest(int index, TreeNode root) {
        if (root.getData() != null)
            return -1;
        StringBuilder binary_index = new StringBuilder(Integer.toBinaryString(index));
        if (binary_index.length() < h) {
            int diff = h - binary_index.length();
            for (int i = 0; i < diff; i++)
                binary_index.insert(0, "0");
        }
        int depth = 0;
        TreeNode<KV> tmp = root;
        for (int i = 0; i < h; i++){
            if (binary_index.charAt(i) == '0')
                tmp = tmp.getLeft();
            else
                tmp = tmp.getRight();
            if (tmp.getData() != null)
                break;
            depth++;
        }
        return depth;
    }

    // 将 forest 中的每个节点加密
    public void Encry_B(TreeNode<KV> root_p,TreeNode<byte[]> root_c) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (root_p == null) {
            return;
        }
        if (root_p.getData() != null) {
            root_c.setData(AESUtil.encrypt(K_enc, (root_p.getData().key+","+root_p.getData().value).getBytes(StandardCharsets.UTF_8)));
        }else {
            root_c.setData(AESUtil.encrypt(K_enc,("dummy,dummy,"+ new Random().nextInt(1000000)).getBytes(StandardCharsets.UTF_8)));
        }
        Encry_B(root_p.getLeft(),root_c.getLeft());
        Encry_B(root_p.getRight(),root_c.getRight());
    }

    public void Query(int tree_index, int loc, ArrayList<byte[]> result) {
        StringBuilder binary_index = new StringBuilder(Integer.toBinaryString(loc));
        if (binary_index.length() < h) {
            int diff = h - binary_index.length();
            for (int i = 0; i < diff; i++) {
                binary_index.insert(0, "0");
            }
        }
        // if 用于将涉及到的节点从bin中剔除(可以省略后续的去重，而且方便后续 重新写入)
        TreeNode<byte[]> tmp = B[tree_index];
        if (tmp.getData() != null){
            result.add(tmp.getData());
            tmp.setData(null);
        }

        for (int i = 0; i < h; i++) {
            if (binary_index.charAt(i) == '0') {
                tmp = tmp.getLeft();
                if (tmp.getData() != null){
                    result.add(tmp.getData());
                    tmp.setData(null);
                }
            } else {
                tmp = tmp.getRight();
                if (tmp.getData() != null){
                    result.add(tmp.getData());
                    tmp.setData(null);
                }
            }
        }
    }
    public void WriteBack(String key,ArrayList<String> v,ArrayList<KV> unwanted,Set<Integer> bin) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // write back Key
        for (String value: v){
            KV kv = new KV(key,value);
            int G_0 = G(kv, 0);
            int dep_0 = CheckDeepest((G_0 % Last_layer),B[G_0 / Last_layer]);
            int G_1 = G(kv, 1);
            int dep_1 = CheckDeepest((G_1 % Last_layer),B[G_1 / Last_layer]);
            if (!bin.contains(G_0) || !bin.contains(G_1))
                System.out.println("error");
            if (dep_0 == -1 && dep_1 == -1)
                stash.add(kv);
            else if (dep_0 >= dep_1) {
                TreeNode<byte[]> tmp = getTreeNode(G_0, dep_0);
                tmp.setData(AESUtil.encrypt(K_enc, (kv.key+","+kv.value).getBytes(StandardCharsets.UTF_8)));
            } else {
                TreeNode<byte[]> tmp = getTreeNode(G_1, dep_1);
                tmp.setData(AESUtil.encrypt(K_enc, (kv.key+","+kv.value).getBytes(StandardCharsets.UTF_8)));
            }
        }
        // write back unwanted
        for (KV kv:unwanted){
            int G_0 = G(kv, 0);
            int dep_0 = CheckDeepest((G_0 % Last_layer),B[G_0 / Last_layer]);
            int G_1 = G(kv, 1);
            int dep_1 = CheckDeepest((G_1 % Last_layer),B[G_1 / Last_layer]);
            if (dep_0 == -1 && dep_1 == -1)
                stash.add(kv);
            else if (dep_0 >= dep_1) {
                TreeNode<byte[]> tmp = getTreeNode(G_0, dep_0);
                tmp.setData(AESUtil.encrypt(K_enc, (kv.key+","+kv.value).getBytes(StandardCharsets.UTF_8)));
            } else {
                TreeNode<byte[]> tmp = getTreeNode(G_1, dep_1);
                tmp.setData(AESUtil.encrypt(K_enc, (kv.key+","+kv.value).getBytes(StandardCharsets.UTF_8)));
            }
        }
        // padding bin
        for(int i:bin){
            StringBuilder binary_loc = new StringBuilder(Integer.toBinaryString(i));
            if (binary_loc.length() < h) {
                int diff = h - binary_loc.length();
                for (int j = 0; j < diff; j++)
                    binary_loc.insert(0, "0");
            }
            TreeNode<byte[]> tmp = B[i / Last_layer];
            for (int j = 0; j < h; j++) {
                if (binary_loc.charAt(j) == '0')
                    tmp = tmp.getLeft();
                else
                    tmp = tmp.getRight();
                if (tmp.getData() == null){
                    tmp.setData(AESUtil.encrypt(K_enc,("dummy,dummy,"+ new Random().nextInt(1000000)).getBytes(StandardCharsets.UTF_8)));
                }
            }
        }

    }

    private TreeNode<byte[]> getTreeNode(int G_0, int dep_0) {
        StringBuilder binary_loc = new StringBuilder(Integer.toBinaryString(G_0 % Last_layer));
        if (binary_loc.length() < h) {
            int diff = h - binary_loc.length();
            for (int i = 0; i < diff; i++)
                binary_loc.insert(0, "0");
        }
        TreeNode<byte[]> tmp = B[G_0 / Last_layer];
        for (int i = 0; i < dep_0; i++) {
            if (binary_loc.charAt(i) == '0')
                tmp = tmp.getLeft();
            else
                tmp = tmp.getRight();
        }
        return tmp;
    }
}
