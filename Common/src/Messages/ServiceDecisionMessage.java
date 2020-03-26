package Messages;

/**
 * @author Linus Hestermeyer
 *
 * Tells if driver accepted to use the inquiried service or not.
 */
public class ServiceDecisionMessage extends DecisionMessage {
    private String softwareID;
    private String serviceID;

    public ServiceDecisionMessage(long inquiryID, boolean accepted, String softwareID, String serviceID) {
        super(inquiryID, accepted);
        this.softwareID=softwareID;
        this.serviceID=serviceID;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
