package coffeeshop;

public class BarView {
    private LineController controller;
    private String lastShown = "";

    public void setController(LineController controller) {
        if (controller == null || this.controller != null) {
            throw new IllegalArgumentException("Controller must be set once");
        }
        this.controller = controller;
    }

    public void update(String info) {
        // The barista only sees what the controller sends
        lastShown = info == null ? "" : info;
    }

    public String getInfo() {
        return lastShown;
    }
}
