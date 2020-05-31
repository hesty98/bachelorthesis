package Messages;

import EnvironmentObjects.Provider;
import EnvironmentObjects.Software.Software;

public class SoftwareInstallationPackage extends SoftwareMessage {
    private Software software;
    private String updatedManifest;

    public SoftwareInstallationPackage(String softwareID, Software software, Provider provider,String updatedManifest){
        super(softwareID, provider);
        this.software=software;
        this.updatedManifest=updatedManifest;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public String getUpdatedManifest() {
        return updatedManifest;
    }

    public void setUpdatedManifest(String updatedManifest) {
        this.updatedManifest = updatedManifest;
    }
}
