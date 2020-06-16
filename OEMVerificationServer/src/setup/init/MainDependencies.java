package setup.init;

import director_repository.Director;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import setup.network.NettyServer;


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
        Director director = new Director(this.eventBus);
        eventBus.register(director);
    }

    public void init() {
        NettyServer nettyServer = new NettyServer(this.eventBus);
    }
}
