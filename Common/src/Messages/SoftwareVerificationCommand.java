package Messages;

import EnvironmentObjects.Description;
import EnvironmentObjects.Provider;

public class SoftwareVerificationCommand extends SoftwareMessage{
    private String car_manifest;
    private Description description;
    private long inquiryID;

    /**
     *
     * @param car_manifest
     * @param description
     * @param provider Is a ServiceProvider
     * @param inquiryID
     * @param requiredSWID
     */
    public SoftwareVerificationCommand(String car_manifest, Description description, Provider provider, long inquiryID, String requiredSWID) {
        super(requiredSWID, provider);
        this.car_manifest = car_manifest;
        this.description = description;
        this.inquiryID=inquiryID;
    }

    public String getCar_manifest() {
        return car_manifest;
    }

    public void setCar_manifest(String car_manifest) {
        this.car_manifest = car_manifest;
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
