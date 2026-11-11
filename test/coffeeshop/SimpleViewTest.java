package coffeeshop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class SimpleViewTest {
    // Run the basic console flow and check that invalid inputs do not crash it
    @Test
    void consoleHandlesMvpAndBadInputs() {
        CoffeeQueue queue = new CoffeeQueue();
        CusView customer = new CusView();
        BarView barista = new BarView();
        LineController controller = new LineController(queue, customer, barista,
                Arrays.asList(new MenuItem("Latte", 4.50)));
        customer.setController(controller);
        barista.setController(controller);
        String input = "bad\n1\nSam\nUnknown\n1\nSam\nLatte\n4\nabc\n7\n2\n3\nNobody\n5\nabc\n0\n";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new SimpleView().run(controller, customer, barista, new Scanner(input), new PrintStream(buffer));
        String output = buffer.toString();
        assertTrue(output.contains("Please choose one of the menu options"));
        assertTrue(output.contains("Invalid name or menu items"));
        assertTrue(output.contains("Order placed"));
        assertTrue(output.contains("Please enter a number"));
        assertTrue(output.contains("COMPLETED"));
        assertTrue(output.contains("Goodbye"));
        assertTrue(queue.isEmpty());
    }
}
