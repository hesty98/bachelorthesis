package Initialization;

import Events.NoConnectionEvent;
import Initialization.CarlaConnection.CarlaClientConnection;
import Initialization.EventBus.CarlaPortModule;
import Initialization.EventBus.MMSPortModule;
import Initialization.MMSConnection.MMSClientConnection;
import Initialization.OEMVerificationServerConnection.NettyConnectionClient;
import Initialization.OEMVerificationServerConnection.NettyClientInitializer;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.airhacks.afterburner.injection.Injector;

import java.util.HashMap;
import java.util.Map;

public class MainDependencies {
    private final NettyClientInitializer nettyClientInitializer;
    private final EventBus eventBus;
    private final NettyConnectionClient nettyClient;

    //private final CarlaClientConnection carlaClientConnection;
    //private final MMSClientConnection mmsClientConnection;

    private boolean nettyConnectionSuccess;

    @Inject
    public MainDependencies(NettyClientInitializer nettyClientInitializer,EventBus eventBus, CarlaClientConnection carlaClientConnection, MMSClientConnection mmsClientConnection){
        this.eventBus=eventBus;

        this.nettyClientInitializer = nettyClientInitializer;
        this.nettyClient = (NettyConnectionClient)nettyClientInitializer.createNettyClientConnection(NetworkConfig.serverUrl,NetworkConfig.serverPort);
/*
        this.carlaClientConnection=carlaClientConnection;
        this.carlaClientConnection.initBootstrap("127.0.0.1", 22898);

        this.mmsClientConnection=mmsClientConnection;
        this.mmsClientConnection.initBootstrap("127.0.0.1",28620);
*/
        Map<Object, Object> context = new HashMap<>();
        context.put( "eventBus", this.eventBus);
        context.put( "nettyClient", this.nettyClient);
        Injector.setConfigurationSource( context::get );

        this.nettyConnectionSuccess = this.nettyClient.isConnectionSuccess();
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
