import java.io.Serializable;

public class NodeSet implements Serializable {
    //每个节点，对应着一个直角坐标系坐标，和一个树节点信息，封装在NodeSet里
    private MMPoint position;
    private TreeNode node;
    public NodeSet() {
    }
    public NodeSet(MMPoint position, TreeNode node){
        this.position=position;
        this.node=node;
    }
    //获取xy坐标
    public MMPoint getPosition() {
        return position;
    }
    //获取节点信息
    public TreeNode getNode(){
        return node;
    }
    //设置xy坐标
    public void setPosition(MMPoint position) {
        this.position = position;
    }
    //设置节点信息
    public void setNode(TreeNode node) {
        this.node = node;
    }
}
