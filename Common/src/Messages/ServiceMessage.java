package Messages;

public class ServiceMessage extends Message {
    private String serviceSoftwareID;

    public ServiceMessage(String serviceSoftwareID) {
        this.serviceSoftwareID = serviceSoftwareID;
    }

    public String getServiceSoftwareID() {
        return serviceSoftwareID;
    }

    public void setServiceSoftwareID(String serviceSoftwareID) {
        this.serviceSoftwareID = serviceSoftwareID;
    }
}
