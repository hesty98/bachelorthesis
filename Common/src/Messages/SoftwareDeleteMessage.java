package Messages;


/**
 * @author Linus Hestermeyer
 *
 * Message, which is sent if the driver unsubscribes the service.
 */
public class SoftwareDeleteMessage extends SoftwareMessage{
    private long carID;

    public SoftwareDeleteMessage(long carID, String softwareID) {
        super(softwareID);
        this.carID = carID;
    }

    public long getCarID() {
        return carID;
    }

    public void setCarID(long carID) {
        this.carID = carID;
    }

}
