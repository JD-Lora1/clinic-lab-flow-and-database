package model;

public class MyQueue<T> implements  IQueue<T>{

    private NodeQueue head;
    private NodeQueue tail;

    public void enqueue(NodeQueue node) {
        if (head == null) {
            head = node;
        }else{
            if (head == tail){
                head.setNext(tail);
            } else{
                tail.setNext(node);
            }
        }
        tail = node;
    }


    public T dequeue() {
        if (head == null) {
            return null;
        }else{
            T out = (T)head.getValue();
            head = head.getNext();

            return out;
        }
    }

    public void undoEnqueue(){
        if (tail.getPrev()==null){
            //Just one node on the queue;
            head = null;
        }else {
            tail = tail.getPrev();
            tail.setNext(null);
        }
    }
    public void undoDequeue(NodeQueue node){
        if (head!=null) {
            node.setNext(head);
            head.setPrev(node);
            head = node;
        }else {
            head = node;
            tail = node;
        }
    }


    public boolean isEmpty() {
        if(head==null){
            return true;
        }else {
            return false;
        }
    }

    public void setHead(NodeQueue head) {
        this.head = head;
    }

    public NodeQueue top(){
        return head;
    }

}
