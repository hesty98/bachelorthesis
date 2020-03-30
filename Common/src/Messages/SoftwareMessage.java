package Messages;

public class SoftwareMessage extends Message {
    private String softwareID;

    public SoftwareMessage(String softwareID) {
        this.softwareID = softwareID;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }
}
