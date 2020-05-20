package Messages;

import EnvironmentObjects.Provider;
import EnvironmentObjects.Software.Software;

public class SoftwareInstallationPackage extends SoftwareMessage {
    private Software software;

    public SoftwareInstallationPackage(String softwareID, Software software, Provider provider) {
        super(softwareID, provider);
        this.software=software;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }
}
