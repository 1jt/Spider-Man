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
    // 获得二叉树的深度（根节点的深度为 0）
    public static int getTreeDepth(TreeNode<KV> root) {
        return root == null ? -1 : (1 + Math.max(getTreeDepth(root.getLeft()), getTreeDepth(root.getRight())));//保证根节点层数为0
    }

    // 打印可视化二叉树
    public static void show(TreeNode<KV> root) {
        if (root == null) {
            return;
        }
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        int arrayHeight = treeDepth + 1;
        int arrayWidth = (int) Math.pow(2, treeDepth);
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i ++) {
            for (int j = 0; j < arrayWidth; j ++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        writeArray(root, 0, 0, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        for (int i = 0; i < arrayHeight; i ++) {
            StringBuilder sb = new StringBuilder();
            int numSpace = (int) (arrayWidth/Math.pow(2,i)/2 * 14);
            String spaces = String.format("%" + numSpace +"s", "");

            for (int j = 0; j < res[i].length; j ++) {
                if (i == arrayHeight - 1) {
                    sb.append(" ");
                } else
                    sb.append(spaces);
                sb.append(res[i][j]);
            }
            System.out.println(sb.toString());
        }
    }

    private static void writeArray(TreeNode<KV> currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) return;
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = currNode.toString();

        // 计算当前位于树的第几层
        int currLevel = rowIndex;
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) return;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.getLeft() != null) {
            writeArray(currNode.getLeft(), rowIndex + 1, columnIndex * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.getRight() != null) {
            writeArray(currNode.getRight(), rowIndex + 1, columnIndex * 2 + 1, res, treeDepth);
        }
    }

    public static void preOrder(TreeNode treeNode) {
        if (treeNode != null) {
            System.out.print(treeNode.toString() + " ");
            preOrder(treeNode.getLeft());
            preOrder(treeNode.getRight());
        }
    }
}
