package Messages;

public class ServiceMessage extends Message {
    /**
     * Gets used from the Software so that this knows what to do.
     */
    private String serviceID;

    public ServiceMessage(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
