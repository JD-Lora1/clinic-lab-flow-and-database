package model;

import java.util.Comparator;

public class AVL_Tree implements ITree{

    private NodeTree root;
    private Comparator comparator;

    public AVL_Tree(Comparator comparator) {
        this.comparator = comparator;
    }

    public NodeTree insert(Patient patient){
        root = insert(patient, root);
        //TODO
        // return the patient just when this one is added. Dont return when its repeated
        return root;
    }
    private NodeTree insert(Patient patient, NodeTree nodeTree) {
        if (nodeTree == null) {
            nodeTree = new NodeTree(patient);
            System.out.println("Patient added");
        }
        else if (comparator.compare(patient, nodeTree.getPatient()) < 0) {
            nodeTree.left = insert( patient, nodeTree.left );
            if( getHeight( nodeTree.left ) - getHeight( nodeTree.right ) == 2 ) {
                if (comparator.compare(patient, nodeTree.getPatient()) < 0)
                    nodeTree = rotateWithLeftChild(nodeTree);
                else
                    nodeTree = doubleWithLeftChild(nodeTree);
            }
        }
        else if (comparator.compare(patient, nodeTree.getPatient()) > 0) {
            nodeTree.right = insert( patient, nodeTree.right );
            if( getHeight( nodeTree.right ) - getHeight( nodeTree.left ) == 2 ) {
                if (comparator.compare(patient, nodeTree.getPatient()) > 0)
                    nodeTree = rotateWithRightChild(nodeTree);
                else
                    nodeTree = doubleWithRightChild(nodeTree);
            }
        }else {
            System.out.println("This id already exists");
        }
        nodeTree.height = Math.max( getHeight(nodeTree.left), getHeight( nodeTree.right) ) + 1;
        return nodeTree;
    }

    public int getHeight(NodeTree nodeTree) {
        return nodeTree == null ? -1 : nodeTree.height;
    }
    private NodeTree rotateWithLeftChild(NodeTree nodeTree2) {
        NodeTree nodeTree1 = nodeTree2.left;
        nodeTree2.left = nodeTree1.right;
        nodeTree1.right = nodeTree2;
        nodeTree1.height = Math.max(getHeight(nodeTree1.left), getHeight(nodeTree1.right)) +1;
        nodeTree2.height = Math.max(getHeight(nodeTree2.left), getHeight(nodeTree2.right)) +1;
        return nodeTree1;
    }

    private NodeTree rotateWithRightChild(NodeTree nodeTree1) {
        NodeTree nodeTree2 = nodeTree1.right;
        nodeTree1.right = nodeTree2.left;
        nodeTree2.right = nodeTree1;
        nodeTree2.height = Math.max(getHeight(nodeTree2.left), getHeight(nodeTree2.right)) + 1;
        nodeTree1.height = Math.max(getHeight(nodeTree1.left), getHeight(nodeTree1.right)) + 1;
        return nodeTree2;
    }

    private NodeTree doubleWithLeftChild(NodeTree k3)
    {
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }

    private NodeTree doubleWithRightChild(NodeTree k1)
    {
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }

    public NodeTree findPatient(String id){
        return findPatient(id, root);
    }

    private NodeTree findPatient(String value, NodeTree current){
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
    public Patient delete(String id){
        root = delete(id, root);
        Patient p;
        if (root != null){
            p = root.getPatient();
        }else {
            p = null;
        }
        return p;
    }

    private NodeTree delete(String goal, NodeTree current){
        if(current == null){
            return null;
        }
        if(current.getPatient().getId().equals(goal)){
            //1. Nodo Hoja
            if(current.left == null && current.right == null){
                if(current == root){
                    root = null;
                }
                return null;
            }
            //2. Nodo solo hijo izquierdo
            else if (current.right == null){
                if(current == root){
                    root = current.left;
                }
                return current.left;
            }
            //3. Nodo solo hijo derecho
            else if(current.left == null){
                if(current == root){
                    root = current.right;
                }
                return current.right;
            }
            //4. Nodo con dos hijos
            else{
                NodeTree min = findMinimum(current.right);
                //Transferencia de valores, NUNCA de conexiones
                current.getPatient().setId(min.getPatient().getId());
                //Hacer eliminación a partir de la derecha
                current.right =  delete(min.getPatient().getId(), current.right) ;
                return current;
            }

        }else if(goal.compareTo(current.getPatient().getId()) < 0){
            NodeTree subArbolIzquierdo = delete(goal, current.left);
            return current.left = subArbolIzquierdo;
        }else{
            NodeTree subArbolDerecho = delete(goal, current.right);
            return current.right = (subArbolDerecho);
        }
    }

    public void inorder(){
        inorder(root);
    }
    private void inorder(NodeTree current){
        if(current == null){
            return;
        }
        inorder(current.right);
        System.out.println(current.getPatient().toPrint());
        inorder(current.left);
    }

    public int getBalance(NodeTree N) {
        if (N == null)
            return 0;
        return getHeight(N.left) - getHeight(N.right);
    }

    public NodeTree findMinimum(NodeTree current){
        //Find the left-most node. Minimum
        while (current.left != null)
            current = current.left;

        return current;
    }

    public NodeTree getRoot() {
        return root;
    }

    public void setRoot(NodeTree root) {
        this.root = root;
    }
}