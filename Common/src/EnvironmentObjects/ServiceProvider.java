package EnvironmentObjects;

import java.io.Serializable;

public class ServiceProvider implements Serializable {
    private String publicProviderID;
    private String providerName;
    private String providerURL;
    private String requiredSoftwareID;

    public ServiceProvider(String publicProviderID, String providerName, String providerURL, String requiredSoftwareID) {
        this.publicProviderID = publicProviderID;
        this.providerName = providerName;
        this.providerURL = providerURL;
        this.requiredSoftwareID = requiredSoftwareID;
    }

    public String getRequiredSoftwareID() {
        return requiredSoftwareID;
    }

    public void setRequiredSoftwareID(String requiredSoftwareID) {
        this.requiredSoftwareID = requiredSoftwareID;
    }

    public String getPublicProviderID() {
        return publicProviderID;
    }

    public void setPublicProviderID(String publicProviderID) {
        this.publicProviderID = publicProviderID;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderURL() {
        return providerURL;
    }

    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }
}
