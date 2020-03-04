package EnvironmentObjects;

import java.io.Serializable;

/**
 * @author Linus Hestermeyer
 *
 * Wrapper for a ServiceTypeID and the ServiceProvider.
 */
public class Service implements Serializable {
    private String serviceTypeID;
    private String serviceProviderID;

    public Service(String serviceTypeID, String serviceProviderID) {
        this.serviceTypeID = serviceTypeID;
        this.serviceProviderID = serviceProviderID;
    }

    public String getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(String serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getServiceProviderID() {
        return serviceProviderID;
    }

    public void setServiceProviderID(String serviceProviderID) {
        this.serviceProviderID = serviceProviderID;
    }
}
