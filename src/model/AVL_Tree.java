package model;

public class AVL_Tree {
    private Node root;

    public void insert(Patient patient){
        root = insert(patient, root);
    }
    private Node insert(Patient patient, Node node) {
        if (node == null) {
            node = new Node(patient);
            System.out.println("Patient added");
        }
        else if (patient.getId().compareTo(node.getPatient().getId()) < 0) {
            node.left = insert( patient, node.left );
            if( getHeight( node.left ) - getHeight( node.right ) == 2 ) {
                if (patient.getId().compareTo(node.left.getPatient().getId()) < 0)
                    node = rotateWithLeftChild(node);
                else
                    node = doubleWithLeftChild(node);
            }
        }
        else if (patient.getId().compareTo(node.getPatient().getId()) > 0) {
            node.right = insert( patient, node.right );
            if( getHeight( node.right ) - getHeight( node.left ) == 2 ) {
                if (patient.getId().compareTo(node.right.getPatient().getId()) > 0)
                    node = rotateWithRightChild(node);
                else
                    node = doubleWithRightChild(node);
            }
        }else {
            System.out.println("This id already exists");
        }
        node.height = Math.max( getHeight(node.left), getHeight( node.right) ) + 1;
        return node;
    }

    public int getHeight(Node node ) {
        return node == null ? -1 : node.height;
    }
    private Node rotateWithLeftChild(Node node2) {
        Node node1 = node2.left;
        node2.left = node1.right;
        node1.right = node2;
        node1.height = Math.max(getHeight(node1.left), getHeight(node1.right)) +1;
        node2.height = Math.max(getHeight(node2.left), getHeight(node2.right)) +1;
        return node1;
    }

    private Node rotateWithRightChild(Node node1) {
        Node node2 = node1.right;
        node1.right = node2.left;
        node2.right = node1;
        node2.height = Math.max(getHeight(node2.left), getHeight(node2.right)) + 1;
        node1.height = Math.max(getHeight(node1.left), getHeight(node1.right)) + 1;
        return node2;
    }

    private Node doubleWithLeftChild(Node k3)
    {
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }

    private Node doubleWithRightChild(Node k1)
    {
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }

    public Node findPatient(String id){
        return findPatient(id, root);
    }

    private Node findPatient(String value, Node current){
        if(current == null){
            return null;
        }else if(current.getPatient().getId().equals(value)){
            return current;
        } else if (value.compareTo(current.getPatient().getId()) > 0) {
            return findPatient(value, current.right);
        } else {
            return findPatient(value, current.left);
        }
    }
    public Node delete(String id){
        return delete(id, root);
    }
    private Node delete(String goal, Node current){
        if(current == null){
            return null;
        }
        if(current.getPatient().getId().equals(goal)){
            //1. Nodo Hoja
            if(current.left == null && current.right == null){
                if(current == root)
                    root = null;
                return null;
            }
            //2. Nodo con un solo hijo
            else if (current.right == null || current.left == null){
                if(current == root)
                    root = (current.left!=null ? current.left:current.right);
                return (current.left!=null ? current.left:current.right);
            }
            //3. Nodo con hijos
            else{
                Node min = findLeft(current.right);
                current.getPatient().setId(min.getPatient().getId());
                //Hacer eliminación a partir de la derecha
                Node subarbolDER = delete(min.getPatient().getId(), current.right);
                current.right = ( subarbolDER );
                return current;
            }

        }else if(goal.compareTo(current.getPatient().getId()) < 0){
            Node subArbolIzquierdo = delete(goal, current.left);
            current.left = (subArbolIzquierdo);
        }else{
            Node subArbolDerecho = delete(goal, current.right);
            current.right = (subArbolDerecho);
        }
        return current;
    }
    public Node findLeft(Node current){
        if(current.left == null){
            return current;
        }
        return findLeft(current.left);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
