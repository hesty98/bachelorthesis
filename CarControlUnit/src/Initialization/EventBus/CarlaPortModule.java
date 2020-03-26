package Initialization.EventBus;

import Initialization.CarlaConnection.CarlaClientConnection;
import Initialization.MMSConnection.MMSClientConnection;
import Messages.IMessage;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class CarlaPortModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CarlaClientConnection.class).toInstance(new CarlaClientConnection());
    }
}
