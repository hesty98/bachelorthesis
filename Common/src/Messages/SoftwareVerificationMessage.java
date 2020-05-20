package Messages;

import EnvironmentObjects.Description;
import EnvironmentObjects.Provider;

import java.util.ArrayList;

public class SoftwareVerificationMessage extends SoftwareMessage {
    private Description desc;
    private boolean verified;
    private String updated_car_manifest;
    private long inquiryID;
    private ArrayList<Provider> verifiedProviders;
    //TODO: look, what other UPTANE features can be integrated

    public SoftwareVerificationMessage(Description desc, boolean verified, String updated_car_manifest, long inquiryID, Provider provider, String reqSWID, ArrayList<Provider> verifiedProviders) {
        super(reqSWID,provider);
        this.desc = desc;
        this.verified = verified;
        this.inquiryID=inquiryID;
        this.updated_car_manifest=updated_car_manifest;
        this.verifiedProviders=verifiedProviders;
    }

    public Description getDesc() {
        return desc;
    }

    public void setDesc(Description desc) {
        this.desc = desc;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setUpdated_car_manifest(String updated_car_manifest) {
        this.updated_car_manifest = updated_car_manifest;
    }

    public long getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(long inquiryID) {
        this.inquiryID = inquiryID;
    }

    public String getUpdated_car_manifest() {
        return updated_car_manifest;
    }

    public ArrayList<Provider> getVerifiedProviders() {
        return verifiedProviders;
    }

    public void setVerifiedProviders(ArrayList<Provider> verifiedProviders) {
        this.verifiedProviders = verifiedProviders;
    }
}
