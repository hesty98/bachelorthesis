package Messages;

import EnvironmentObjects.ServiceProvider;

/**
 * @author Linus Hestermeyer
 *
 * Tells if driver accepted to use the inquiried service or not.
 */
public class ServiceDecisionMessage extends ServiceMessage {

    private long inquiryID;
    private boolean accepted;

    public ServiceDecisionMessage(long inquiryID, boolean accepted, ServiceProvider serviceProvider) {
        super(serviceProvider);
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
