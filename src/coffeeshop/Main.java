package coffeeshop;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Set up the menu and connect the two views to the controller
        CoffeeQueue line = new CoffeeQueue();
        CusView customerView = new CusView();
        BarView baristaView = new BarView();
        LineController controller = new LineController(line, customerView, baristaView,
                Arrays.asList(new MenuItem("Latte", 4.50), new MenuItem("Mocha", 5.00),
                        new MenuItem("Tea", 3.00)));
        customerView.setController(controller);
        baristaView.setController(controller);
        new SimpleView().run(controller, customerView, baristaView,
                new Scanner(System.in), System.out);
    }
}
