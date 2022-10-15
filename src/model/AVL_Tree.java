package model;

import java.util.Comparator;

public class AVL_Tree {

    private Node root;
    private Comparator comparator;

    public AVL_Tree(Comparator comparator) {
        this.comparator = comparator;
    }

    public Node insert(Patient patient){
        return root = insert(patient, root);
    }
    private Node insert(Patient patient, Node node) {
        if (node == null) {
            node = new Node(patient);
            System.out.println("Patient added");
        }
        else if (comparator.compare(patient,node.getPatient()) < 0) {
            node.left = insert( patient, node.left );
            if( getHeight( node.left ) - getHeight( node.right ) == 2 ) {
                if (comparator.compare(patient,node.getPatient()) < 0)
                    node = rotateWithLeftChild(node);
                else
                    node = doubleWithLeftChild(node);
            }
        }
        else if (comparator.compare(patient,node.getPatient()) > 0) {
            node.right = insert( patient, node.right );
            if( getHeight( node.right ) - getHeight( node.left ) == 2 ) {
                if (comparator.compare(patient,node.getPatient()) > 0)
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
        return deleteNode(id, root);
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
                Node min = findMinimum(current.right);
                current.getPatient().setId(min.getPatient().getId());
                //Hacer eliminación a partir de la derecha
                current.right = (delete(min.getPatient().getId(), current.right));
                return current;
            }

        }else if(goal.compareTo(current.getPatient().getId()) < 0){
            current.left = (delete(goal, current.left)); //Subtree left
        }else{
            current.right = (delete(goal, current.right)); //Subtree right
        }
        return current;
    }

    public Node deleteNode(String goal, Node current) {
        // First, delete as a standard BST
        if (current == null)
            return null;

        // smaller
        if (goal.compareTo(current.getPatient().getId()) < 0)
            current.left = deleteNode(goal, current.left);
        //bigger
        else if (goal.compareTo(current.getPatient().getId()) > 0)
            current.right = deleteNode(goal, current.right);
        //goal found
        else {
            // node with only one child or no child
            if ((current.left == null) || (current.right == null)) {
                Node temp = null;
                if (current.left == null)
                    temp = current.right;
                else
                    temp = current.left;

                // No children
                if (temp == null) {
                    temp = current;
                    current = null;
                }
                else // One child
                    current = temp; // Copy the contents of the non-empty child
            }
            else {
                // node with two children
                Node temp = findMinimum(current.right);

                // Copy the inorder successor's data to this node
                current.getPatient().setId(temp.getPatient().getId());

                // Delete the inorder successor
                current.right = deleteNode(temp.getPatient().getId(), current.right);
            }
        }

        // If the tree had only one node then return
        if (current == null)
            return null;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether this node became unbalanced)
        int balance = getBalance(current);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(current.left) >= 0)
            return rotateWithRightChild(current);

        // Left Right Case
        if (balance > 1 && getBalance(current.left) < 0)
        {
            //current.left = leftRotate(current.left);
            return doubleWithRightChild(current);
        }

        // Right Right Case
        if (balance < -1 && getBalance(current.right) <= 0)
            return rotateWithLeftChild(current);

        // Right Left Case
        if (balance < -1 && getBalance(current.right) > 0)
        {
            //current.right = rightRotate(current.right);
            return doubleWithLeftChild(current);
        }

        return current;
    }

    int getBalance(Node N)
    {
        if (N == null)
            return 0;
        return getHeight(N.left) - getHeight(N.right);
    }

    public  Node findMinimum(Node current){
        //Find the left-most node. Minimum
        while (current.left != null)
            current = current.left;

        return current;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
