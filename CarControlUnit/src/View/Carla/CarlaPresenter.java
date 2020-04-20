package View.Carla;

import Actions.ActionEnums;
import Car.Software.ParkingServiceSoftware;
import EnvironmentObjects.Angebot;
import EnvironmentObjects.ServiceDescription;
import EnvironmentObjects.ServiceProvider;
import Initialization.OEMVerificationServerConnection.NettyConnectionClient;
import Messages.*;
import View.LogPrinter;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import java.io.File;
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
    private NettyConnectionClient nettyClient;

    @FXML
    public Label carLog;

    @FXML
    public Label environmentlog;

    @FXML
    public Button useCarla;

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
                    //BUILD of a ServiceRegistrationMessage for the ParkingServiceSoftware

                    ArrayList<ActionEnums> list = new ArrayList<>();
                    list.add(ActionEnums.TARGET);
                    Angebot angebot = new Angebot(0);
                    ServiceProvider serviceProvider = new ServiceProvider(
                            "hestermeyer_parken",
                            "Hestermeyer parken",
                            "verify.parking_in_germany.de/hestermeyer_parken"

                    );
                    ServiceDescription desc = new ServiceDescription(
                            "Parken in der Innenstadt",
                            "Parken Sie in nächster Nähe zur Oldenburger Innenstadt!\r\n Preis pro angefangene Stunde: 70cent",
                            serviceProvider,
                            "PARKING_SERVICE_GERMAN_CITIES",
                            new ArrayList<>(),
                            new Angebot(0.70),
                            new ArrayList<ActionEnums>()
                    );
                    //TODO: von Carla aus tun.
                    ServiceRegistrationMessage msg = new ServiceRegistrationMessage(desc, 992120, ParkingServiceSoftware.SOFTWARE_ID);
                    //ID of the Service, used by Software to know what to do
                    msg.setServiceID("park_lh228");
                    eventBus.post(msg);
                    happened=true;
                }
            }
        });
        useCarla.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    final ProcessBuilder pb = new ProcessBuilder("D:\\Carla\\CarlaUE4.exe");
                    pb.directory(new File("D:\\Carla"));
                    final Process p = pb.start();
                    System.err.println("Starting Carla.... This could take a while.");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Method belongs to the ParkingServiceActor of CarlaSimulation. Is used in order to not have to connect to carla-python-script
     * @param serviceDecisionMessage
     */
    @Subscribe
    public void waitForDecisions(ServiceDecisionMessage serviceDecisionMessage){
        if(serviceDecisionMessage.isAccepted()) {
            LogPrinter.displayInView(environmentlog, environmentlog.getText() +
                    "\nDriver accepted to use the Service! Preparing the ServiceActionCommand");

            ServiceActionCommand serviceActionCommand = new ServiceActionCommand(ActionEnums.MOVEMENT, serviceDecisionMessage.getServiceID(), serviceDecisionMessage.getSoftwareID());
            eventBus.post(serviceActionCommand);
        }
    }

    /**
     * Method belongs to Carla Car, needed for GUI Log.
     *
     * @param serviceActionMessage
     */
    @Subscribe
    public void readyForAction(ServiceActionMessage serviceActionMessage){
        LogPrinter.displayInView(carLog ,"Received a new Action, yeeeey!");
    }


    /**
     * TODO: Über NEtty und OEMServer machen, da gehört das hin.
     * @param msg
     */
    @Subscribe
    public void handleInstallRequest(SoftwareInstallRequest msg){
        SoftwareInstallationPackage sw= new SoftwareInstallationPackage(msg.getSoftwareID(), new ParkingServiceSoftware("Parken in Deutschalnds Städten",
                "Hier steht üblicherweise eine ausführliche Beschreibung der Software"));
        eventBus.post(sw);
    }
}