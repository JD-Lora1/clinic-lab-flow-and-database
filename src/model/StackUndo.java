package model;

import java.util.EmptyStackException;

public class StackUndo <T> implements IStack<T> {
    private NodeHistory<T> head;

    public StackUndo(){
    }

    @Override
    public void push(String option, T elementT, String actionT) {
        NodeHistory<T> current = new NodeHistory<>();
        if (head==null){
            head = new NodeHistory<>(option, elementT, actionT);
        }else {
            current.setNext(head);
            head = current;
            current.setNodeTvalue(elementT);
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
