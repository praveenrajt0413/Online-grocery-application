import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testOrderAboveMinimum() {
        App app = new App();
        App.Order order = new App.Order(15.0); 
        
        boolean result = app.isValid(order);
        assertTrue(result); 
    }

    @Test
    public void testOrderBelowMinimum() {
        App app = new App();
        App.Order order = new App.Order(5.0); 
        
        boolean result = app.isValid(order);
        assertFalse(result); 
    }

    @Test
    public void testNullOrder() {
        App app = new App();
        
        boolean result = app.isValid(null);
        assertFalse(result); 
    }

    @Test
    public void testValidDeliverySlot() {
        App app = new App();
        App.DeliverySlot slot = new App.DeliverySlot(false); 
        
        boolean result = app.validateSlot(slot);
        assertTrue(result); 
    }

    @Test
    public void testBookedDeliverySlot() {
        App app = new App();
        App.DeliverySlot slot = new App.DeliverySlot(true); 
        
        boolean result = app.validateSlot(slot);
        assertFalse(result); 
    }
}
