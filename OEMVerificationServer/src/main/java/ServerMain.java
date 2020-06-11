import com.google.inject.Guice;
import com.google.inject.Injector;
import setup.init.EventBusModule;
import setup.init.MainDependencies;
import setup.network.NettyServerModule;

public class ServerMain {
    private static final Injector injector = Guice.createInjector(
            new NettyServerModule(),
            new EventBusModule()
    );

    private static final MainDependencies mainDependencies = injector.getInstance(MainDependencies.class);

    public static void main(String[] args) {
        //ServerLogger.start();
        mainDependencies.init();
    }
}
