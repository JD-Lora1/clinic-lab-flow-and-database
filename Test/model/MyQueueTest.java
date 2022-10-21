package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyQueueTest {

    private MyQueue queue;

    public void setup0(){
        queue = new MyQueue();
    }

    public void setup1(){
        queue.enqueue(new NodeQueue<>(new Patient("Jeison", "123456", 53, true)));
        queue.enqueue(new NodeQueue<>(new Patient("Santi", "234567", 39, true)));
        queue.enqueue(new NodeQueue<>(new Patient("Juandi", "345678", 15, true)));
        queue.enqueue(new NodeQueue<>(new Patient("Jeimy", "456789", 24, true)));
    }

    public void setup2(){
        queue.enqueue(new NodeQueue<>(new Patient("Aaron Arango", "1002821688", 12, true)));
        queue.enqueue(new NodeQueue<>(new Patient("Belixa Baena", "34578457", 21, true)));
        queue.enqueue(new NodeQueue<>(new Patient("Cristhian Crown", "1005489694", 34, true)));
    }

    public void setup3(){
        queue.enqueue(new NodeQueue<>(new Patient("Andrea Castro", "1038246388", 14, true)));
        queue.dequeue();
    }

    @Test
    public void enqueueTest1(){
        //Simple enqueue
        setup0();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        NodeQueue<Patient> node2 = new NodeQueue<>(new Patient("Benito B", "315444", 53, false));
        queue.enqueue(node);
        queue.enqueue(node2);

        assertEquals(node2, queue.top());
    }

    @Test
    public void enqueueTest2(){
        //Todo
        // Allow to insert the patient just once in a queue
        setup0();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Otro Juan", "123456", 65, false));
        queue.enqueue(node);
        queue.enqueue(node);
        queue.enqueue(node);
        queue.enqueue(node);
        queue.dequeue();

        assert(queue.isEmpty());
    }

    @Test
    public void enqueueTest3(){
        //Todo
        // Don't allow to insert the patient in two differents queues at the same time
        MyQueue<Patient> otherQueue = new MyQueue<>();
        setup0(); //queue

        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Diana Diaz", "7329534", 22, false));
        queue.enqueue(node);
        otherQueue.enqueue(node);

        assertTrue(otherQueue.isEmpty());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void dequeueTest1(){
        //Todo
        // When is empty
        setup0();
        queue.dequeue();

        assertEquals(queue.top(), null);
    }

    @Test
    public  void dequeueTest2(){
        // Simple dequeue
        setup0();
        setup1();
        queue.dequeue();
        queue.dequeue();
        Patient patient = (Patient) queue.top().getValue();

        assertEquals(patient.getName(), "Santi");
    }

    @Test
    public  void dequeueTest3(){
        setup2();
        queue.dequeue();
        queue.enqueue(new NodeQueue<Patient>(new Patient("Prueba", )));
        queue.dequeue();

        assertEquals(queue.top().getValue(), queue.dequeue());
    }

    @Test
    public void isEmptyTest(){
        queue = setup1();
        assertEquals(queue.isEmpty(), false);
    }

    @Test
    public void isEmptyTest2(){
        queue = setup0();
        assertEquals(queue.isEmpty(), true);
    }

    @Test
    public void isEmptyTest3(){
        queue = setup2();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        assertEquals(queue.isEmpty(), true);
    }
}