package Car;

import EnvironmentObjects.Software;
import Initialization.CarlaConnection.CarlaClientConnection;
import Initialization.EventBus.CarlaPortModule;
import Initialization.EventBus.MMSPortModule;
import Initialization.MMSConnection.MMSClientConnection;
import Messages.ServiceRegistrationMessage;
import com.google.inject.Inject;

import java.util.ArrayList;

/**
 * Every Car has a SoftwareManager. the Softwaremanager forwards incoming messages to the specific Software which is needed to handle these Messages. It also prepares the installation of new Software.
 */
public class SoftwareManager {
    @Inject
    private CarlaClientConnection carlaClientConnection;
    @Inject
    private MMSClientConnection mmsClientConnection;

    private static ArrayList<Software> installedSW = new ArrayList<>();

    /**
     * Method to get a Software from the SoftwareManager by SoftwareID.
     *
     * @param serviceSoftwareID the id
     * @return the software requested by ID
     */
    public Software getSoftware(String serviceSoftwareID) {
        for(Software sw : installedSW){
            if(sw.getSoftwareID()==serviceSoftwareID){
                return sw;
            }
        }
        return null;
    }

    /**
     * Called if a incoming Service requires a software which is not installed. This method passes Softwware-based
     * information to the MMS.
     *
     * @param msg
     */
    public void submitSoftware(ServiceRegistrationMessage msg) {
        msg.setInstallSW(true);
        mmsClientConnection.sendMessage(msg);
    }


    public void installSoftware(Software software) {
        //eigentlich müsste hier ein installSkript durchgeführt werden. Aber
        installedSW.add(software);
    }

    public void uninstallSoftware(Software software){
        installedSW.remove(software);
    }
}
