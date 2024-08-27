public class Roots {

    public static TreeNode<byte[]>[] CreateRoots(int n)
    {
        TreeNode<byte[]>[] nodeArray = new TreeNode[n];
        for (int i = 0; i < n; i++) {
            nodeArray[i] = new TreeNode<byte[]>(null);
        }
        return nodeArray;
    }
}
