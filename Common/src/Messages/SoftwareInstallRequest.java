package Messages;

import EnvironmentObjects.Provider;

public class SoftwareInstallRequest extends SoftwareMessage {
    private String vehicleManifest;

    public SoftwareInstallRequest(String softwareID, Provider provider, String vehicleManifest) {
        super(softwareID,provider);
        this.vehicleManifest=vehicleManifest;
    }

    public String getVehicleManifest() {
        return vehicleManifest;
    }

    public void setVehicleManifest(String vehicleManifest) {
        this.vehicleManifest = vehicleManifest;
    }
}
