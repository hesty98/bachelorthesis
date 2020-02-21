package init;

import Messaging.ServiceListener;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import network.NettyServer;

/**
 * Steuert die Initzialisierung der NettyServer-Instanz --> ©c.blank♥
 * @author Markus Reinke
 */
public class MainDependencies {
    private final EventBus eventBus;
    //private final DataHandler dataHandler; -> used for MySQL zugriffe

    @Inject
    public MainDependencies(EventBus eventBus/*, DataHandler dataHandler*/){
        this.eventBus=eventBus;
        //this.dataHandler = dataHandler;
        registerListeners();
    }

    /**
     * Diese Methode registriert alle Listener, die in ihr erstellt werden.
     */
    private void registerListeners(){
        ServiceListener serviceListener = new ServiceListener(this.eventBus);
        eventBus.register(serviceListener);
    }

    public void init() {
        NettyServer nettyServer = new NettyServer(this.eventBus);
    }
}
