package coffeeshop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Collections;

public class ViewTest {
    // Check that the views only display text provided through the controller
    @Test
    void viewsOnlyStoreControllerText() {
        CusView customer = new CusView();
        BarView barista = new BarView();
        assertEquals("", customer.getInfo());
        assertEquals("", barista.getInfo());
        LineController controller = new LineController(new CoffeeQueue(), customer, barista,
                Collections.emptyList());
        customer.setController(controller);
        barista.setController(controller);
        assertTrue(customer.getInfo().contains("Orders waiting"));
        assertTrue(barista.getInfo().contains("No orders waiting"));
        assertThrows(IllegalArgumentException.class, () -> customer.setController(controller));
        assertThrows(IllegalArgumentException.class, () -> barista.setController(null));
    }
}
