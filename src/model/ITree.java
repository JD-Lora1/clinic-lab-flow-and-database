package model;


public interface ITree{

    public Patient insert(Patient patient);

    private NodeTree insert(Patient patient, NodeTree nodeTree) {
        return null;
    }
    public int getHeight(NodeTree nodeTree);

    private NodeTree rotateWithRightChild(NodeTree nodeTree1){
        return null;
    }
    private NodeTree rotateWithLeftChild(NodeTree nodeTree2) {
        return null;
    }

    private NodeTree doubleWithRightChild(NodeTree k1) {
        return null;
    }
    private NodeTree doubleWithLeftChild(NodeTree k3){
        return null;
    }

    public NodeTree findPatient(String id);
    private NodeTree findPatient(String value, NodeTree current){
        return null;
    }

    public Patient delete(String id);
    private NodeTree deleteNode(String goal, NodeTree current){
        return null;
    }
    public void inorder();

    private void inorder(NodeTree current) {

    }

    public int getBalance(NodeTree N);
    public NodeTree findMinimum(NodeTree current);
    public  NodeTree getRoot() ;
    public void setRoot(NodeTree root);
}
