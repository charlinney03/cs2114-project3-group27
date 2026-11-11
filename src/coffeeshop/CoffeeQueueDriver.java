package coffeeshop;

public class CoffeeQueueDriver {

    public static void main(String[] args) {

        // create menu items
        MenuItem latte = new MenuItem("Latte", 4.50);
        MenuItem mocha = new MenuItem("Mocha", 5.00);

        // create customers
        Customer sam = new Customer("Sam");
        Customer alex = new Customer("Alex");

        // create orders
        Order orderA = new Order(sam);
        orderA.addItem(latte);

        Order orderB = new Order(alex);
        orderB.addItem(mocha);

        // create queue
        CoffeeQueue queue = new CoffeeQueue();

        // add orders to queue
        queue.enqueue(orderA);
        queue.enqueue(orderB);

        System.out.println("=== Initial Queue ===");
        System.out.println("Orders Waiting: " + queue.size());
        System.out.println("Front Order:");
        System.out.println(queue.peek().describe());

        // find position of second order
        System.out.println("\nPosition of Alex's order: "
            + queue.findPosition(orderB));

        // complete first order
        System.out.println("\n=== Completing First Order ===");
        Order completed = queue.dequeue();
        System.out.println(completed.describe());

        // check queue after completion
        System.out.println("\n=== Queue After Completion ===");
        System.out.println("Orders Waiting: " + queue.size());

        if (!queue.isEmpty()) {
            System.out.println("Front Order:");
            System.out.println(queue.peek().describe());
        }

        // cancel remaining order
        System.out.println("\n=== Cancelling Alex's Order ===");
        boolean cancelled = queue.cancelOrder(alex);
        System.out.println("Cancelled: " + cancelled);

        // final queue state
        System.out.println("\n=== Final Queue State ===");
        System.out.println("Queue Empty: " + queue.isEmpty());
        System.out.println("Orders Waiting: " + queue.size());
    }
}