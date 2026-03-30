public class App {

    public static void main(String[] args) {
        System.out.println("Delivery System running...");
        try {
            while (true) {
                Thread.sleep(10000); 
            }
        } catch (InterruptedException e) {
            System.err.println("Application interrupted.");
        }
    }

    public static class Order {
        public double totalAmount;

        public Order(double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    public static class DeliverySlot {
        public boolean isBooked;

        public DeliverySlot(boolean isBooked) {
            this.isBooked = isBooked;
        }
    }

    public boolean isValid(Order order) {
        if (order == null) {
            return false;
        }
        
        // check minimum amount is 10
        return order.totalAmount >= 10.0;
    }

    public boolean validateSlot(DeliverySlot slot) {
        if (slot == null) {
            return false;
        }
        
        // check if slot is already booked
        return !slot.isBooked;
    }
}
