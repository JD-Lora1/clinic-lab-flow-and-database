package model;

public class AVL_Tree {
    private Node root;

    public void insert(Node node){
        if(root == null){
            root = node;
        }else{
            insert(node, root);
        }
    }
    private void insert(Node node, Node current){
        if (node.getPacient().getId() < current.getPacient().getId()) {
            //Izquierda
            if(current.getLeft() != null){
                insert(node, current.getLeft());
            }else{
                current.setLeft(node);
            }
        }
        else if(node.getPacient().getId() > current.getPacient().getId()){
            //Derecha
            if(current.getRight() != null){
                insert(node, current.getRight());
            }else{
                current.setRight(node);
            }
        } else {
            System.out.println("This user already exists");
        }
    }

    public void inorder(){
        inorder(root);
    }
    private void inorder(Node current){
        if(current == null){
            return;
        }
        inorder(current.getRight());
        System.out.println(current.getPacient().getId());
        inorder(current.getLeft());
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
