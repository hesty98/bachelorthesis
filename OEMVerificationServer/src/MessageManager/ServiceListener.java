package MessageManager;

import EnvironmentObjects.Software.ParkingServiceSoftware;
import EnvironmentObjects.Software.Software;
import Messages.SoftwareVerificationCommand;
import Messages.SoftwareVerificationMessage;
import Messages.SoftwareInstallRequest;
import Messages.SoftwareInstallationPackage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import init.SoftwareDatabase;

/**
 * Listens on messages regarding a service handles the content.
 */
public class ServiceListener {
    private EventBus eventBus;
    private SoftwareDatabase database;

    public ServiceListener(EventBus eventBus) {
        this.eventBus=eventBus;
    }

    @Subscribe
    public void incomingVerificationCommand(SoftwareVerificationCommand softwareVerificationCommand){
        System.err.println("Received ServiceVerification Command. InquiryID: "+ softwareVerificationCommand.getInquiryID()
                + "\nManifest:\n"+ softwareVerificationCommand.getCar_manifest()
                + "\nProvider: "+ softwareVerificationCommand.getProvider().getProviderName()
        );


        if(verified(softwareVerificationCommand.getSoftwareID())){
            Software sw = SoftwareDatabase.getInstance().getSoftwareByKey(softwareVerificationCommand.getSoftwareID());

            SoftwareVerificationMessage msg = new SoftwareVerificationMessage(
                    //Todo: description for SW
                    sw.getDescription(),
                    true,
                    softwareVerificationCommand.getCar_manifest()+" updated",
                    softwareVerificationCommand.getInquiryID(),
                    sw.getProvider(),
                    sw.getSoftwareID(),
                    sw.getVerifiedProviders()
            );
            softwareVerificationCommand.getCtx().writeAndFlush(msg);
        }

    }

    @Subscribe
    public void handleSWInstallRequest(SoftwareInstallRequest softwareInstallRequest){
        Software required =SoftwareDatabase.getInstance().getSoftwareByKey(softwareInstallRequest.getSoftwareID());;
        SoftwareInstallationPackage pkg = new SoftwareInstallationPackage(required.getSoftwareID(), required, required.getProvider());
        softwareInstallRequest.getCtx().writeAndFlush(pkg);
    }


    private boolean verified(String requiredSWID) {
        Software sw = SoftwareDatabase.getInstance().getSoftwareByKey(requiredSWID);
        if(sw != null)
            return true;
        return false;
    }

}
