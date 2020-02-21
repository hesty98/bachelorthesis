package Messaging;

import Messages.ServiceActionMessage;
import Messages.ServiceRegistrationMessage;
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
    public void registerService(ServiceRegistrationMessage msg){
        System.out.println(msg.getDescription().getServiceTitle());
    }

    @Subscribe
    public void handleServiceAction(ServiceActionMessage msg){
        //check if serviceProvider is registered
    }

}
