package Messages;

import EnvironmentObjects.Description;
import EnvironmentObjects.Provider;

/**
 * @author Linus Hestermeyer
 *
 */
public class SoftwareRegistrationMessage extends SoftwareMessage {
    private Description description;
    private long inquiryID;

    public SoftwareRegistrationMessage(Description description, long inquiryID, String swID, Provider provider) {
        super(swID,provider);
        this.description = description;
        this.inquiryID = inquiryID;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public long getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(long inquiryID) {
        this.inquiryID = inquiryID;
    }
}
