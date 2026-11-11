package coffeeshop;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CoffeeQueue {
    private Node head;
    private Node tail;
    private int size;
    private final List<Order> completed;
    private final List<Order> cancelled;

    private static class Node {
        private final Order order;
        private Node next;

        private Node(Order order) {
            this.order = order;
        }
    }

    public CoffeeQueue() {
        completed = new ArrayList<>();
        cancelled = new ArrayList<>();
    }

    public void enqueue(Order order) {
        // Keep the same order from being added twice
        if (order == null || !"WAITING".equals(order.getStatus()) || containsOrder(order)) {
            throw new IllegalArgumentException("Order must be new and waiting");
        }
        Node node = new Node(order);
        if (isEmpty()) {
            head = node;
            tail = node;
        }
        else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    private boolean containsOrder(Order order) {
        Node current = head;
        while (current != null) {
            if (current.order == order) {
                return true;
            }
            current = current.next;
        }
        return completed.contains(order) || cancelled.contains(order);
    }

    public Order dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("No waiting orders");
        }
        Order removed = head.order;
        head = head.next;
        size--;
        if (head == null) {
            tail = null;
        }
        removed.setStatus("COMPLETED");
        completed.add(removed);
        return removed;
    }

    public Order peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("No waiting orders");
        }
        return head.order;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public boolean cancelOrder(Customer cust) {
        if (cust == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current.order.getCustomer().equals(cust)) {
                // Connect the nodes again after removing this order
                if (previous == null) {
                    head = current.next;
                }
                else {
                    previous.next = current.next;
                }
                if (current == tail) {
                    tail = previous;
                }
                current.order.setStatus("CANCELLED");
                cancelled.add(current.order);
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public int findPosition(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        Node current = head;
        int position = 1;
        while (current != null) {
            if (current.order == order) {
                return position;
            }
            current = current.next;
            position++;
        }
        return -1;
    }

    public Order searchOrder(int orderId) {
        Node current = head;
        while (current != null) {
            if (current.order.getOrderId() == orderId) {
                return current.order;
            }
            current = current.next;
        }
        for (Order order : completed) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        for (Order order : cancelled) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        throw new NoSuchElementException("Order not found");
    }
}
