package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackUndoTest {

    private StackUndo<Patient> stackUndo;

    public void setup1(){
        stackUndo = new StackUndo<>();
    }

    public void setup2(){
        setup1();
        stackUndo.push( "AVLtree",new Patient("PatientFirst","0", 1, true),"Insert AVL-Node");
        stackUndo.push( "AVLtree", new Patient("patientLast","2", 2, false),"Insert AVL-Node");
    }

    @Test
    void push() {
        setup2();
        stackUndo.push( "AVLtree", new Patient("lastOne","3",2, false), "Insert AVL-Node");
        NodeTree nodeTree = (NodeTree) stackUndo.peek().getNodeTvalue();
        assertEquals(nodeTree.getPatient().getId(), "3");
    }

    @Test
    void pop() {
        setup2();
        assertEquals(stackUndo.peek(), stackUndo.pop());
        NodeTree nodet = (NodeTree) stackUndo.peek().getNodeTvalue();
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