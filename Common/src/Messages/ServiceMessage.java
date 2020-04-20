package Messages;

public class ServiceMessage extends Message {
    /**
     * Gets used from the Software so that this knows what to do.
     */
    private String requiredSWID;
    private String serviceID;

    public ServiceMessage(String requiredSWID, String serviceID) {
        this.requiredSWID = requiredSWID;
        this.serviceID = serviceID;
    }

    public String getRequiredSWID() {
        return requiredSWID;
    }

    public void setRequiredSWID(String requiredSWID) {
        this.requiredSWID = requiredSWID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
