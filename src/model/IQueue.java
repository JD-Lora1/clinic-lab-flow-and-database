package model;

public interface IQueue<T>{

    public void enqueue(NodeQueue node);
    public T dequeue();
    public boolean isEmpty();

}
