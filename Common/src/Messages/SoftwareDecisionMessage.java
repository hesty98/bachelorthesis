package Messages;

public class SoftwareDecisionMessage extends DecisionMessage {

    private String softwareID;

    public SoftwareDecisionMessage(long inquiryID, boolean accepted, String softwareID) {
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
