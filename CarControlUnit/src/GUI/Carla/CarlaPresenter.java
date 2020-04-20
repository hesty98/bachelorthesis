package GUI.Carla;

import Actions.ActionEnums;
import EnvironmentObjects.Software.ParkingServiceSoftware;
import EnvironmentObjects.Angebot;
import EnvironmentObjects.ServiceDescription;
import EnvironmentObjects.ServiceProvider;
import Messages.*;
import GUI.LogPrinter;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

    @Inject
    private EventBus eventBus;

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

    private boolean messageSent =false;

    private enum STAGE {
        NO_REGISTERED_CAR,
        CAR_IN_PERCEPTION_AREA,
        CAR_LEAVING,
        CAR_INSTALLING_SW,
        CAR_WAITING_FOR_SERVICEACTION,
    }

    private STAGE currentStage = STAGE.NO_REGISTERED_CAR;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpButtons();
        sendServiceRegistrationMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!messageSent) {
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
                            new ArrayList<>(),
                            new Angebot(0.70),
                            new ArrayList<ActionEnums>()
                    );
                    //TODO: von Carla aus tun.
                    ServiceRegistrationMessage msg = new ServiceRegistrationMessage(desc, 992120, serviceProvider.getPublicProviderID(), ParkingServiceSoftware.SOFTWARE_ID);
                    //ID of the Service, used by Software to know what to do
                    eventBus.post(msg);
                    messageSent =true;
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
                    setButtonVisibility(true);
                    /*
                    TODO: Buttons auf unsichtbar setzen
                      Generell: entweder Carla oder Buttons
                    */
                }catch (Exception e){
                    setButtonVisibility(true);
                    System.err.println("Carla Failed to run.");
                    e.printStackTrace();
                    //TODO: wenn carla nichtz startet soll steuerung über GUI-Buttons möglich sein.
                }
            }
        });
    }

    /**
     * Enables and disables invalid Buttons. Sets what is to happen on Action.
     */
    private void setUpButtons() {
        switch (currentStage){
            case NO_REGISTERED_CAR:
                setButtonVisibility(new boolean[]{false, false, true});
                break;
            case CAR_IN_PERCEPTION_AREA:
                setButtonVisibility(new boolean[]{true, false, false});
                break;
            case CAR_LEAVING:
            case CAR_INSTALLING_SW:
                setButtonVisibility(new boolean[]{false,false,false});
                break;
            case CAR_WAITING_FOR_SERVICEACTION:
                setButtonVisibility(new boolean[]{false, true, false});
                break;

            default:
                break;
        }
    }

    private void setButtonVisibility(boolean[] b) {
        sendServiceRegistrationMessage.setDisable(b[0]);
        sendServiceActionButton.setDisable(b[1]);
        driveIntoPerceptionAreaButton.setDisable(b[2]);
    }

    private void setButtonVisibility(boolean b) {
        useCarla.setDisable(b);
        sendServiceRegistrationMessage.setDisable(b);
        sendServiceActionButton.setDisable(b);
        driveIntoPerceptionAreaButton.setDisable(b);
    }

    public void printToEnvironment(String s) {
        LogPrinter.displayInView(environmentlog, s);
    }

    public void printToCar(String s) {
        LogPrinter.displayInView(carLog, s);
    }
}