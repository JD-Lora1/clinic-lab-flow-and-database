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
        if (node.getPatient().getId() < current.getPatient().getId()) {
            //Izquierda
            if(current.getLeft() != null){
                insert(node, current.getLeft());
                if( getHeight( current.getRight() ) - getHeight( current.getLeft() ) == 2 )
                    if( node.getPatient().getId() < current.getPatient().getId())
                        current = rotateLeft( current);
                    else
                        current = doubleWithLeftChild( current );
            }else{
                current.setLeft(node);
            }
        }
        else if(node.getPatient().getId() > current.getPatient().getId()){
            //Derecha
            if(current.getRight() != null){
                insert(node, current.getRight());
                if( getHeight( current.getRight() ) - getHeight( current.getLeft() ) == 2 )
                    if( node.getPatient().getId() > current.getPatient().getId())
                        current = rotateRight( current);
                    else
                        current = doubleWithRightChild( current );
            }else{
                current.setRight(node);
            }
        } else
            System.out.println("This user already exists");
        node.setH(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) +1);
    }

    public int getHeight(Node node ) {
        return node == null ? -1 : node.getH();
    }
    int getBalance(Node n) {
        return (n == null) ? 0 : getHeight(n.getRight()) - getHeight(n.getLeft());
    }
    public void updateHeight(Node n) {
        n.setH(1 + Math.max(getHeight(n.getLeft()), getHeight(n.getRight())));
    }
    private Node doubleWithLeftChild(Node k3)
    {
        k3.setLeft(rotateRight( k3.getLeft() ));
        return rotateLeft( k3 );
    }
    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child */
    private Node doubleWithRightChild(Node k1)
    {
        k1.setRight(rotateLeft( k1.getRight() ));
        return rotateRight( k1 );
    }

    private Node rotateLeft(Node node2) {
        Node node1 = node2.getLeft();
        node2.setLeft(node1==null?null:node1.getRight());
        if (node1!=null){
            node1.setRight(node2);
            node1.setH(Math.max(getHeight(node1.getLeft()), getHeight(node1.getLeft())) +1);
        }
        node2.setH(Math.max(getHeight(node2.getLeft()), getHeight(node2.getRight())) +1);
        return node1;
    }

    private Node rotateRight(Node node1) {
        Node node2 = node1.getRight();
        node1.setLeft(node2==null?null:node2.getRight());
        if (node2!=null){
            node2.setRight(node1);
            node2.setH(Math.max(getHeight(node1.getLeft()), getHeight(node2.getLeft())) +1);
        }
        node1.setH(Math.max(getHeight(node1.getLeft()), getHeight(node1.getRight())) +1);
        return node2;
    }

    public void inorder(){
        inorder(root);
    }
    private void inorder(Node current){
        if(current == null){
            return;
        }
        inorder(current.getRight());
        System.out.println(current.getPatient().getId());
        inorder(current.getLeft());
    }

    public Node findPatient(long id){
        return findPatient(id, root);
    }

    private Node findPatient(long value, Node current){
        if(current == null){
            return null;
        }else if(current.getPatient().getId() == value){
            return current;
        } else if (value > current.getPatient().getId()) {
            return findPatient(value, current.getRight());
        } else {
            return findPatient(value, current.getLeft());
        }
    }
    public Node delete(int id){
        return delete(id, root);
    }
    private Node delete(long goal, Node current){
        if(current == null){
            return null;
        }
        if(current.getPatient().getId() == goal){
            //1. Nodo Hoja
            if(current.getLeft() == null && current.getRight() == null){
                if(current == root)
                    root = null;
                return null;
            }
            //2. Nodo con un solo hijo
            else if (current.getRight() == null || current.getLeft() == null){
                if(current == root)
                    root = (current.getLeft()!=null ? current.getLeft():current.getRight());
                return (current.getLeft()!=null ? current.getLeft():current.getRight());
            }
            //3. Nodo con hijos
            else{
                Node min = findLeft(current.getRight());
                current.getPatient().setId(min.getPatient().getId());
                //Hacer eliminación a partir de la derecha
                Node subarbolDER = delete(min.getPatient().getId(), current.getRight());
                current.setRight( subarbolDER );
                return current;
            }

        }else if(goal < current.getPatient().getId()){
            Node subArbolIzquierdo = delete(goal, current.getLeft());
            current.setLeft(subArbolIzquierdo);
        }else{
            Node subArbolDerecho = delete(goal, current.getRight());
            current.setRight(subArbolDerecho);
        }
        return current;
    }
    public Node findLeft(Node current){
        if(current.getLeft() == null){
            return current;
        }
        return findLeft(current.getLeft());
    }

    public int findIterations(Node current, int i){
        if (current==null){
            return i;
        }
        if(current.getLeft() == null && current.getRight()==null){
            return i;
        }else if (current.getLeft()!=null && current.getRight()!=null ){
            int i2 = findIterations(current.getRight(),i+1);
            int i3 = findIterations(current.getLeft(),i+1);
            return Math.max(i2,i3);
        }else if (current.getRight()!=null){
            return findIterations(current.getRight(),i+1);
        }else {
            return findIterations(current.getLeft(), i+1);
        }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
