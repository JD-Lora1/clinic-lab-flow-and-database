package model;

public class Node {
    private Node right;
    private Node left;
    private Patient patient;

    public Node(Patient patient) {
        this.patient = patient;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Patient getPacient() {
        return patient;
    }

    public void setPacient(Patient patient) {
        this.patient = patient;
    }
}
