package chFB;

import Tools.*;

import java.io.Serializable;

public class TwoChoiceHash implements Serializable {
    private int s; // 满二叉树的数量
    private int h; // 每颗二叉树的高度
    private int c; // 参数c
    private TreeNode[] forest; // 存储KV对的森林
    private byte[][] EMM; // 存储密文
    private TreeNode<KV> root; // 存储KV对的树

    public TwoChoiceHash(KV[] kv, int s, int h) {
        this.s = s;
        this.h = h;
        this.forest = new TreeNode[s];
        this.EMM = new byte[s][32];
        Setup(kv);
    }
    public void Setup(KV[] kv) {
        for (int i = 0; i < s; i++) {
//            forest[i] = new TreeNode<KV>();
            // 初始化深度为h的满二叉树
            forest[i] = TreeOperation.createFullBinaryTree(h);
        }
        forest[0].setData(kv[0]);
        System.out.println("forest[0] height: " + TreeOperation.getTreeDepth(forest[0]));
        // 前序遍历 forest[0]
        System.out.println("Preorder traversal of binary tree is ");
        TreeOperation.preOrder(forest[0]);

    }

    public void InsertEntry(int index, KV[] kv_list) {

    }
}
