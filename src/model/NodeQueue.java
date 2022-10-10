package model;

public class NodeQueue<T> {
    //Values
    private T value;
    private NodeQueue next;
    private NodeQueue prev;

    public NodeQueue(T value) {
        this.value = value;
    }

    public NodeQueue getNext() {
        return next;
    }

    public void setNext(NodeQueue next) {
        this.next = next;
    }

    public NodeQueue getPrev() {
        return prev;
    }

    public void setPrev(NodeQueue prev) {
        this.prev = prev;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
