package Messages;

/**
 * @author Linus Hestermeyer
 *
 * Message, which is sent if the driver unsubscribes the service.
 */
public class SoftwareDeleteMessage extends Message{
    private long carID;
    private String softwareID;

    public SoftwareDeleteMessage(long carID, String softwareID) {
        this.carID = carID;
        this.softwareID = softwareID;
    }

    public long getCarID() {
        return carID;
    }

    public void setCarID(long carID) {
        this.carID = carID;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }
}
