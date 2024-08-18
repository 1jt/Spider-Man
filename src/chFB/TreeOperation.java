package chFB;

import Tools.KV;

import java.util.Arrays;

public class TreeOperation {
    // 创建 一颗 满二叉树

    public static TreeNode<KV> createFullBinaryTree(int depth) {
        if (depth <= -1) {
            return null; // 基本条件：深度为 0，返回 null
        }

        // 创建当前节点
        TreeNode<KV> node = new TreeNode(); // 节点值设置为 null，表示空节点

        // 递归创建左子树和右子树
        node.setLeft(createFullBinaryTree(depth - 1));
        node.setRight(createFullBinaryTree(depth - 1));

        return node;
    }
    public static int getTreeDepth(TreeNode<KV> root) {
        return root == null ? -1 : (1 + Math.max(getTreeDepth(root.getLeft()), getTreeDepth(root.getRight())));//保证根节点层数为0
    }

    public static void preOrder(TreeNode treeNode) {
        if (treeNode != null) {
            System.out.print(treeNode.toString() + " ");
            preOrder(treeNode.getLeft());
            preOrder(treeNode.getRight());
        }
    }
}
