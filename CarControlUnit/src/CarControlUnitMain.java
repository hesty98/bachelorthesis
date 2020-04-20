import GUI.Main.MainPresenter;
import GUI.Main.MainView;
import Car.MessageHandler;
import com.google.common.eventbus.EventBus;
import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Linus Hestermeyer
 *
 * View.Main Class.
 */
public class CarControlUnitMain extends Application {

    private MessageHandler handler;
    private EventBus bus= new EventBus();

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

        handler=MessageHandler.getInstance();
        handler.setEventBus(bus);
        handler.setCarlaPresenter(mainPresenter.getCarlaPresenter());
        handler.setMessageHandlerPresenter(mainPresenter.getMessageHandlerPresenter());
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }
}
