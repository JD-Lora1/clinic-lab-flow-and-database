package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackUndoTest {

    private StackUndo<Node, NodeQueue> stackUndo;

    public void setup1(){
        stackUndo = new StackUndo<>();
    }

    public void setup2(){
        setup1();
        stackUndo.push( new Node(new Patient("PatientFirst","0")), null,"Insert AVL-Node");
        stackUndo.push( new Node(new Patient("patientLast","2")), null,"Insert AVL-Node");
    }

    @Test
    void push() {
        setup2();
        stackUndo.push( new Node(new Patient("lastOne","3")), null,"Insert AVL-Node");
        Node node = (Node) stackUndo.peek().getNodeTvalue();
        assertEquals(node.getPatient().getId(), "3");
    }

    @Test
    void pop() {
        setup2();
        assertEquals(stackUndo.peek(), stackUndo.pop());
        Node nodet = (Node) stackUndo.peek().getNodeTvalue();
        assertEquals("PatientFirst", nodet.getPatient().getName());
    }

    @Test
    void peek() {
        setup2();
        assertEquals(stackUndo.peek(), stackUndo.pop());
    }

    @Test
    void isEmpty() {
        setup1();
        assertTrue(stackUndo.isEmpty());
        setup2();
        assertFalse(stackUndo.isEmpty());
    }

    @Test
    void clear() {
        setup2();
        stackUndo.clear();
        assertTrue(stackUndo.isEmpty());
    }
}