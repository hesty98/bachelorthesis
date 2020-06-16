package Messages;

import EnvironmentObjects.Provider;

public class SoftwareMessage extends Message {
    private String softwareID;
    private Provider provider;

    public SoftwareMessage(String softwareID,Provider provider) {
        this.softwareID = softwareID;
        this.provider=provider;
    }

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
