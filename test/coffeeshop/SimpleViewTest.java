package coffeeshop;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
public class SimpleViewTest {
    private String run(String input, CoffeeQueue queue) {
        CusView customer = new CusView();
        BarView barista = new BarView();
        LineController controller = new LineController(queue,customer,barista,Arrays.asList(new MenuItem("Latte",4.50)));
        customer.setController(controller);
        barista.setController(controller);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new SimpleView().run(controller,customer,barista,new Scanner(input),new PrintStream(buffer));
        return buffer.toString();
    }
    @Test void consoleHandlesMvpAndBadInputs() {
        CoffeeQueue q = new CoffeeQueue();
        String output = run("bad\n1\nSam\nUnknown\n1\nSam\nLatte\n4\nabc\n7\n2\n3\nNobody\n5\nabc\n0\n",q);
        assertTrue(output.contains("Please choose one of the menu options"));
        assertTrue(output.contains("Invalid name or menu items"));
        assertTrue(output.contains("Order placed"));
        assertTrue(output.contains("Please enter a number"));
        assertTrue(output.contains("COMPLETED"));
        assertTrue(output.contains("Goodbye"));
        assertTrue(q.isEmpty());
    }

    @Test void waitTimeChoiceHandlesUnknownAndNonnumericInput() {
        // Create an order first so we know its actual ID
        CoffeeQueue q = new CoffeeQueue();
        Order waiting = new Order(new Customer("Sam"));
        waiting.addItem(new MenuItem("Latte", 4.50));
        q.enqueue(waiting);
    
        // Test invalid input, unknown IDs, and a valid wait estimate
        String input = "8\nabc\n8\n-9\n8\n"
            + waiting.getOrderId() + "\n0\n";
    
        String output = run(input, q);
    
        assertTrue(output.contains("Please enter a number"));
        assertTrue(output.contains("No waiting order found"));
        assertTrue(output.contains("Estimated wait: 0 minutes"));
        assertEquals(1, q.size());
    }

    @Test void eofDoesNotCrash() {
        CoffeeQueue q = new CoffeeQueue();
        assertDoesNotThrow(()->run("1\n",q));
        assertTrue(q.isEmpty());
    }
}
