package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyQueueTest {

    private MyQueue priority;
    private MyQueue secondary;

    public MyQueue setup1(){
        MyQueue priority = new MyQueue();
        priority.enqueue(new NodeQueue<>(new Patient("Juan", "123456", 65, true)));
        priority.enqueue(new NodeQueue<>(new Patient("Juan", "234567", 65, true)));
        priority.enqueue(new NodeQueue<>(new Patient("Juan", "345678", 65, true)));
        priority.enqueue(new NodeQueue<>(new Patient("Juan", "456789", 65, true)));

        return priority;
    }

    public MyQueue setup2(){
        MyQueue secondary = new MyQueue();
        secondary.enqueue(new NodeQueue<>(new Patient("Otro Juan", "123456", 65, false)));
        secondary.enqueue(new NodeQueue<>(new Patient("Juan", "234567", 65, false)));
        secondary.enqueue(new NodeQueue<>(new Patient("Juan", "456789", 65, false)));

        return secondary;
    }

    public MyQueue setup3(){
        MyQueue queueTest = new MyQueue();
        return queueTest;
    }

    @Test
    public void enqueueTest(){
        priority = setup3();
        NodeQueue patient = new NodeQueue<Patient>(new Patient("Otro Juan", "123456", 65, false));
        priority.enqueue(patient);

        assertEquals(patient, priority.top());
    }

    @Test
    public void enqueueSecondTest(){
        secondary = setup3();
        priority = setup3();

        NodeQueue patient = new NodeQueue<Patient>(new Patient("Otro Juan", "123456", 65, false));
        priority.enqueue(patient);
        secondary.enqueue(patient);

        assertEquals(priority.top(), secondary.top());
    }

    @Test
    public void dequeuePriorityTest(){
        priority = setup1();
        priority.dequeue();
        priority.dequeue();

        assertEquals(priority.top().getValue(), priority.dequeue());
    }

    @Test
    public  void dequeueSecondaryTest(){
        secondary = setup2();
        secondary.dequeue();
        secondary.dequeue();

        assertEquals(secondary.top().getValue(), secondary.dequeue());
    }

    @Test
    public void isEmptyTest(){
        priority = setup1();
        assertEquals(priority.isEmpty(), false);
    }

    @Test
    public void isEmptyTest2(){
        secondary = setup3();
        assertEquals(secondary.isEmpty(), true);
    }

    @Test
    public void isEmptyTest3(){
        secondary = setup2();
        secondary.dequeue();
        secondary.dequeue();
        secondary.dequeue();
        secondary.dequeue();
        assertEquals(secondary.isEmpty(), true);
    }
}