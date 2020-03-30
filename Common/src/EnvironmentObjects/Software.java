package EnvironmentObjects;

public abstract class Software implements ISoftware {
    private String softwareID;
    private String swName;
    private String swDescription;
    private String swProviderID;

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
}
