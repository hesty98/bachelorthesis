package Messages;

/**
 * @author Linus Hestermeyer
 *
 * Tells if driver accepted to use the inquiried service or not.
 */
public class ServiceDecisionMessage extends DecisionMessage {
    private String softwareID;

    public ServiceDecisionMessage(long inquiryID, boolean accepted, String softwareID) {
        super(inquiryID, accepted);
        this.softwareID=softwareID;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }
}
