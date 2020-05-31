package setup.init;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class EventBusModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).toInstance(new EventBus());
    }
}
