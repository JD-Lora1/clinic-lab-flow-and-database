package model;

public class MyQueue<T> implements  IQueue<T>{

    private NodeQueue head;
    private NodeQueue tail;

    public void enqueue(NodeQueue node) {
        if (!((Patient)(node.getValue())).isInQueue()) {
            ((Patient)(node.getValue())).setInQueue(true);
            if (head == null) {
                head = node;
            } else {
                if (head == tail) {
                    head.setNext(node);
                    tail.setPrev(head);
                } else {
                    tail.setNext(node);
                    node.setPrev(tail);
                }
            }
            tail = node;
        }
    }


    public Patient dequeue() {
        if (head == null) {
            return null;
        }else{
            Patient out = (Patient)(head.getValue());
            out.setInQueue(false);
            head = head.getNext();
            head.setPrev(null);

            return out;
        }
    }

    public void undoEnqueue(){
        if (tail!=null) {
            ((Patient) (tail.getValue())).setInQueue(false);
            if (tail.getPrev() == null) {
                //Just one node on the queue;
                head = null;
            } else {
                tail = tail.getPrev();
                tail.setNext(null);
            }
        }
    }
    public void undoDequeue(Patient patient){
        if (!patient.isInQueue()) {
            patient.setInQueue(true);
            NodeQueue<Patient> node = new NodeQueue<>(patient);
            if (head != null) {
                node.setNext(head);
                head.setPrev(node);
                head = node;
            } else {
                head = node;
                tail = node;
            }
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
