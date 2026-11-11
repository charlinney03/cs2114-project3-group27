package coffeeshop;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleView {
    public void display(String message) {
        System.out.println(message);
    }

    public void run(LineController controller, CusView customerView,
                    BarView baristaView, Scanner input, PrintStream out) {
        if (controller == null || customerView == null || baristaView == null
                || input == null || out == null) {
            throw new IllegalArgumentException("Missing console input or controller");
        }
        while (true) {
            out.println("\n1 Place order | 2 Complete next | 3 Cancel | 4 Position | 5 Search | 6 Menu | 7 Line | 0 Exit");
            if (!input.hasNextLine()) {
                return;
            }
            String choice = input.nextLine().trim();
            switch (choice) {
                case "1":
                    out.println("Customer name:");
                    if (!input.hasNextLine()) return;
                    String name = input.nextLine();
                    out.println("Items separated by commas:");
                    if (!input.hasNextLine()) return;
                    String itemsText = input.nextLine();
                    List<String> items = new ArrayList<>();
                    for (String part : itemsText.split(",", -1)) {
                        items.add(part.trim());
                    }
                    int id = controller.addOrder(name, items);
                    out.println(id == -1 ? "Invalid name or menu items, please try again" : "Order placed: #" + id);
                    break;
                case "2":
                    String completed = controller.complete();
                    out.println(completed == null ? "No orders waiting" : completed);
                    break;
                case "3":
                    out.println("Customer name to cancel:");
                    if (!input.hasNextLine()) return;
                    out.println(controller.cancelOrder(input.nextLine()) ? "Order cancelled" : "No waiting order found");
                    break;
                case "4":
                    out.println("Order ID:");
                    if (!input.hasNextLine()) return;
                    try {
                        int position = controller.findPosition(Integer.parseInt(input.nextLine().trim()));
                        out.println(position == -1 ? "Order is not waiting" : "Position: " + position);
                    }
                    catch (NumberFormatException ex) {
                        out.println("Please enter a number");
                    }
                    break;
                case "5":
                    out.println("Order ID:");
                    if (!input.hasNextLine()) return;
                    try {
                        String result = controller.searchOrder(Integer.parseInt(input.nextLine().trim()));
                        out.println(result == null ? "Order not found" : result);
                    }
                    catch (NumberFormatException ex) {
                        out.println("Please enter a number");
                    }
                    break;
                case "6":
                    out.println(controller.getMenuText());
                    break;
                case "7":
                    out.println(customerView.getInfo());
                    out.println(baristaView.getInfo());
                    break;
                case "0":
                    out.println("Goodbye");
                    return;
                default:
                    out.println("Please choose one of the menu options");
            }
        }
    }
}
