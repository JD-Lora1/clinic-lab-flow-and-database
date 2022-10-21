package model;

public class NodeTree {
    public NodeTree right;
    public NodeTree left;
    private Patient patient;
    public int height;

    public NodeTree(Patient patient) {
        left = null;
        right = null;
        height = 0;
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

}
