import Initialization.EventBusModule;
import Initialization.MainDependencies;
import Initialization.ServerNettyModule;
import View.Main.MainPresenter;
import View.Main.MainView;
import com.google.inject.Guice;
import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Linus Hestermeyer
 *
 * View.Main Class.
 */
public class CarControlUnitMain extends Application {

    private static final com.google.inject.Injector injector = Guice.createInjector(
            new EventBusModule(),
            new ServerNettyModule()
    );

    private static final MainDependencies mainDependencies = injector.getInstance(MainDependencies.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainView mainView = new MainView();

        /**
         * Darstellen der Scene
         */
        Scene scene = new Scene(mainView.getView());
        primaryStage.setTitle("CarControlUnit GUI");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        /**
         * übergibt dem mainpresenter die primarystage, damit dieser die windowsize anpassen kann.
         */
        MainPresenter mainPresenter = (MainPresenter) mainView.getPresenter();
        mainPresenter.setStage(primaryStage);

        /**
         * sizetoScene muss hier geschehen, da im MainPresenter initalizer noch keine primarystage übergeben ist
         */
        primaryStage.sizeToScene();
        primaryStage.show();

        if(!mainDependencies.isNettyConnectionSuccess())
        {
            mainDependencies.postNettyConnectionFailed();
        }
    }

    @Override
    public void stop() throws Exception {

        Injector.forgetAll();
    }
}
