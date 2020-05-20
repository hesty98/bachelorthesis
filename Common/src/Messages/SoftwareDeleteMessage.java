package Messages;


import EnvironmentObjects.Provider;

/**
 * @author Linus Hestermeyer
 *
 * Message, which is sent if the driver unsubscribes the service.
 */
public class SoftwareDeleteMessage extends SoftwareMessage{
    private long carID;

    public SoftwareDeleteMessage(long carID, String softwareID, Provider provider) {
        super(softwareID,provider);
        this.carID = carID;
    }

    public long getCarID() {
        return carID;
    }

    public void setCarID(long carID) {
        this.carID = carID;
    }

}
