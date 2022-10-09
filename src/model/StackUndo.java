package model;

import java.util.EmptyStackException;

public class StackUndo <T> implements IStack<T> {
    private NodeHistory<T> head;

    public StackUndo(){
    }

    @Override
    public void push(T element, Class aClass) {
        NodeHistory<T> current = new NodeHistory<>();
        if (head==null){
            head = new NodeHistory<>(element, aClass);
        }else {
            current.setNext(head);
            head = current;
            current.setValue(element);
            current.setaClass(aClass);
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
