package MessageManager;

import EnvironmentObjects.Software.ParkingServiceSoftware;
import EnvironmentObjects.Software.Software;
import Messages.ServiceVerificationCommand;
import Messages.ServiceVerificationMessage;
import Messages.SoftwareInstallRequest;
import Messages.SoftwareInstallationPackage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Listens on messages regarding a service handles the content.
 */
public class ServiceListener {
    private EventBus eventBus;

    public ServiceListener(EventBus eventBus) {
        this.eventBus=eventBus;
    }

    @Subscribe
    public void incomingVerificationCommand(ServiceVerificationCommand serviceVerificationCommand){
        System.err.println("Received ServiceVerification Command. InquiryID: "+serviceVerificationCommand.getInquiryID()
                + "\nManifest:\n"+serviceVerificationCommand.getCar_manifest()
                + "\nProvider: "+serviceVerificationCommand.getServiceProvider().getProviderName()
        );
        if(verified(serviceVerificationCommand.getServiceProvider().getRequiredSoftwareID())){
            ServiceVerificationMessage msg = new ServiceVerificationMessage(
                    serviceVerificationCommand.getDescription(),
                    true,
                    serviceVerificationCommand.getCar_manifest()+" updated",
                    serviceVerificationCommand.getInquiryID(),
                    serviceVerificationCommand.getServiceProvider()
            );
            serviceVerificationCommand.getCtx().writeAndFlush(msg);
        }

    }

    @Subscribe
    public void handleSWInstallRequest(SoftwareInstallRequest softwareInstallRequest){
        //Todo: get from Database
        Software parkSoftware = new ParkingServiceSoftware();
        SoftwareInstallationPackage pkg = new SoftwareInstallationPackage(parkSoftware.getSoftwareID(), parkSoftware);

        softwareInstallRequest.getCtx().writeAndFlush(pkg);
    }


    private boolean verified(String publicProviderID) {
        /*TODO future: check, rather or not Service is verified by SoftwareProvider
            OEMs can unVerify a ServiceProvider if its handlings are illegal
         */
        return true;
    }

}
