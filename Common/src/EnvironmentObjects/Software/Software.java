package EnvironmentObjects.Software;

import EnvironmentObjects.IConnectionClient;

public abstract class Software implements ISoftware {
    private String softwareID;
    private String swName;
    private String swDescription;
    private String swProviderID;
    private IConnectionClient mmsConnection;
    private IConnectionClient carlaConnection;
    private IConnectionClient swsConnection;

    public Software(String softwareID, String swName, String swDescription, String swProviderID) {
        this.softwareID = softwareID;
        this.swName = swName;
        this.swDescription = swDescription;
        this.swProviderID = swProviderID;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }

    public String getSwName() {
        return swName;
    }

    public void setSwName(String swName) {
        this.swName = swName;
    }

    public String getSwDescription() {
        return swDescription;
    }

    public void setSwDescription(String swDescription) {
        this.swDescription = swDescription;
    }

    public String getSwProviderID() {
        return swProviderID;
    }

    public void setSwProviderID(String swProviderID) {
        this.swProviderID = swProviderID;
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
}
