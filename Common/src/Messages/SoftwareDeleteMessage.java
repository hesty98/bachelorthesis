package Messages;

import EnvironmentObjects.Service;

/**
 * @author Linus Hestermeyer
 *
 * Message, which is sent if the driver unsubscribes the service.
 */
public class ServiceDeleteMessage extends Message{
    private long carID;
    private String softwareID;

    public ServiceDeleteMessage(long carID, String softwareID) {
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

    public void setSoftwareID(Service softwareID) {
        this.softwareID = softwareID;
    }
}
