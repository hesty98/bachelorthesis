package Messages;

import EnvironmentObjects.Service;

/**
 * @author Linus Hestermeyer
 *
 * Message, which is sent if the driver unsubscribes the service.
 */
public class ServiceDeleteMessage extends ServiceMessage{
    private long carID;
    private Service service;

    public ServiceDeleteMessage(long carID, Service service, String serviceSoftwareID) {
        super(serviceSoftwareID);
        this.carID = carID;
        this.service = service;
    }

    public long getCarID() {
        return carID;
    }

    public void setCarID(long carID) {
        this.carID = carID;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
