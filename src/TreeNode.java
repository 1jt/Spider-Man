public class TreeNode<T> {
    private T data;
    private TreeNode<T> left;
    private TreeNode<T> right;

    private MMPoint id;

    public TreeNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.id = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public void setRight(TreeNode<T> right) {
        this.right = right;
    }

    public MMPoint getId(){
        return id;
    }

    public  void  setId(MMPoint id){
        this.id = id;
    }

    public MMPoint getLeftId(){ return left.id; }

    public MMPoint getRightId(){ return right.id; }

    public void setLeftId(MMPoint id){ left.id = id; }

    public void setRightId(MMPoint id){ right.id = id; }




}