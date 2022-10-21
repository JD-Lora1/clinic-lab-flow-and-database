package model;

public interface IStack <T>{

    public void push(String option, T elementT, String actionT);
    public NodeHistory pop();
    public NodeHistory peek();
    public boolean isEmpty();
    public void clear();
}
