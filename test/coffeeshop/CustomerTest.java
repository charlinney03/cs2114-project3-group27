package coffeeshop;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Arrays;
public class CustomerTest {
    @Test void savesNameAndMatchesSameName() {
        Customer a = new Customer("Sam"), b = new Customer("Sam");
        assertEquals("Sam",a.getName());
        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertEquals(1,new HashSet<>(Arrays.asList(a,b)).size());
        assertNotEquals(a,new Customer("sam"));
        assertNotEquals(a,null);
        assertNotEquals(a,"Sam");
    }
    @Test void rejectsBlankNames() {
        assertThrows(IllegalArgumentException.class,()->new Customer(null));
        assertThrows(IllegalArgumentException.class,()->new Customer(" "));
        assertEquals("Sam",new Customer(" Sam ").getName());
    }
}
