package coffeeshop;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class CoffeeQueueTest {

    

    @Test
    public void testEnqueue(){

        CoffeeQueue queue = new CoffeeQueue();
        Customer cus1 = new Customer("cus1");
        Customer cus2 = new Customer("cus2");

        Order test1 = new Order(1, cus1);
        Order test2 = new Order(2, cus2);

        queue.enqueue(test1);
        queue.enqueue(test2);

        assertEquals(2, queue.size());
        assertSame(test1, queue.peek());
    }


    @Test
    public void testDequeue() {
        CoffeeQueue queue = new CoffeeQueue();
        Customer cus1 = new Customer("cus1");
        Customer cus2 = new Customer("cus2");

        Order test1 = new Order(1, cus1);
        Order test2 = new Order(2, cus2);

        queue.enqueue(test1);
        queue.enqueue(test2);

        Order removed = queue.dequeue();

        assertSame(test1, removed);
        assertEquals(1, queue.size());
        assertSame(test2, queue.peek());
}

    @Test
    public void testPeek() {
        CoffeeQueue queue = new CoffeeQueue();
        Customer cus1 = new Customer("cus1");
        Customer cus2 = new Customer("cus2");

        Order test1 = new Order(1, cus1);
        Order test2 = new Order(2, cus2);

        queue.enqueue(test1);
        queue.enqueue(test2);

        assertSame(test1, queue.peek());
        
    }

    @Test
    public void testFindPosition() {
        CoffeeQueue queue = new CoffeeQueue();
        Customer cus1 = new Customer("cus1");
        Customer cus2 = new Customer("cus2");
        Customer cus3 = new Customer("cus3");

        Order test1 = new Order(1, cus1);
        Order test2 = new Order(2, cus2);
        Order test3 = new Order(3, cus3);

        queue.enqueue(test1);
        queue.enqueue(test2);

        assertEquals(1, queue.findPosition(test1));
        assertEquals(2, queue.findPosition(test2));
        assertEquals(-1, queue.findPosition(test3));
    }


    @Test
    public void testSearchOrder() {
        CoffeeQueue queue = new CoffeeQueue();
        Customer cus1 = new Customer("cus1");
        Customer cus2 = new Customer("cus2");
        Customer cus3 = new Customer("cus3");

        Order completed = new Order(1, cus1);
        Order pending = new Order(2, cus2);
        Order cancelled = new Order(3, cus3);

        queue.enqueue(completed);
        queue.enqueue(cancelled);
        queue.enqueue(pending);

        queue.dequeue();             
        queue.cancelOrder(cus3);     

        assertSame(completed, queue.searchOrder(1));
        assertSame(pending, queue.searchOrder(2));
        assertSame(cancelled, queue.searchOrder(3));
  }
}
