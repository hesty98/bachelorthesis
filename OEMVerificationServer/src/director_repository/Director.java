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
        try {
            //Load the required Software from Database
            Software required = SoftwareDatabase.getInstance().getSoftwareByKey(softwareInstallRequest.getSoftwareID());
            String manifest = softwareInstallRequest.getVehicleManifest();
            manifest += manifest+ "SW\r\n  id: "+required.getSoftwareID()+"\r\n  version: "+required.getVersion()+"\r\n \r\n";
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
