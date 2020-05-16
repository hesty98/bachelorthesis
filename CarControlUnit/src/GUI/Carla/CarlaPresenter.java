package GUI.Carla;

import Actions.IAction;
import Actions.TargetAction;
import Car.MessageHandler;
import EnvironmentObjects.Software.ParkingServiceSoftware;
import EnvironmentObjects.Angebot;
import EnvironmentObjects.Description;
import EnvironmentObjects.ServiceProvider;
import Messages.*;
import GUI.LogPrinter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

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
    public ScrollPane carLog;

    @FXML
    public ScrollPane environmentlog;

    @FXML
    public Button useCarla; //start the engine

    @FXML
    public Button startScenarioButton;

    @FXML
    public Button driveIntoPerceptionAreaButton;

    @FXML
    public Button sendServiceRegistrationMessage;

    @FXML
    public Button sendServiceActionButton;

    @FXML
    public Button sayGoodbye;

    private boolean messageSent =false;

    public enum STAGE {
        NO_CAR_STARTED,
        NO_RUNNING_SCENARIO,
        NO_REGISTERED_CAR,
        CAR_IN_PERCEPTION_AREA,
        CAR_DECLINED_SERVICE,
        CAR_INSTALLING_SW,
        CAR_ACCEPTED_SERVICE
    }
    private ServiceProvider serviceProvider;

    public STAGE currentStage = STAGE.NO_CAR_STARTED;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpButtons();
        useCarla.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    final ProcessBuilder pb = new ProcessBuilder("D:\\Carla\\CarlaUE4.exe");
                    pb.directory(new File("D:\\Carla"));
                    final Process p = pb.start();
                    System.err.println("Starting Carla.... This could take a while.");
                    currentStage=STAGE.NO_RUNNING_SCENARIO;
                    setUpButtons();
                    /*
                    TODO: Buttons auf unsichtbar setzen
                      Generell: entweder Carla oder Buttons
                    */
                }catch (Exception e){
                    currentStage=STAGE.NO_REGISTERED_CAR;
                    setUpButtons();
                    LogPrinter.displayInView(carLog,"Carla Failed to run. Please correct the carla directory in config file. Running Without Carla in GUI.");

//                    e.printStackTrace();

                }
            }
        });
        startScenarioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO: start scenario in Carla
            }
        });
        driveIntoPerceptionAreaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentStage = STAGE.CAR_IN_PERCEPTION_AREA;
                LogPrinter.displayInView(environmentlog, "Percepted a Car within area. I could register to that car by clicking the Button below.");
                setUpButtons();
            }
        });
        sendServiceRegistrationMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO: send ServiceRegistrationMessage received from Carla
                if(!messageSent) {
                    //BUILD of a ServiceRegistrationMessage for the ParkingServiceSoftware

                    ArrayList<IAction> list = new ArrayList<>();
                    list.add(new TargetAction());
                    Angebot angebot = new Angebot(0);
                    serviceProvider = new ServiceProvider(
                            "hestermeyer_parken",
                            "Hestermeyer parken",
                            "verify.parking_in_germany.de/hestermeyer_parken",
                            ParkingServiceSoftware.SOFTWARE_ID

                    );
                    Description desc = new Description(
                            "Parken in der Innenstadt",
                            "Parken Sie in nächster Nähe zur Oldenburger Innenstadt!\r\n Preis pro angefangene Stunde: 70cent",
                            new ArrayList<>(),
                            new Angebot(0.70),
                            list
                    );
                    //TODO: von Carla aus tun.
                    ServiceRegistrationMessage msg = new ServiceRegistrationMessage(
                            desc, 992120, serviceProvider
                    );
                    //ID of the Service, used by Software to know what to do
                    //eventBus.post(msg);
                    messageSent =true;
                    MessageHandler.getInstance().post(msg);
                }
            }
        });
        sendServiceActionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServiceActionCommand cmd = new ServiceActionCommand(
                        new TargetAction(), serviceProvider
                );
                MessageHandler.getInstance().post(cmd);
            }
        });
        sayGoodbye.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    /**
     * Enables and disables invalid Buttons. Sets what is to happen on Action.
     */
    public void setUpButtons() {
        switch (currentStage){
            case NO_CAR_STARTED:

                setButtonVisibility(new boolean[]{true,false,false,false,false,false});
                break;
            case NO_RUNNING_SCENARIO:
                setButtonVisibility(new boolean[]{false,true,false,false,false,false});
                break;
            case NO_REGISTERED_CAR:
                setButtonVisibility(new boolean[]{false, false, true,false,false,false});
                break;
            case CAR_IN_PERCEPTION_AREA:
                setButtonVisibility(new boolean[]{false, false, false, true,false,false});
                break;
            case CAR_DECLINED_SERVICE:
                setButtonVisibility(new boolean[]{false,false,false, false,true,false});
                break;
            case CAR_INSTALLING_SW:
                setButtonVisibility(new boolean[]{false,false,false,false,false,false});
                break;
            case CAR_ACCEPTED_SERVICE:
                setButtonVisibility(new boolean[]{false, false, false, false, false, true});
                break;

            default:
                break;
        }
    }

    private void setButtonVisibility(boolean[] b) {
        System.err.println("Setting visibility...");
        useCarla.setDisable(!b[0]);
        startScenarioButton.setDisable(!b[1]);
        driveIntoPerceptionAreaButton.setDisable(!b[2]);
        sendServiceRegistrationMessage.setDisable(!b[3]);
        sayGoodbye.setDisable(!b[4]);
        sendServiceActionButton.setDisable(!b[5]);
    }

    public void printToEnvironment(String s) {
        LogPrinter.displayInView(environmentlog, s);
    }

    public void printToCar(String s) {
        LogPrinter.displayInView(carLog, s);
    }
}