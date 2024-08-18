package chFB;

import java.io.Serializable;

public class TreeNode<T> implements Serializable {
    private T data;
    private TreeNode<T> left;
    private TreeNode<T> right;

    public TreeNode() {
        this.data = null;
        this.left = null;
        this.right = null;
    }

    public TreeNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
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

    @Override
    public String toString() {
        return "" +
                data;
    }

}