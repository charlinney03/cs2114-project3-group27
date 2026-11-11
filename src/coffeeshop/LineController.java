package coffeeshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class LineController {
    private final CoffeeQueue line;
    private final CusView cview;
    private final BarView bview;
    private final List<MenuItem> menu;

    public LineController(CoffeeQueue line, CusView cview, BarView bview, List<MenuItem> menu) {
        if (line == null || cview == null || bview == null || menu == null || menu.contains(null)) {
            throw new IllegalArgumentException("Missing queue, view, or menu");
        }
        this.line = line;
        this.cview = cview;
        this.bview = bview;
        this.menu = Collections.unmodifiableList(new ArrayList<>(menu));
        updateViews();
    }

    public int addOrder(String customerName, List<String> itemNames) {
        // Check everything before adding anything to the queue
        if (customerName == null || customerName.trim().isEmpty()
                || itemNames == null || itemNames.isEmpty()) {
            return -1;
        }
        List<MenuItem> selected = new ArrayList<>();
        for (String itemName : itemNames) {
            if (itemName == null || itemName.trim().isEmpty()) {
                return -1;
            }
            MenuItem match = null;
            for (MenuItem item : menu) {
                if (item.getName().equalsIgnoreCase(itemName.trim())) {
                    match = item;
                    break;
                }
            }
            if (match == null) {
                return -1;
            }
            selected.add(match);
        }
        Order order = new Order(new Customer(customerName));
        for (MenuItem item : selected) {
            order.addItem(item);
        }
        line.enqueue(order);
        updateViews();
        return order.getOrderId();
    }

    public String complete() {
        if (line.isEmpty()) {
            updateViews();
            return null;
        }
        Order order = line.dequeue();
        updateViews();
        return order.describe();
    }

    public boolean cancelOrder(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return false;
        }
        boolean result = line.cancelOrder(new Customer(customerName));
        updateViews();
        return result;
    }

    public int findPosition(int orderId) {
        try {
            return line.findPosition(line.searchOrder(orderId));
        }
        catch (NoSuchElementException ex) {
            return -1;
        }
    }

    public String searchOrder(int orderId) {
        try {
            return line.searchOrder(orderId).describe();
        }
        catch (NoSuchElementException ex) {
            return null;
        }
    }

    public String getMenuText() {
        StringBuilder text = new StringBuilder("Menu:");
        for (MenuItem item : menu) {
            text.append(String.format(java.util.Locale.US, "\n%s - $%.2f", item.getName(), item.getPrice()));
        }
        return text.toString();
    }

    private void updateViews() {
        // Only the controller talks to both views
        StringBuilder customerText = new StringBuilder("Orders waiting: ").append(line.size());
        StringBuilder baristaText = new StringBuilder("Barista line: ").append(line.size()).append(" waiting");
        if (!line.isEmpty()) {
            Order next = line.peek();
            customerText.append("\nNext order: #").append(next.getOrderId());
            baristaText.append("\nNext to make:\n").append(next.describe());
        }
        else {
            baristaText.append("\nNo orders waiting");
        }
        cview.update(customerText.toString());
        bview.update(baristaText.toString());
    }
}
