import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private App app;

    @BeforeEach
    public void setUp() {
        app = new App();
    }

    // --- ORDER VALIDATION TESTS ---

    @Test
    public void testValidOrder() {
        List<App.Item> items = new ArrayList<>();
        items.add(new App.Item("Apples", 2.50, 4)); // Total 10.0
        App.Order order = new App.Order("O1", "C1", items);
        
        assertTrue(app.validateOrder(order));
    }

    @Test
    public void testOrderBelowMinimumAmount() {
        List<App.Item> items = new ArrayList<>();
        items.add(new App.Item("Bananas", 1.50, 2)); // Total 3.0
        App.Order order = new App.Order("O2", "C1", items);
        
        assertFalse(app.validateOrder(order));
    }

    @Test
    public void testOrderWithNoItems() {
        App.Order order = new App.Order("O3", "C1", Collections.emptyList());
        
        assertFalse(app.validateOrder(order));
    }

    @Test
    public void testNullOrder() {
        assertFalse(app.validateOrder(null));
    }

    // --- DELIVERY SLOT VALIDATION TESTS ---

    @Test
    public void testValidAvailableSlot() {
        LocalDateTime now = LocalDateTime.now();
        App.DeliverySlot slot = new App.DeliverySlot("S1", now.plusHours(2), now.plusHours(4));
        
        assertTrue(app.validateAndBookSlot(slot, now));
        assertTrue(slot.isBooked());
    }

    @Test
    public void testAlreadyBookedSlot() {
        LocalDateTime now = LocalDateTime.now();
        App.DeliverySlot slot = new App.DeliverySlot("S2", now.plusHours(2), now.plusHours(4));
        slot.setBooked(true);
        
        assertFalse(app.validateAndBookSlot(slot, now));
    }

    @Test
    public void testPastSlotBooking() {
        LocalDateTime now = LocalDateTime.now();
        App.DeliverySlot slot = new App.DeliverySlot("S3", now.minusHours(4), now.minusHours(2));
        
        assertFalse(app.validateAndBookSlot(slot, now));
    }

    @Test
    public void testNullSlot() {
        assertFalse(app.validateAndBookSlot(null, LocalDateTime.now()));
    }
}
