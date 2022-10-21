package model;

public interface IQueue<T>{
    public void enqueue(NodeQueue node);
    public Patient dequeue();
    public void undoEnqueue();
    public void undoDequeue(Patient patient);
    public boolean isEmpty();

}
