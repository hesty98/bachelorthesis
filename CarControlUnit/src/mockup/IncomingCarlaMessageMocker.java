package mockup;

import Initialization.Netty.NettyClient;
import Messages.ServicePerceptionMessage;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @author Linus Hestermeyer
 *
 * Messages, which vom into ControlUnit initialized by Carla.
 */
public class IncomingCarlaMessageMocker {

    private NettyClient nettyClient;


    @Inject
    public IncomingCarlaMessageMocker(NettyClient nettyClient) {
        this.nettyClient=nettyClient;
    }

    public void sendPerceptionMessage(){
        nettyClient.sendMessage(new ServicePerceptionMessage("automated_parking", "stadt-oldenburg"));
    }
}
