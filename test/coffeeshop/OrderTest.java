package coffeeshop;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class OrderTest {
    @Test void newOrderHasUniqueIdAndWaitingStatus() {
        Order a = new Order(new Customer("Sam")), b = new Order(new Customer("Alex"));
        assertNotEquals(a.getOrderId(),b.getOrderId());
        assertEquals("WAITING",a.getStatus());
        assertTrue(a.getOrder().isEmpty());
        assertEquals("Sam",a.getCustomer().getName());
        assertTrue(a.describe().contains("$0.00"));
    }
    @Test void addsItemsAndFormatsTotal() {
        Order o = new Order(new Customer("Sam"));
        o.addItem(new MenuItem("Latte",4.50));
        o.addItem(new MenuItem("Mocha",5));
        assertEquals(2,o.getOrder().size());
        assertTrue(o.describe().contains("$9.50"));
        assertTrue(o.describe().contains("WAITING"));
        assertThrows(UnsupportedOperationException.class,()->o.getOrder().add(new MenuItem("Tea",2)));
    }
    @Test void rejectsBadChanges() {
        assertThrows(IllegalArgumentException.class,()->new Order(null));
        Order o = new Order(new Customer("Sam"));
        assertThrows(IllegalArgumentException.class,()->o.addItem(null));
        assertTrue(o.getOrder().isEmpty());
        CoffeeQueue q = new CoffeeQueue();
        q.enqueue(o); q.dequeue();
        assertEquals("COMPLETED",o.getStatus());
        assertThrows(IllegalStateException.class,()->o.addItem(new MenuItem("Tea",2)));
        assertThrows(IllegalStateException.class,()->o.setStatus("WAITING"));
    }
}
