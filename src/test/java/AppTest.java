import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testOrderAboveMinimum() {
        App app = new App();
        App.Order order = new App.Order(15.0); // $15 is greater than $10
        
        boolean result = app.isValid(order);
        
        assertTrue(result); // We expect this to be true
    }

    @Test
    public void testOrderBelowMinimum() {
        App app = new App();
        App.Order order = new App.Order(5.0); // $5 is less than $10
        
        boolean result = app.isValid(order);
        
        assertFalse(result); // We expect this to be false
    }

    @Test
    public void testNullOrder() {
        App app = new App();
        
        boolean result = app.isValid(null);
        
        assertFalse(result); // We expect this to safely return false
    }
}
