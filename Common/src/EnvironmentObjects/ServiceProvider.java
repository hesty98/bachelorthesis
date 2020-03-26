package EnvironmentObjects;

import java.io.Serializable;

public class ServiceProvider implements Serializable {
    private String publicProviderID;
    private String providerName;
    private String providerURL;

    public ServiceProvider(String publicProviderID, String providerName, String providerURL) {
        this.publicProviderID = publicProviderID;
        this.providerName = providerName;
        this.providerURL = providerURL;
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
