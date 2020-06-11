package EnvironmentObjects.Software;

import EnvironmentObjects.IConnectionClient;
import EnvironmentObjects.Description;
import EnvironmentObjects.Provider;

import java.util.ArrayList;

public abstract class Software implements ISoftware {
    private String softwareID;
    private Provider provider;
    private Description description;
    private IConnectionClient mmsConnection;
    private IConnectionClient carlaConnection;
    private IConnectionClient swsConnection;
    private ArrayList<Provider> verifiedProviders;
    private int version;

    public Software(String softwareID,Provider provider, Description description, ArrayList<Provider> verifiedProviders, int version) {
        this.softwareID = softwareID;
        this.description= description;
        this.provider=provider;
        this.verifiedProviders=verifiedProviders;
        this.version=version;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public IConnectionClient getMmsConnection() {
        return mmsConnection;
    }

    public void setMmsConnection(IConnectionClient mmsConnection) {
        this.mmsConnection = mmsConnection;
    }

    public IConnectionClient getCarlaConnection() {
        return carlaConnection;
    }

    public void setCarlaConnection(IConnectionClient carlaConnection) {
        this.carlaConnection = carlaConnection;
    }

    public IConnectionClient getSwsConnection() {
        return swsConnection;
    }

    public void setSwsConnection(IConnectionClient swsConnection) {
        this.swsConnection = swsConnection;
    }
    public boolean isUpTpDate(){
        return true;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public ArrayList<Provider> getVerifiedProviders() {
        return verifiedProviders;
    }

    public void setVerifiedProviders(ArrayList<Provider> verifiedProviders) {
        this.verifiedProviders = verifiedProviders;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
