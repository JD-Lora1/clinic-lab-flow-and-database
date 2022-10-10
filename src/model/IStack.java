package model;

public interface IStack <T, H>{

    public void push(T elementT, H elementH, String actionT);
    public NodeHistory pop();
    public NodeHistory peek();
    public boolean isEmpty();
    public void clear();
}
