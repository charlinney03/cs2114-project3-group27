package coffeeshop;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class MenuItemTest {
    @Test void storesItemAndPrice() {
        MenuItem item = new MenuItem("Latte",4.50);
        assertEquals("Latte",item.getName());
        assertEquals(4.50,item.getPrice(),0.001);
        assertEquals(0,new MenuItem("Water",0).getPrice(),0.001);
    }
    @Test void rejectsInvalidItems() {
        assertThrows(IllegalArgumentException.class,()->new MenuItem(null,1));
        assertThrows(IllegalArgumentException.class,()->new MenuItem(" ",1));
        assertThrows(IllegalArgumentException.class,()->new MenuItem("Latte",-1));
        assertThrows(IllegalArgumentException.class,()->new MenuItem("Latte",Double.NaN));
        assertThrows(IllegalArgumentException.class,()->new MenuItem("Latte",Double.POSITIVE_INFINITY));
    }
}
