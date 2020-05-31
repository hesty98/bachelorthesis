package Messages;

import EnvironmentObjects.Provider;

public class SoftwareDecisionMessage extends SoftwareMessage {

    private long inquiryID;
    private boolean accepted;

    public SoftwareDecisionMessage(long inquiryID, boolean accepted, String softwareID, Provider provider) {
        super(softwareID,provider);
        this.inquiryID=inquiryID;
        this.accepted=accepted;

    }

    public long getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(long inquiryID) {
        this.inquiryID = inquiryID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
