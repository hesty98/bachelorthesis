package View.Carla;

import Actions.ActionEnums;
import EnvironmentObjects.Angebot;
import EnvironmentObjects.Service;
import EnvironmentObjects.ServiceDescription;
import Initialization.Netty.NettyClient;
import Messages.ServiceActionCommand;
import Messages.ServiceActionMessage;
import Messages.ServiceDecisionMessage;
import Messages.ServiceRegistrationMessage;
import View.LogPrinter;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Diese Klasse repräsentiert den MainPresenter der die Mainview steuert,
 * und somit die einzelnen Views lädt.
 *
 * @version 1.0
 */
public class CarlaPresenter implements Initializable {

    @FXML
    public BorderPane mainPane;

    @Inject
    private EventBus eventBus;

    @Inject
    private NettyClient nettyClient;

    @FXML
    public Label carLog;

    @FXML
    public Label environmentlog;

    @FXML
    public Button sendServiceRegistrationMessage;

    @FXML
    public Button sendServiceActionButton;

    @FXML
    public Button driveIntoPerceptionAreaButton;

    private boolean happened =false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register(this);
        sendServiceRegistrationMessage.setDisable(false);
        sendServiceRegistrationMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!happened) {
                    ArrayList<ActionEnums> list = new ArrayList<>();
                    list.add(ActionEnums.TARGET);
                    Angebot angebot = new Angebot(19.99);
                    ServiceDescription desc = new ServiceDescription("automated_parking",
                            "Eine lange Beschreibung des Service.",
                            "Parken am Schloßplatz",
                            list,
                            angebot,
                            "parken.stadt-oldenburg.de",
                            "Stadt Oldenburg"
                    );
                    //TODO: von Carla aus tun.
                    ServiceRegistrationMessage msg = new ServiceRegistrationMessage(desc, 992120);
                    eventBus.post(msg);
                    happened=true;
                }
            }
        });
    }

    @Subscribe
    public void waitForDecisions(ServiceDecisionMessage serviceDecisionMessage){
        if(serviceDecisionMessage.isAccepted()) {
            LogPrinter.displayInView(environmentlog, environmentlog.getText() +
                    "\nDriver accepted to use the Service! Preparing the ServiceActionCommand");

            Service service = new Service("automated_parking", "Stadt Oldenburg");
            ServiceActionCommand serviceActionCommand = new ServiceActionCommand(ActionEnums.MOVEMENT, service);
            eventBus.post(serviceActionCommand);
            //TODO future: an CarlaPython zusätzlich schicken
        }
    }

    @Subscribe
    public void readyForAction(ServiceActionMessage serviceActionMessage){
        LogPrinter.displayInView(carLog ,"Received a new Action, yeeeey!");
    }
}