package Messages;

import EnvironmentObjects.Description;
import EnvironmentObjects.Provider;

public class SoftwareContentRequest extends SoftwareMessage{
    private Description description;
    private long inquiryID;

    /**
     *
     * @param description
     * @param provider Is a ServiceProvider
     * @param inquiryID
     * @param requiredSWID
     */
    public SoftwareContentRequest(Description description, Provider provider, long inquiryID, String requiredSWID) {
        super(requiredSWID, provider);
        this.description = description;
        this.inquiryID=inquiryID;
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
