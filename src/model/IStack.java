package model;

public interface IStack <T, Class>{

    public void push(T element, Class aClass);
    public NodeHistory pop();
    public NodeHistory peek();
    public boolean isEmpty();
    public void clear();
}
