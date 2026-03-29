import java.time.LocalDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("Online Grocery Delivery System running...");
        try {
            while (true) {
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            System.err.println("Application interrupted.");
        }
    }

    // --- DOMAIN MODELS ---

    public static class Item {
        private String name;
        private double price;
        private int quantity;

        public Item(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }

    public static class Order {
        private String id;
        private String customerId;
        private List<Item> items;
        private double totalAmount;

        public Order(String id, String customerId, List<Item> items) {
            this.id = id;
            this.customerId = customerId;
            this.items = items;
            this.totalAmount = calculateTotal(items);
        }

        private double calculateTotal(List<Item> items) {
            double total = 0;
            if (items != null) {
                for (Item item : items) {
                    total += item.getPrice() * item.getQuantity();
                }
            }
            return total;
        }

        public String getId() { return id; }
        public String getCustomerId() { return customerId; }
        public List<Item> getItems() { return items; }
        public double getTotalAmount() { return totalAmount; }
    }

    public static class DeliverySlot {
        private String id;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean isBooked;

        public DeliverySlot(String id, LocalDateTime startTime, LocalDateTime endTime) {
            this.id = id;
            this.startTime = startTime;
            this.endTime = endTime;
            this.isBooked = false; // Initially not booked
        }

        public String getId() { return id; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public boolean isBooked() { return isBooked; }
        public void setBooked(boolean booked) { isBooked = booked; }
    }

    // --- BUSINESS LOGIC ---

    private static final double MINIMUM_ORDER_AMOUNT = 10.0;

    public boolean validateOrder(Order order) {
        if (order == null) {
            System.out.println("Order cannot be null.");
            return false;
        }
        
        if (order.getItems() == null || order.getItems().isEmpty()) {
            System.out.println("Order must contain at least one item.");
            return false;
        }
        
        if (order.getTotalAmount() < MINIMUM_ORDER_AMOUNT) {
            System.out.println("Order total must be at least $" + MINIMUM_ORDER_AMOUNT);
            return false;
        }
        
        return true;
    }

    public boolean validateAndBookSlot(DeliverySlot slot, LocalDateTime bookingTime) {
        if (slot == null) {
            System.out.println("Slot cannot be null.");
            return false;
        }
        
        if (slot.isBooked()) {
            System.out.println("Delivery slot is already booked.");
            return false;
        }
        
        if (slot.getStartTime().isBefore(bookingTime)) {
            System.out.println("Cannot book a past delivery slot.");
            return false;
        }
        
        // Slot is available and valid, we can book it
        slot.setBooked(true);
        return true;
    }
}
