package Initialization;

import Events.NoConnectionEvent;
import Initialization.Netty.NettyClient;
import Initialization.Netty.NettyClientInitializer;
import Messages.ServicePerceptionMessage;
import Messaging.ServiceListener;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.airhacks.afterburner.injection.Injector;

import java.util.HashMap;
import java.util.Map;

public class MainDependencies {
    private final NettyClientInitializer nettyClientInitializer;
    private final EventBus eventBus;
    private final NettyClient nettyClient;

    private boolean nettyConnectionSuccess;

    @Inject
    public MainDependencies(NettyClientInitializer nettyClientInitializer,EventBus eventBus){
        this.eventBus=eventBus;
        this.nettyClientInitializer = nettyClientInitializer;
        this.nettyClient = (NettyClient)nettyClientInitializer.createNettyClientConnection(NetworkConfig.serverUrl,NetworkConfig.serverPort);

        Map<Object, Object> context = new HashMap<>();
        context.put( "eventBus", this.eventBus);
        context.put( "nettyClient", this.nettyClient);
        Injector.setConfigurationSource( context::get );

        this.nettyConnectionSuccess = this.nettyClient.isConnectionSuccess();
        registerListeners();
    }

    /**
     * Registers all Listeners on the EventBus. Called
     */
    private void registerListeners(){
        ServiceListener serviceListener = new ServiceListener(this.eventBus);
        eventBus.register(serviceListener);
    }

    /**
     * Called if Connection to Server was successful. Listeners are already Subscribed.
     *    0. initialize intern communication
     *    1. start Carla Simulation (call .exe file)
     *    2. start MMS (launch APK in VirtualDevice)
     *    3. connect to Server
     */
    public void init() {
        nettyClient.sendMessage(new ServicePerceptionMessage("automated_parking", "stadt-oldenburg"));
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
