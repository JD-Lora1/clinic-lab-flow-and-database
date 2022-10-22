package model;

public class NodeHistory <T> {
    private NodeHistory<T> next;
    private T nodeTvalue;
    private String option;
    private String actionT;

    public NodeHistory() {
    }

    public NodeHistory(String option, T nodeTvalue, String actionT) {
        this.nodeTvalue = nodeTvalue;
        this.actionT = actionT;
    }

    public NodeHistory<T> getNext() {
        return next;
    }

    public void setNext(NodeHistory<T> next) {
        this.next = next;
    }

    public T getNodeTvalue() {
        return nodeTvalue;
    }

    public void setNodeTvalue(T nodeTvalue) {
        this.nodeTvalue = nodeTvalue;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getActionT() {
        return actionT;
    }

    public void setActionT(String actionT) {
        this.actionT = actionT;
    }
}
