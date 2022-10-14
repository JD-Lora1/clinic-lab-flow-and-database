package model;

public class NodeHistory <T, H>{
    private NodeHistory<T, H> next;
    private T nodeTvalue;
    private H nodeHvalue;
    private String actionT;

    public NodeHistory() {
    }

    public NodeHistory(T nodeTvalue, H nodeHvalue, String actionT) {
        this.nodeTvalue = nodeTvalue;
        this.nodeHvalue = nodeHvalue;
        this.actionT = actionT;
    }

    public NodeHistory<T,H> getNext() {
        return next;
    }

    public void setNext(NodeHistory<T, H> next) {
        this.next = next;
    }

    public T getNodeTvalue() {
        return nodeTvalue;
    }

    public void setNodeTvalue(T nodeTvalue) {
        this.nodeTvalue = nodeTvalue;
    }

    public H getNodeHvalue() {
        return nodeHvalue;
    }

    public void setNodeHvalue(H nodeHvalue) {
        this.nodeHvalue = nodeHvalue;
    }

    public String getActionT() {
        return actionT;
    }

    public void setActionT(String actionT) {
        this.actionT = actionT;
    }
}
