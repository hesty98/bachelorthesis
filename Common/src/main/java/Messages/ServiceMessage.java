package Messages;

import EnvironmentObjects.Provider;

public class ServiceMessage extends Message {
    /**
     * Gets used from the Software so that this knows what to do.
     */
    private Provider provider;
    private String requiredSWID;

    public ServiceMessage(Provider provider, String requiredSWID) {
        this.provider = provider;
        this.requiredSWID=requiredSWID;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getRequiredSWID() {
        return requiredSWID;
    }

    public void setRequiredSWID(String requiredSWID) {
        this.requiredSWID = requiredSWID;
    }
}
