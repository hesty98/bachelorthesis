package EnvironmentObjects.Software;

import EnvironmentObjects.IConnectionClient;
import EnvironmentObjects.Description;

public abstract class Software implements ISoftware {
    private String softwareID;
    private String providerID;
    private Description description;
    private IConnectionClient mmsConnection;
    private IConnectionClient carlaConnection;
    private IConnectionClient swsConnection;

    public Software(String softwareID,String providerID, Description description) {
        this.softwareID = softwareID;
        this.description= description;
        this.providerID=providerID;
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

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }
}
