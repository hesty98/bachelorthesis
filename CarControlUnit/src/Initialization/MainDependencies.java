package Initialization;

import Initialization.Network.Events.NoConnectionEvent;
import Initialization.Network.CarlaClientConnection;
import Initialization.Network.MMSClientConnection;
import Initialization.Network.NetworkConfig;
import Initialization.Network.OEMVerificationServerConnection.NettyConnectionClient;
import Initialization.Network.OEMVerificationServerConnection.NettyClientInitializer;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.airhacks.afterburner.injection.Injector;

import java.util.HashMap;
import java.util.Map;

/**
 * This class' purpose is to start the connection to every chosen module of the prototype.
 *
 * @author linus hestermeyer
 */
public class MainDependencies {
    //private final NettyClientInitializer nettyClientInitializer;
    private final EventBus eventBus;
    //private final NettyConnectionClient nettyClient;

    //private final CarlaClientConnection carlaClientConnection;
    //private final MMSClientConnection mmsClientConnection;

    private boolean nettyConnectionSuccess;

    public MainDependencies(EventBus eventBus){
        this.eventBus=eventBus;

        this.nettyClient = (NettyConnectionClient)nettyClientInitializer.createNettyClientConnection(NetworkConfig.serverUrl,NetworkConfig.serverPort);

        //this.carlaClientConnection=carlaClientConnection;
        //this.carlaClientConnection.initBootstrap("127.0.0.1", 22898);

        //this.mmsClientConnection=mmsClientConnection;
        //this.mmsClientConnection.initBootstrap("127.0.0.1",28620);
/*
        Map<Object, Object> context = new HashMap<>();
        context.put( "eventBus", this.eventBus);
        context.put( "nettyClient", this.nettyClient);
        Injector.setConfigurationSource( context::get );

        this.nettyConnectionSuccess = this.nettyClient.isConnectionSuccess();
*/

    }

    public void init() {
        /*
        Softwareliste setzen

         */
    }

    /**
     *
     * Wenn der Verbindungsaufbau via Netty fehlerhaft ist,
     * wird dem Eventbus Event uebergeben
     *
     *
     */
    public void postNettyConnectionFailed()
    {
        this.eventBus.post(new NoConnectionEvent());
    }

    /**
     * Getter fuer nettyConnectionSuccess
     *
     * @return nettyConnectionSuccess
     */
    public boolean isNettyConnectionSuccess() {
        return nettyConnectionSuccess;
    }
}
