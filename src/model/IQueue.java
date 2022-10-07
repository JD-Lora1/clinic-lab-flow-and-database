package model;

public interface IQueue {

    public void enqueue(NodeQueue node);
    public NodeQueue dequeue();
    public boolean isEmpty();

}
