package coffeeshop;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
public class LineControllerTest {
    private static class Setup {
        CoffeeQueue queue = new CoffeeQueue();
        CusView customer = new CusView();
        BarView barista = new BarView();
        LineController controller = new LineController(queue,customer,barista,
            Arrays.asList(new MenuItem("Latte",4.50),new MenuItem("Mocha",5)));
    }
    @Test void addsCompletesAndUpdatesBothViews() {
        Setup s = new Setup();
        int id = s.controller.addOrder("Sam",Arrays.asList("Latte","Mocha"));
        assertTrue(id>0);
        assertEquals(1,s.queue.size());
        assertEquals(1,s.controller.findPosition(id));
        assertTrue(s.customer.getInfo().contains("Orders waiting: 1"));
        assertTrue(s.barista.getInfo().contains("Sam"));
        assertTrue(s.controller.searchOrder(id).contains("$9.50"));
        assertTrue(s.controller.complete().contains("COMPLETED"));
        assertEquals(-1,s.controller.findPosition(id));
        assertEquals(0,s.queue.size());
        assertNull(s.controller.complete());
        assertTrue(s.barista.getInfo().contains("No orders waiting"));
    }
    @Test void invalidInputNeverAddsPartialOrder() {
        Setup s = new Setup();
        assertEquals(-1,s.controller.addOrder(" ",Arrays.asList("Latte")));
        assertEquals(-1,s.controller.addOrder("Sam",Collections.emptyList()));
        assertEquals(-1,s.controller.addOrder("Sam",Arrays.asList("Latte","Unknown")));
        assertEquals(-1,s.controller.addOrder("Sam",Arrays.asList("Latte",null)));
        assertEquals(-1,s.controller.addOrder("Sam",null));
        assertEquals(0,s.queue.size());
        assertEquals(-1,s.controller.findPosition(999999));
        assertNull(s.controller.searchOrder(999999));
        assertFalse(s.controller.cancelOrder(" "));
        assertFalse(s.controller.cancelOrder("Nobody"));
        assertTrue(s.controller.getMenuText().contains("Latte - $4.50"));
    }
    @Test void cancelsOnlyOldestOrderForCustomer() {
        Setup s = new Setup();
        int a = s.controller.addOrder("Sam",Arrays.asList("Latte"));
        int b = s.controller.addOrder("Sam",Arrays.asList("Mocha"));
        assertTrue(s.controller.cancelOrder("Sam"));
        assertTrue(s.controller.searchOrder(a).contains("CANCELLED"));
        assertEquals(1,s.controller.findPosition(b));
    }
    @Test void estimateIsAvailableThroughControllerAndHandlesUnknownId() {
        Setup s = new Setup();
        int a = s.controller.addOrder("Sam",Arrays.asList("Latte","Mocha"));
        int b = s.controller.addOrder("Alex",Arrays.asList("Mocha"));
        assertEquals(0,s.controller.estimateWaitMinutes(a));
        assertEquals(6,s.controller.estimateWaitMinutes(b));
        assertEquals(-1,s.controller.estimateWaitMinutes(-42));
        s.controller.complete();
        assertEquals(0,s.controller.estimateWaitMinutes(b));
        assertEquals(-1,s.controller.estimateWaitMinutes(a));
    }
    @Test void rejectsInvalidConstructorDependencies() {
        Setup s = new Setup();
        assertThrows(IllegalArgumentException.class,()->new LineController(null,s.customer,s.barista,Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,()->new LineController(s.queue,s.customer,s.barista,Arrays.asList((MenuItem)null)));
    }
}
