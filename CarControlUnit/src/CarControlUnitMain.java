import Initialization.EventBusModule;
import Initialization.MainDependencies;
import Initialization.ServerNettyModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import mockup.IncomingCarlaMessageMocker;

/**
 * @author Linus Hestermeyer
 *
 * Main Class.
 */
public class CarControlUnitMain {

    private static final Injector injector = Guice.createInjector(
            new EventBusModule(),
            new ServerNettyModule()
    );

    private static final MainDependencies mainDependencies = injector.getInstance(MainDependencies.class);

    public static void main(String[] args) {
        if (!mainDependencies.isNettyConnectionSuccess()) {
            mainDependencies.postNettyConnectionFailed();
        } else {
            mainDependencies.init();
        }

    }
}
