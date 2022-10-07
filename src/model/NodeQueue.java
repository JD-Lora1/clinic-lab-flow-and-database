package model;

public class NodeQueue {
    //Values
    private Patient patient;
    private NodeQueue next;

    public NodeQueue(Patient patient) {
        this.patient = patient;
    }

    public NodeQueue getNext() {
        return next;
    }

    public void setNext(NodeQueue next) {
        this.next = next;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
