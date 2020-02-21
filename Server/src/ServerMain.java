import com.google.inject.Guice;
import com.google.inject.Injector;
import init.EventBusModule;
import init.MainDependencies;
import network.NettyServerModule;

/**
 * Injector der MainDependencies für die Main Klasse
 * @author Markus Reinke
 */

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