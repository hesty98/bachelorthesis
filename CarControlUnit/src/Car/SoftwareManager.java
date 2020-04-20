package Car;

import EnvironmentObjects.Software.Software;
import Initialization.Network.CarlaClientConnection;
import Initialization.Network.MMSClientConnection;
import Messages.ServiceRegistrationMessage;
import com.google.inject.Inject;

import java.util.ArrayList;

/**
 * Every Car has a SoftwareManager. the Softwaremanager forwards incoming messages to the specific Software which is needed to handle these Messages. It also prepares the installation of new Software.
 */
public class SoftwareManager {

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
        MessageHandler.getInstance().sendToMMS(msg);
    }


    public void installSoftware(Software software) {
        installedSW.add(software);
    }

    public void uninstallSoftware(Software software){
        installedSW.remove(software);
    }
}
