package model;

public class NodeHistory <T, Class>{
    private NodeHistory<T, Class> next;
    private T value;
    private Class aClass;

    public NodeHistory() {
    }

    public NodeHistory(T value, Class aClass) {
        this.value = value;
        this.aClass = aClass;
    }

    public NodeHistory<T, Class> getNext() {
        return next;
    }

    public void setNext(NodeHistory<T, Class> next) {
        this.next = next;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
