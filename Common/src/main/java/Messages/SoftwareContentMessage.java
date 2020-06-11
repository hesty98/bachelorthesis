package Messages;

import EnvironmentObjects.Description;
import EnvironmentObjects.Provider;

import java.util.ArrayList;

public class SoftwareContentMessage extends SoftwareMessage {
    private Description desc;
    private boolean verified;
    private long inquiryID;
    private ArrayList<Provider> verifiedProviders;
    //TODO: look, what other UPTANE features can be integrated

    public SoftwareContentMessage(Description desc, boolean verified, long inquiryID, Provider provider, String reqSWID, ArrayList<Provider> verifiedProviders) {
        super(reqSWID,provider);
        this.desc = desc;
        this.verified = verified;
        this.inquiryID=inquiryID;
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

    public long getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(long inquiryID) {
        this.inquiryID = inquiryID;
    }


    public ArrayList<Provider> getVerifiedProviders() {
        return verifiedProviders;
    }

    public void setVerifiedProviders(ArrayList<Provider> verifiedProviders) {
        this.verifiedProviders = verifiedProviders;
    }
}
