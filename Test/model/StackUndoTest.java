package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackUndoTest {

    private StackUndo<String> stackUndo;

    public void setup1(){
        stackUndo = new StackUndo<>();
    }

    public void setup2(){
        setup1();
        stackUndo.push("JsonElementNodeQueue-format", NodeQueue.class);
        stackUndo.push("JsonElementAVltree-format", AVL_Tree.class);
    }

    @Test
    void push() {
        setup1();
        stackUndo.push("JsonElementNodeQueue-format", NodeQueue.class);
        assertEquals(stackUndo.peek().getValue(), "JsonElementNodeQueue-format");
    }

    @Test
    void pop() {
        setup2();
        assertEquals(stackUndo.peek(), stackUndo.pop());
        assertEquals("JsonElementNodeQueue-format", stackUndo.peek().getValue());
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