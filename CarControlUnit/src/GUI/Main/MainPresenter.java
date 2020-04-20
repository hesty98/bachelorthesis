package GUI.Main;

import GUI.CCUMessageHandler.MessageHandlerPresenter;
import GUI.Carla.CarlaPresenter;
import Initialization.Network.OEMVerificationServerConnection.NettyConnectionClient;
import GUI.Carla.CarlaView;
import GUI.CCUMessageHandler.MessageHandlerView;

import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Linus Hestermeyer
 */
public class MainPresenter implements Initializable {

    @FXML
    public BorderPane leftPane;

    @FXML
    public BorderPane rightPane;

    private Stage primaryStage;
    private CarlaView carlaView = new CarlaView();
    private MessageHandlerView messageHandlerView = new MessageHandlerView();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftPane.setCenter(messageHandlerView.getView());
        rightPane.setCenter(carlaView.getView());

        Platform.runLater(() -> {
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
        });
    }

    public MessageHandlerPresenter getMessageHandlerPresenter(){
        return (MessageHandlerPresenter)messageHandlerView.getPresenter();
    }

    public CarlaPresenter getCarlaPresenter(){
        return (CarlaPresenter)carlaView.getPresenter();
    }


    public void setStage(Stage stage){
        this.primaryStage = stage;
    }
}