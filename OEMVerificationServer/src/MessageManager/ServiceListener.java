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
        if(verified(serviceVerificationCommand.getServiceDescription().getServiceVerificationURL())){
            ServiceVerificationMessage msg = new ServiceVerificationMessage(
                    serviceVerificationCommand.getServiceDescription(),
                    true,
                    serviceVerificationCommand.getCar_manifest()+" updated",
                    serviceVerificationCommand.getInquiryID(),
                    serviceVerificationCommand.getServiceSoftwareID()
            );
            serviceVerificationCommand.getCtx().writeAndFlush(msg);
        }

    }

    private boolean verified(String serviceVerificationURL) {
        //TODO future: check, rather or not Service is verified
        return true;
    }

}
