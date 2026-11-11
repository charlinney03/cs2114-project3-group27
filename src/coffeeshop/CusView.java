package coffeeshop;

public class CusView {
    private LineController controller;
    private String lastShown = "";

    public void setController(LineController controller) {
        if (controller == null || this.controller != null) {
            throw new IllegalArgumentException("Controller must be set once");
        }
        this.controller = controller;
    }

    public void update(String info) {
        // The view only gets text from the controller
        lastShown = info == null ? "" : info;
    }

    public String getInfo() {
        return lastShown;
    }
}
