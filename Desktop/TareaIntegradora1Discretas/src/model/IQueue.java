package model;

public interface IQueue<T>{

    public void enqueue(NodeQueue node);
    public T dequeue();
    public void undoEnqueue();
    public void undoDequeue(NodeQueue node);
    public boolean isEmpty();

}
