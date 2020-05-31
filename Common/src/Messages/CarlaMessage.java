package Messages;

public class CarlaMessage extends Message {
    private int action;

    public CarlaMessage(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
