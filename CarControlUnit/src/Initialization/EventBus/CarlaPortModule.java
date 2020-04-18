package Initialization.EventBus;

import Initialization.Network.CarlaClientConnection;
import com.google.inject.AbstractModule;

public class CarlaPortModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CarlaClientConnection.class).toInstance(new CarlaClientConnection());
    }
}
