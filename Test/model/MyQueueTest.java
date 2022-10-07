package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyQueueTest {

    private MyQueue priority;
    private MyQueue secondary;

    public MyQueue setup1(){
        MyQueue priority = new MyQueue();
        priority.enqueue(new NodeQueue(new Patient("Juan", "123456", 65, true)));
        priority.enqueue(new NodeQueue(new Patient("Juan", "234567", 65, true)));
        priority.enqueue(new NodeQueue(new Patient("Juan", "345678", 65, true)));
        priority.enqueue(new NodeQueue(new Patient("Juan", "456789", 65, true)));

        return priority;
    }

    public MyQueue setup2(){
        MyQueue secondary = new MyQueue();
        secondary.enqueue(new NodeQueue(new Patient("Otro Juan", "123456", 65, false)));
        secondary.enqueue(new NodeQueue(new Patient("Juan", "234567", 65, false)));
        secondary.enqueue(new NodeQueue(new Patient("Juan", "456789", 65, false)));

        return secondary;
    }

    @Test
    public void enqueuePriorityQueueTest(){
        priority = setup1();
        Patient p1 = (Patient) priority.top().getValue();
        assertEquals("Juan",p1.getName());
    }

    @Test
    public void enqueuePriorityTest(){
        priority = setup1();
        priority.dequeue();
        priority.dequeue();

        assertEquals(priority.top(), priority.dequeue());
    }

    @Test
    public  void enqueueAndDequeueSecondaryTest(){
        secondary = setup2();
        secondary.dequeue();
        secondary.dequeue();
        secondary.dequeue();

        assertEquals(secondary.top(), secondary.dequeue());
    }

    @Test
    public void isEmptyTest(){
        priority = setup1();
        assertEquals(priority.isEmpty(), false);
    }

    @Test
    public void isEmptyTest2(){
        secondary = setup2();
        secondary.dequeue();
        secondary.dequeue();
        secondary.dequeue();
        secondary.dequeue();
        assertEquals(secondary.isEmpty(), true);
    }

}