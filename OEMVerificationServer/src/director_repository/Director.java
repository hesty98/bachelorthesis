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
        System.err.println("Anfrage für Softwarebeschreibung erhalten, InquiryID: "+ softwareContentRequest.getInquiryID()
                + "\n   Service Provider: "+ softwareContentRequest.getProvider().getProviderName()
        );
        System.err.println("");
        SUPR.determineIfSoftwareIsNeeded();
        System.err.println("");
        if(exists(softwareContentRequest.getSoftwareID())){
            Software sw = SoftwareDatabase.getInstance().getSoftwareByKey(softwareContentRequest.getSoftwareID());

            SoftwareContentMessage msg = new SoftwareContentMessage(
                    sw.getDescription(),
                    true,
                    softwareContentRequest.getInquiryID(),
                    sw.getProvider(),
                    sw.getSoftwareID(),
                    sw.getVerifiedServiceProviders()    
            );
            System.err.println("Beschreibung wurde erstellt.");
            System.err.println("Beschreibung wird verschickt!");
            softwareContentRequest.getCtx().writeAndFlush(msg);
        }

    }

    @Subscribe
    public void handleSWInstallRequest(SoftwareInstallRequest softwareInstallRequest){
        System.err.println("Installationsanfrage für Software erhalten."
                + "\n   Software_ID: "+ softwareInstallRequest.getSoftwareID()
        );

        String manifest ="";
        try{
            manifest =InventoryDatabase.getInstance().getManifestByKey(InventoryDatabase.CAR_1_ID);

            System.err.println("Vom Fahrzeug geschicktes Manifest: \n"+softwareInstallRequest.getVehicleManifest());
            System.err.println("Auf Server gespeichertes Manifest: \n"+manifest);
            System.err.println("");
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
        if(verified) {
            System.err.println("Fahrzeugsicherheit garantiert!");
            try {
                //Load the required Software from Database
                Software required = SoftwareDatabase.getInstance().getSoftwareByKey(softwareInstallRequest.getSoftwareID());

                String manifest = softwareInstallRequest.getVehicleManifest();
                manifest += manifest + "  " + required.getSoftwareID() + ": " + required.getVersion() + "\r\n";

                SoftwareInstallationPackage pkg = new SoftwareInstallationPackage(required.getSoftwareID(), required, required.getProvider(), manifest);
                softwareInstallRequest.getCtx().writeAndFlush(pkg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            try{
                Software required = SoftwareDatabase.getInstance().getSoftwareByKey(softwareInstallRequest.getSoftwareID());

                System.err.println("Fahrzeugsicherheit nicht garantiert!");
                SoftwareInstallationPackage pkg = new SoftwareInstallationPackage(required.getSoftwareID(), null, null, softwareInstallRequest.getVehicleManifest());
                softwareInstallRequest.getCtx().writeAndFlush(pkg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void handleSWSearchRequest(SoftwareSearchRequest request){
        /*
        Todo.
          SUPR.findBestSoftware(request)
         */
    }

    private boolean exists(String requiredSWID) {
        Software sw = SoftwareDatabase.getInstance().getSoftwareByKey(requiredSWID);
        if(sw != null)
            return true;
        return false;
    }

}
