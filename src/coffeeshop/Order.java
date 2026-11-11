package coffeeshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Order {
    private static int nextId = 1;
    private final int orderId;
    private final Customer customer;
    private final List<MenuItem> items;
    private String status;

    public Order(Customer cust) {
        if (cust == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        customer = cust;
        items = new ArrayList<>();
        status = "WAITING";
        orderId = nextId++;
    }

    public void addItem(MenuItem item) {
        // Only waiting orders can still be changed
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (!"WAITING".equals(status)) {
            throw new IllegalStateException("Finished orders cannot be edited");
        }
        items.add(item);
    }

    public List<MenuItem> getOrder() {
        return Collections.unmodifiableList(items);
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    // The queue is the only class that should finish or cancel an order
    void setStatus(String newStatus) {
        if (!"WAITING".equals(status)
                || !("COMPLETED".equals(newStatus) || "CANCELLED".equals(newStatus))) {
            throw new IllegalStateException("Invalid order status change");
        }
        status = newStatus;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(orderId);
        sb.append("\nCustomer: ").append(customer.getName());
        sb.append("\nItems:");
        double total = 0;
        for (MenuItem item : items) {
            sb.append("\n- ").append(item.getName());
            total += item.getPrice();
        }
        sb.append(String.format(Locale.US, "\nTotal: $%.2f", total));
        sb.append("\nStatus: ").append(status);
        return sb.toString();
    }
}
