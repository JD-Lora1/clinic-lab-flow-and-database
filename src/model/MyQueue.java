package model;

public class MyQueue {

    private NodeQueue head;
    private NodeQueue tail;

    public void enqueue(NodeQueue node) {
        if (head == null) {
            head = node;
        }else{
            tail.setNext(node);
            tail.setNext(node);
        }
        tail = node;
    }


    public NodeQueue dequeue() {
        if (head == null) {
            return null;
        }else{
            NodeQueue out = head;
            head = head.getNext();

            return out;
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
