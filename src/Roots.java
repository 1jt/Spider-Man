public class Roots {

    public static TreeNode<String>[] CreateRoots(int n)
    {
        TreeNode<String>[] nodeArray = new TreeNode[n];
        for (int i = 0; i < n; i++) {
            nodeArray[i] = new TreeNode<String>(null);
        }
        return nodeArray;
    }
}
