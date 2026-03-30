public class App {

    public static void main(String[] args) {
        System.out.println("Delivery System running...");
        try {
            while (true) {
                Thread.sleep(10000); // Keeps the app running for Docker
            }
        } catch (InterruptedException e) {
            System.err.println("Application interrupted.");
        }
    }

    // --- DOMAIN MODEL (The Noun) ---
    // We only keep one simple object: an Order with just an amount.
    public static class Order {
        public double totalAmount;

        public Order(double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    // --- BUSINESS LOGIC (The Verb) ---
    // We only have one simple rule: an order must be at least $10.0
    public boolean isValid(Order order) {
        if (order == null) {
            return false;
        }
        
        if (order.totalAmount >= 10.0) {
            return true;
        } else {
            return false;
        }
    }
}
