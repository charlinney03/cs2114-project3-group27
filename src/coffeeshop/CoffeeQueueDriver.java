package coffeeshop;
public class CoffeeQueueDriver {
    public static void main(String[] args) {
        // Model-only demo that does not depend on the controller or view
        MenuItem latte = new MenuItem("Latte", 4.50);
        MenuItem mocha = new MenuItem("Mocha", 5.00);
        Customer sam = new Customer("Sam");
        Customer alex = new Customer("Alex");
        Order orderA = new Order(sam);
        orderA.addItem(latte);
        Order orderB = new Order(alex);
        orderB.addItem(mocha);
        CoffeeQueue queue = new CoffeeQueue();
        queue.enqueue(orderA);
        queue.enqueue(orderB);
        System.out.println("=== Initial Queue ===");
        System.out.println("Orders Waiting: " + queue.size());
        System.out.println("Front Order:");
        System.out.println(queue.peek().describe());
        System.out.println("\nPosition of Alex's order: " + queue.findPosition(orderB));
        System.out.println("Estimated wait for Alex: " + queue.estimateWaitMinutes(orderB.getOrderId()));
        System.out.println("\n=== Completing First Order ===");
        System.out.println(queue.dequeue().describe());
        System.out.println("\n=== Queue After Completion ===");
        System.out.println("Orders Waiting: " + queue.size());
        if (!queue.isEmpty()) System.out.println(queue.peek().describe());
        System.out.println("\n=== Cancelling Alex's Order ===");
        System.out.println("Cancelled: " + queue.cancelOrder(alex));
        System.out.println("\n=== Final Queue State ===");
        System.out.println("Queue Empty: " + queue.isEmpty());
        System.out.println("Orders Waiting: " + queue.size());
    }
}
