package coffeeshop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;

public class CustomerTest {
    // Check that customer names are saved and matching names count as the same customer
    @Test
    void savesNameAndMatchesSameName() {
        Customer a = new Customer("Sam");
        Customer b = new Customer("Sam");
        assertEquals("Sam", a.getName());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(1, new HashSet<>(java.util.Arrays.asList(a, b)).size());
        assertNotEquals(a, new Customer("sam"));
        assertNotEquals(a, null);
        assertNotEquals(a, "Sam");
    }

    // Make sure blank or null names are rejected
    @Test
    void rejectsBlankNames() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(null));
        assertThrows(IllegalArgumentException.class, () -> new Customer(" "));
        assertEquals("Sam", new Customer(" Sam ").getName());
    }
}
