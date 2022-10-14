package model;

import java.util.EmptyStackException;

public class StackUndo <T,H> implements IStack<T, H> {
    private NodeHistory<T, H> head;

    public StackUndo(){
    }

    @Override
    public void push(T elementT, H elementH, String actionT) {
        NodeHistory<T, H> current = new NodeHistory<>();
        if (head==null){
            head = new NodeHistory<>(elementT, elementH, actionT);
        }else {
            current.setNext(head);
            head = current;
            current.setNodeTvalue(elementT);
            current.setNodeHvalue(elementH);
            current.setActionT(actionT);
        }
    }

    @Override
    public NodeHistory pop() {
        if (head==null) {
            throw new EmptyStackException();
        }else {
            NodeHistory tReturn = head;
            if(head.getNext()==null){
                head=null;
            }else {
                head = head.getNext();
            }
            return tReturn;
        }
    }

    @Override
    public NodeHistory peek() {
        if (head==null) {
            //throw new EmptyStackException();
            return null;
        }else {
            return head;
        }
    }

    @Override
    public boolean isEmpty() {
        if (head==null) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void clear(){
        head = null;
    }
}
