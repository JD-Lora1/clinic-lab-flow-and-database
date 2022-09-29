package model;

public class Node {
    public Node right;
    public Node left;
    private Patient patient;
    public int height;

    public Node(Patient patient) {
        left = null;
        right = null;
        height = 0;
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

}
