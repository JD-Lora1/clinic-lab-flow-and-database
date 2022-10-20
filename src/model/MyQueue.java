package model;

public class MyQueue<T> implements  IQueue<T>{

    private NodeQueue head;
    private NodeQueue tail;

    public void enqueue(NodeQueue node) {
        if (head == null) {
            head = node;
        }else{
            if (head == tail){
                head.setNext(node);
            } else{
                tail.setNext(node);
            }
        }
        tail = node;
    }


    public NodeQueue dequeue() {
        if (head == null) {
            return null;
        }else{
            NodeQueue<T> out = head;
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

    public void printQueue(){
        NodeQueue current = head;
        int number = 1;
        while(current!=null){
            System.out.println(number + "." + current.toPrint());
            number++;
            current = current.getNext();
        }
    }

    public void setHead(NodeQueue head) {
        this.head = head;
    }

    public NodeQueue top(){
        return head;
    }

}
