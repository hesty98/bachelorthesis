package director_repository;

import EnvironmentObjects.Software.Software;
import Messages.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import databases.SoftwareDatabase;

/**
 * Listens on messages regarding a service handles the content.
 * @author linus hestermeyer
 */
public class Director {
    private EventBus eventBus;
    private SoftwareDatabase database;

    public Director(EventBus eventBus) {
        this.eventBus=eventBus;
    }

    /**
     * This method is needed for filling getting all the information needed for a software.
     * @param softwareContentRequest
     */
    @Subscribe
    public void incomingContentRequest (SoftwareContentRequest softwareContentRequest){
        System.err.println("Received SoftwareContentRequest. InquiryID: "+ softwareContentRequest.getInquiryID()
                + "\nProvider: "+ softwareContentRequest.getProvider().getProviderName()
        );


        if(verified(softwareContentRequest.getSoftwareID())){
            Software sw = SoftwareDatabase.getInstance().getSoftwareByKey(softwareContentRequest.getSoftwareID());

            SoftwareContentMessage msg = new SoftwareContentMessage(
                    sw.getDescription(),
                    true,
                    softwareContentRequest.getInquiryID(),
                    sw.getProvider(),
                    sw.getSoftwareID(),
                    sw.getVerifiedProviders()
            );
            softwareContentRequest.getCtx().writeAndFlush(msg);
        }

    }

    @Subscribe
    public void handleSWInstallRequest(SoftwareInstallRequest softwareInstallRequest){
        String manifest ="";
        try{
            manifest =InventoryDatabase.getInstance().getManifestByKey(InventoryDatabase.CAR_1_ID);

            // Verification as described here: https://uptane.github.io/papers/uptane-standard.1.0.1.html#directing-installation-of-images-on-vehicles
            if(!manifest.equals(softwareInstallRequest.getVehicleManifest())){
                prepareSoftware(softwareInstallRequest, false);
            }else{
                prepareSoftware(softwareInstallRequest, true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void prepareSoftware(SoftwareInstallRequest softwareInstallRequest, boolean verified) {
        try {
            //Todo: Software verifizieren
            //Load the required Software from Database
            Software required = SoftwareDatabase.getInstance().getSoftwareByKey(softwareInstallRequest.getSoftwareID());

            String manifest = softwareInstallRequest.getVehicleManifest();
            if(verified) {
                manifest += manifest + "  " + required.getSoftwareID() + ": " + required.getVersion() + "\r\n";
            }
            SoftwareInstallationPackage pkg = new SoftwareInstallationPackage(required.getSoftwareID(), required, required.getProvider(), manifest);
            softwareInstallRequest.getCtx().writeAndFlush(pkg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleSWSearchRequest(SoftwareSearchRequest request){
        /*
        Todo.
          SUPR.findBestSoftware(request)
         */
    }

    private boolean verified(String requiredSWID) {
        Software sw = SoftwareDatabase.getInstance().getSoftwareByKey(requiredSWID);
        if(sw != null)
            return true;
        return false;
    }

}
