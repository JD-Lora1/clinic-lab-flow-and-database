package model;

import java.util.Comparator;

public class AVL_Tree {

    private NodeTree root;
    private Comparator comparator;

    public AVL_Tree(Comparator comparator) {
        this.comparator = comparator;
    }

    public Patient insert(Patient patient){
        root = insert(patient, root);
        //TODO
        // return the patient just when this one is added. Dont return when its repeated
        return patient;
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
        return deleteNode(id, root).getPatient();
    }

    private NodeTree deleteNode(String goal, NodeTree current) {
        // First, delete as a standard BST
        if (current == null)
            return null;

        // smaller
        if (goal.compareTo(current.getPatient().getId()) < 0)
            current = deleteNode(goal, current.left);
        //bigger
        else if (goal.compareTo(current.getPatient().getId()) > 0)
            current = deleteNode(goal, current.right);
        //goal found
        else {
            // node with only one child or no child
            if ((current.left == null) || (current.right == null)) {
                NodeTree temp = null;
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
                NodeTree temp = findMinimum(current.right);

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
            //current.left = leftRotate(current.left);
            return doubleWithRightChild(current);


        // Right Right Case
        if (balance < -1 && getBalance(current.right) <= 0)
            return rotateWithLeftChild(current);

        // Right Left Case
        if (balance < -1 && getBalance(current.right) > 0)
            //current.right = rightRotate(current.right);
            return doubleWithLeftChild(current);

        return current;
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

    int getBalance(NodeTree N)
    {
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
