package Initialization.EventBus;

import Initialization.MMSConnection.MMSClientConnection;
import Messages.IMessage;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class MMSPortModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MMSClientConnection.class).toInstance(new MMSClientConnection());
    }
}
