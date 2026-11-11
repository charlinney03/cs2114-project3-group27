package coffeeshop;

public class MenuItem {
    private final String name;
    private final double price;

    public MenuItem(String name, double price) {
        // Make sure the menu only has valid items
        if (name == null || name.trim().isEmpty() || !Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("Invalid menu item");
        }
        this.name = name.trim();
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
