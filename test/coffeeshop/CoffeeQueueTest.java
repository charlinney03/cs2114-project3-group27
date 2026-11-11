package coffeeshop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

public class CoffeeQueueTest {
    private Order order(String name) {
        Order order = new Order(new Customer(name));
        order.addItem(new MenuItem("Latte", 4.50));
        return order;
    }

    // Check that a new queue starts empty and does not let us peek or remove an order
    @Test
    void startsEmpty() {
        CoffeeQueue queue = new CoffeeQueue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertThrows(NoSuchElementException.class, queue::peek);
        assertThrows(NoSuchElementException.class, queue::dequeue);
    }

    // Make sure orders come out in the same order they were added
    @Test
    void addsAndRemovesInFIFOOrder() {
        CoffeeQueue queue = new CoffeeQueue();
        Order a = order("Sam");
        Order b = order("Alex");
        Order c = order("Kim");
        queue.enqueue(a);
        queue.enqueue(b);
        queue.enqueue(c);
        assertSame(a, queue.peek());
        assertEquals(3, queue.size());
        assertSame(a, queue.dequeue());
        assertEquals("COMPLETED", a.getStatus());
        assertSame(b, queue.dequeue());
        assertSame(c, queue.dequeue());
        assertTrue(queue.isEmpty());
        assertThrows(NoSuchElementException.class, queue::peek);
        // Check that the tail was reset when the queue became empty
        Order d = order("Taylor");
        queue.enqueue(d);
        assertSame(d, queue.peek());
        assertEquals(1, queue.size());
    }

    // Check that cancelling orders from different spots keeps the queue connected
    @Test
    void cancelHeadMiddleAndTail() {
        CoffeeQueue queue = new CoffeeQueue();
        Order a = order("Sam");
        Order b = order("Alex");
        Order c = order("Kim");
        Order d = order("Taylor");
        queue.enqueue(a); queue.enqueue(b); queue.enqueue(c); queue.enqueue(d);
        assertTrue(queue.cancelOrder(new Customer("Sam")));
        assertTrue(queue.cancelOrder(new Customer("Kim")));
        assertTrue(queue.cancelOrder(new Customer("Taylor")));
        assertEquals(1, queue.size());
        assertSame(b, queue.peek());
        assertEquals("CANCELLED", a.getStatus());
        assertEquals("CANCELLED", c.getStatus());
        assertEquals("CANCELLED", d.getStatus());
        assertEquals(-1, queue.findPosition(a));
        assertSame(d, queue.searchOrder(d.getOrderId()));
    }

    // Make sure the queue still works after its only order gets cancelled
    @Test
    void cancellingOnlyOrderResetsQueue() {
        CoffeeQueue queue = new CoffeeQueue();
        Order a = order("Sam");
        queue.enqueue(a);
        assertTrue(queue.cancelOrder(new Customer("Sam")));
        assertTrue(queue.isEmpty());
        Order b = order("Alex");
        queue.enqueue(b);
        assertSame(b, queue.peek());
    }

    // Check that cancelling by name only removes the first waiting order
    @Test
    void duplicateNamesCancelOldestWaitingOrder() {
        CoffeeQueue queue = new CoffeeQueue();
        Order a = order("Sam");
        Order b = order("Sam");
        queue.enqueue(a); queue.enqueue(b);
        assertTrue(queue.cancelOrder(new Customer("Sam")));
        assertEquals("CANCELLED", a.getStatus());
        assertEquals("WAITING", b.getStatus());
        assertSame(b, queue.peek());
    }

    // Make sure we can find positions and look up waiting, completed, and cancelled orders
    @Test
    void findsPositionsAndSearchesAllStatuses() {
        CoffeeQueue queue = new CoffeeQueue();
        Order a = order("Sam");
        Order b = order("Alex");
        Order c = order("Kim");
        queue.enqueue(a); queue.enqueue(b); queue.enqueue(c);
        assertEquals(1, queue.findPosition(a));
        assertEquals(2, queue.findPosition(b));
        assertEquals(3, queue.findPosition(c));
        queue.dequeue();
        queue.cancelOrder(new Customer("Kim"));
        assertEquals(-1, queue.findPosition(a));
        assertEquals(-1, queue.findPosition(c));
        assertSame(a, queue.searchOrder(a.getOrderId()));
        assertSame(b, queue.searchOrder(b.getOrderId()));
        assertSame(c, queue.searchOrder(c.getOrderId()));
        assertThrows(NoSuchElementException.class, () -> queue.searchOrder(-999));
    }

    // Check that invalid inputs do not change or break the queue
    @Test
    void badInputsLeaveQueueUnchanged() {
        CoffeeQueue queue = new CoffeeQueue();
        Order a = order("Sam");
        queue.enqueue(a);
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(null));
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(a));
        assertThrows(IllegalArgumentException.class, () -> queue.cancelOrder(null));
        assertThrows(IllegalArgumentException.class, () -> queue.findPosition(null));
        assertFalse(queue.cancelOrder(new Customer("Nobody")));
        assertEquals(1, queue.size());
        assertSame(a, queue.peek());
        queue.dequeue();
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(a));
        assertTrue(queue.isEmpty());
    }
}
