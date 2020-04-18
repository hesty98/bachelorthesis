package Initialization.EventBus;

import Initialization.Network.MMSClientConnection;
import com.google.inject.AbstractModule;

public class MMSPortModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MMSClientConnection.class).toInstance(new MMSClientConnection());
    }
}
