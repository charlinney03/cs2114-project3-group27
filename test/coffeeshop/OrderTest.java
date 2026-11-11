package coffeeshop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OrderTest {
    // Check that new orders get their own ID and start with a waiting status
    @Test
    void newOrderHasUniqueIdAndWaitingStatus() {
        Order a = new Order(new Customer("Sam"));
        Order b = new Order(new Customer("Alex"));
        assertNotEquals(a.getOrderId(), b.getOrderId());
        assertEquals("WAITING", a.getStatus());
        assertTrue(a.getOrder().isEmpty());
        assertEquals("Sam", a.getCustomer().getName());
        assertTrue(a.describe().contains("$0.00"));
    }

    // Make sure items are added correctly and the total shows the right price
    @Test
    void addsItemsAndFormatsTotal() {
        Order order = new Order(new Customer("Sam"));
        order.addItem(new MenuItem("Latte", 4.50));
        order.addItem(new MenuItem("Mocha", 5.00));
        assertEquals(2, order.getOrder().size());
        assertTrue(order.describe().contains("$9.50"));
        assertTrue(order.describe().contains("WAITING"));
        assertThrows(UnsupportedOperationException.class,
                () -> order.getOrder().add(new MenuItem("Tea", 2)));
    }

    // Check that invalid changes are rejected and completed orders cannot be edited
    @Test
    void rejectsBadChanges() {
        assertThrows(IllegalArgumentException.class, () -> new Order(null));
        Order order = new Order(new Customer("Sam"));
        assertThrows(IllegalArgumentException.class, () -> order.addItem(null));
        assertTrue(order.getOrder().isEmpty());
        CoffeeQueue queue = new CoffeeQueue();
        queue.enqueue(order);
        queue.dequeue();
        assertEquals("COMPLETED", order.getStatus());
        assertThrows(IllegalStateException.class, () -> order.addItem(new MenuItem("Tea", 2)));
        assertThrows(IllegalStateException.class, () -> order.setStatus("WAITING"));
    }
}
