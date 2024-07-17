public class NodeSet {
    //每个节点，对应着一个直角坐标系坐标，和一个树节点信息，封装在NodeSet里
    private MMPoint position;
    private TreeNode node;
    public NodeSet() {
    }
    public NodeSet(MMPoint position, TreeNode node){
        this.position=position;
        this.node=node;
    }
    public MMPoint getPosition() {
        return position;
    }

    public TreeNode getNode(){
        return node;
    }

    public void setPosition(MMPoint position) {
        this.position = position;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }
}
