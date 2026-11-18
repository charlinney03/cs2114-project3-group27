package coffeeshop;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
public class CoffeeQueueTest {
    private Order order(String name, int items) {
        Order o = new Order(new Customer(name));
        for (int i = 0; i < items; i++) o.addItem(new MenuItem("Latte", 4.50));
        return o;
    }
    @Test void startsEmpty() {
        CoffeeQueue q = new CoffeeQueue();
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
        assertThrows(NoSuchElementException.class, q::peek);
        assertThrows(NoSuchElementException.class, q::dequeue);
        assertThrows(NoSuchElementException.class, () -> q.searchOrder(100));
        assertEquals(-1, q.estimateWaitMinutes(100));
    }
    @Test void addsAndRemovesInFIFOOrder() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam", 1), b = order("Alex", 1), c = order("Kim", 1);
        q.enqueue(a); q.enqueue(b); q.enqueue(c);
        assertSame(a, q.peek());
        assertEquals(3, q.size());
        assertSame(a, q.dequeue());
        assertEquals("COMPLETED", a.getStatus());
        assertSame(b, q.dequeue());
        assertSame(c, q.dequeue());
        assertTrue(q.isEmpty());
        assertThrows(NoSuchElementException.class, q::peek);
        // Check the tail resets after removing the last order
        Order d = order("Taylor", 1);
        q.enqueue(d);
        assertSame(d, q.peek());
    }
    @Test void interleavedEnqueuesAndDequeuesPreserveOrder() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("A",1), b = order("B",1), c = order("C",1), d = order("D",1);
        q.enqueue(a); q.enqueue(b);
        assertSame(a, q.dequeue());
        q.enqueue(c);
        assertSame(b, q.dequeue());
        q.enqueue(d);
        assertSame(c, q.dequeue());
        assertSame(d, q.dequeue());
        assertTrue(q.isEmpty());
    }
    @Test void cancelHeadMiddleAndTail() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",1), b = order("Alex",1), c = order("Kim",1), d = order("Taylor",1);
        q.enqueue(a); q.enqueue(b); q.enqueue(c); q.enqueue(d);
        assertTrue(q.cancelOrder(new Customer("Sam")));
        assertTrue(q.cancelOrder(new Customer("Kim")));
        assertTrue(q.cancelOrder(new Customer("Taylor")));
        assertEquals(1,q.size());
        assertSame(b,q.peek());
        assertEquals("CANCELLED",a.getStatus());
        assertEquals("CANCELLED",c.getStatus());
        assertEquals("CANCELLED",d.getStatus());
        assertEquals(-1,q.findPosition(a));
        assertSame(d,q.searchOrder(d.getOrderId()));
    }
    @Test void cancellingOnlyOrderResetsQueue() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",1);
        q.enqueue(a);
        assertTrue(q.cancelOrder(new Customer("Sam")));
        assertTrue(q.isEmpty());
        Order b = order("Alex",1);
        q.enqueue(b);
        assertSame(b,q.peek());
    }
    @Test void duplicateNamesCancelOldestWaitingOrder() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",1), b = order("Sam",1);
        q.enqueue(a); q.enqueue(b);
        assertTrue(q.cancelOrder(new Customer("Sam")));
        assertEquals("CANCELLED",a.getStatus());
        assertEquals("WAITING",b.getStatus());
        assertSame(b,q.peek());
    }
    @Test void findsPositionsAndSearchesAllStatuses() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",1), b = order("Alex",1), c = order("Kim",1);
        q.enqueue(a); q.enqueue(b); q.enqueue(c);
        assertEquals(1,q.findPosition(a));
        assertEquals(2,q.findPosition(b));
        assertEquals(3,q.findPosition(c));
        q.dequeue();
        q.cancelOrder(new Customer("Kim"));
        assertEquals(-1,q.findPosition(a));
        assertEquals(-1,q.findPosition(c));
        assertSame(a,q.searchOrder(a.getOrderId()));
        assertSame(b,q.searchOrder(b.getOrderId()));
        assertSame(c,q.searchOrder(c.getOrderId()));
        assertThrows(NoSuchElementException.class,()->q.searchOrder(-999));
    }
    @Test void badInputsLeaveQueueUnchanged() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",1);
        q.enqueue(a);
        assertThrows(IllegalArgumentException.class,()->q.enqueue(null));
        assertThrows(IllegalArgumentException.class,()->q.enqueue(a));
        assertThrows(IllegalArgumentException.class,()->q.cancelOrder(null));
        assertThrows(IllegalArgumentException.class,()->q.findPosition(null));
        assertFalse(q.cancelOrder(new Customer("Nobody")));
        assertEquals(1,q.size());
        assertSame(a,q.peek());
        q.dequeue();
        assertThrows(IllegalArgumentException.class,()->q.enqueue(a));
        assertTrue(q.isEmpty());
    }
    @Test void estimatesWaitForFirstMiddleLastAndMultipleItems() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",2), b = order("Alex",1), c = order("Kim",3);
        q.enqueue(a); q.enqueue(b); q.enqueue(c);
        assertEquals(0,q.estimateWaitMinutes(a.getOrderId()));
        assertEquals(6,q.estimateWaitMinutes(b.getOrderId()));
        assertEquals(9,q.estimateWaitMinutes(c.getOrderId()));
        assertEquals(3,q.size());
    }
    @Test void waitEstimateChangesAfterCompleteAndCancel() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",2), b = order("Alex",1), c = order("Kim",1);
        q.enqueue(a); q.enqueue(b); q.enqueue(c);
        assertEquals(9,q.estimateWaitMinutes(c.getOrderId()));
        q.dequeue();
        assertEquals(3,q.estimateWaitMinutes(c.getOrderId()));
        assertEquals(-1,q.estimateWaitMinutes(a.getOrderId()));
        assertTrue(q.cancelOrder(new Customer("Alex")));
        assertEquals(0,q.estimateWaitMinutes(c.getOrderId()));
        assertEquals(-1,q.estimateWaitMinutes(b.getOrderId()));
    }
    @Test void unknownAndInvalidIdsHaveSafeEstimates() {
        CoffeeQueue q = new CoffeeQueue();
        Order a = order("Sam",1);
        q.enqueue(a);
        assertEquals(-1,q.estimateWaitMinutes(-1));
        assertEquals(-1,q.estimateWaitMinutes(Integer.MAX_VALUE));
        assertEquals(1,q.size());
        assertSame(a,q.peek());
    }
}
