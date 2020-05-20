package Messages;

import EnvironmentObjects.Provider;

public class SoftwareInstallRequest extends SoftwareMessage {
    public SoftwareInstallRequest(String softwareID, Provider provider) {
        super(softwareID,provider);
    }
}
