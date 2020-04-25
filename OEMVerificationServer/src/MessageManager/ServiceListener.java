package MessageManager;

import Messages.ServiceVerificationCommand;
import Messages.ServiceVerificationMessage;
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
                + "\nProvider: "+serviceVerificationCommand.getServiceDescription().getServiceProvider()
        );
        if(verified(serviceVerificationCommand.getServiceDescription().getServiceProvider().getPublicProviderID())){
            ServiceVerificationMessage msg = new ServiceVerificationMessage(
                    serviceVerificationCommand.getServiceDescription(),
                    true,
                    serviceVerificationCommand.getCar_manifest()+" updated",
                    serviceVerificationCommand.getInquiryID(),
                    serviceVerificationCommand.getRequiredSWID(),
                    serviceVerificationCommand.getServiceID()
            );
            serviceVerificationCommand.getCtx().writeAndFlush(msg);
        }

    }

    private boolean verified(String publicProviderID) {
        /*TODO future: check, rather or not Service is verified by SoftwareProvider
            OEMs can unVerify a ServiceProvider if its handlings are illegal
         */
        return true;
    }

}
