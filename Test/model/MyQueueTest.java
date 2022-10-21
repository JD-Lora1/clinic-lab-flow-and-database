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
        Patient p =  (Patient) queue.top().getValue();

        assertEquals(node.getValue().getName(), p.getName());
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

        assertTrue(queue.isEmpty());
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

        assertEquals(patient.getName(), "Juandi");
    }

    @Test
    public  void dequeueTest3(){
        setup0();
        queue.enqueue(new NodeQueue<Patient>(new Patient("Prueba", "111",2,true)));
        queue.dequeue();
        setup1();

        assertEquals(((Patient)(queue.top().getValue())).getName(), ((Patient)(queue.dequeue().getValue())).getName());
    }

    //Undo

    @Test
    public void undoEnqueueTest1(){
        setup0();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        queue.enqueue(node);
        queue.undoEnqueue();

        assertTrue(queue.isEmpty());
    }

    @Test
    public void undoEnqueueTest2(){
        setup0();
        setup2();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        queue.enqueue(node);
        queue.undoEnqueue();

        assertEquals(((Patient)(queue.top().getValue())).getName(), "Aaron Arango");
    }

    @Test
    public void undoEnqueueTest3(){
        // Allows to enque after a undo
        setup0();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        queue.enqueue(node);
        queue.undoEnqueue();
        queue.enqueue(node);

        assertEquals(((Patient)(queue.top().getValue())).getName(), "Andres Arco");
    }

    @Test
    public void undoEnqueueTest4(){
        // Undo several times
        setup0();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        queue.enqueue(node);
        queue.undoEnqueue();
        queue.enqueue(node);
        queue.undoEnqueue();
        queue.undoEnqueue();
        queue.undoEnqueue();

        assertTrue(queue.isEmpty());
    }

    @Test
    public void undoEnqueueTest5(){
        // Undo with previous actions
        setup0();
        queue.undoEnqueue();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        queue.enqueue(node);

        assertTrue(!queue.isEmpty());
    }

    @Test
    public void undoDequeueTest1(){
        // Undo
        setup0();
        setup1();
        NodeQueue node1 = queue.dequeue();
        assertFalse(((Patient)node1.getValue()).isInQueue());
        NodeQueue node2 = queue.dequeue();
        queue.undoDequeue(node1);

        assertTrue(((Patient)node1.getValue()).isInQueue());
    }

    @Test
    public void undoDequeueTest2(){
        // Undo, with the same node, can't be
        setup0();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        queue.dequeue();
        queue.undoDequeue(node);
        queue.undoDequeue(node);
        queue.undoDequeue(node);
        queue.undoDequeue(node);
        queue.dequeue();

        assertTrue(queue.isEmpty());
    }
    @Test
    public void undoDequeueTest3(){
        // Undo with previous actions
        setup0();
        NodeQueue<Patient> node = new NodeQueue<>(new Patient("Andres Arco", "90996", 51, false));
        queue.undoDequeue(node);
        queue.enqueue(node);

        assertTrue(!queue.isEmpty());
    }

    @Test
    public void isEmptyTest(){
        setup0();
        setup1();
        assertEquals(queue.isEmpty(), false);
    }

    @Test
    public void isEmptyTest2(){
        setup0();
        assertEquals(queue.isEmpty(), true);
    }

    @Test
    public void isEmptyTest3(){
        setup0();
        setup2();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        assertEquals(queue.isEmpty(), true);
    }
}