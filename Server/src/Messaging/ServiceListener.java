package Messaging;

import EnvironmentObjects.ServiceDescription;
import Messages.ServiceActionMessage;
import Messages.ServicePerceptionMessage;
import Messages.ServiceRegistrationMessage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.netty.channel.ChannelHandlerContext;

/**
 * Listens on messages regarding a service handles the content.
 */
public class ServiceListener {
    private EventBus eventBus;

    public ServiceListener(EventBus eventBus) {
        this.eventBus=eventBus;
    }

    @Subscribe
    public void handleServicePerception(ServicePerceptionMessage msg){
        System.out.println("ServiceType: "+ msg.getActorType());
        System.out.println("ServiceProvider: "+msg.getActorID());
        ChannelHandlerContext ctx =(ChannelHandlerContext) msg.getCtx();
        try {
            ctx.writeAndFlush(new ServiceRegistrationMessage(new ServiceDescription(msg.getActorType(), "I am a cool service", "Parkplatz", null, null), 1234));
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            System.out.println("Sent the Message.");
        }
    }

}
