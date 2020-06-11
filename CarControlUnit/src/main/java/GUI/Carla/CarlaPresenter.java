package GUI.Carla;

import Actions.GoAwayAction;
import Actions.IAction;
import Actions.TargetAction;
import Car.MessageHandler;
import EnvironmentObjects.Angebot;
import EnvironmentObjects.Description;
import EnvironmentObjects.Provider;
import Messages.*;
import GUI.LogPrinter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import Messages.CarlaMessage;
import Messages.ServiceActionCommand;
import Messages.ServiceRegistrationMessage;

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


    public static final String CAR_1_ID = "CAR-LA-1";
    public static final String CAR_1_MANIFEST =
            "software:\r\n" +
                    "  DriveAutomaticBasic: 1.0\r\n" +
                    "  DriveInMcDonalds: 1.2\r\n";

    //TODO: edit this according to your local Carla.exe/Carla.sh
    private static final String PATH =
            "D:\\Carla";

    @FXML
    public ScrollPane environmentlog;

    @FXML
    public Button startScenario; //start the engine

    @FXML
    public Button driveAround;

    @FXML
    public Button driveIntoPerceptionAreaButton;

    @FXML
    public Button sendServiceRegistrationMessage;

    @FXML
    public Button sendServiceActionButton;

    @FXML
    public Button sayGoodbye;

    @FXML
    public Button hack;

    @FXML
    public Label hackLabel;

    private boolean messageSent =false;

    private ScrollPane carLog;

    public void setCarLogRefference(ScrollPane carLog) {
        this.carLog=carLog;
    }

    public enum STAGE {
        NO_CAR_STARTED,
        NO_RUNNING_SCENARIO,
        NO_REGISTERED_CAR,
        CAR_IN_PERCEPTION_AREA,
        CAR_DECLINED_SERVICE,
        CAR_INSTALLING_SW,
        CAR_ACCEPTED_SERVICE,
        CAR_PARKED
    }
    private Provider provider;

    public STAGE currentStage = STAGE.NO_CAR_STARTED;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpButtons();
        startScenario.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MessageHandler.getInstance().sendToCarla(new CarlaMessage(1));
                LogPrinter.displayInView(carLog, "Fahrzeug wurde in Carla gespawned.");
                currentStage=STAGE.NO_RUNNING_SCENARIO;
                setUpButtons();
            }
        });
        driveAround.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MessageHandler.getInstance().sendToCarla(new CarlaMessage(2));
                LogPrinter.displayInView(carLog, "Fahrzeug fährt zum Parkplatz.");
                currentStage = STAGE.NO_REGISTERED_CAR;
                setUpButtons();
            }
        });
        driveIntoPerceptionAreaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MessageHandler.getInstance().sendToCarla(new CarlaMessage(3));
                currentStage = STAGE.CAR_IN_PERCEPTION_AREA;
                LogPrinter.displayInView(carLog, "Fahrzeug fährt in Registrierungszone des Parkplatzes.");
                LogPrinter.displayInView(environmentlog, "Fahrzeug in Registrierungszone erkannt. Registrierung möglich.");
                setUpButtons();
            }
        });
        sendServiceRegistrationMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<IAction> list = new ArrayList<>();
                list.add(new TargetAction());
                provider = new Provider(
                      "GER_PARK_28",
                        "Hestermeyer Parking and partying",
                        "linushestermeyer.de"
                );
                ArrayList<String> angebotTitel = new ArrayList<>();
                angebotTitel.add("Preis pro Stunde");

                ArrayList<Angebot> angebote = new ArrayList<>();
                angebote.add(new Angebot(0.70));
                Description desc = new Description(
                        "Parken in Oldenburg",
                        "Parken Sie in nächster Nähe zur Oldenburger Innenstadt!",
                        new ArrayList<>(),
                        angebote,
                        angebotTitel,
                        list
                );

                ServiceRegistrationMessage msg = new ServiceRegistrationMessage(
                        desc, 992120, provider, "PARKING_SERVICE_GERMAN_CITIES"
                );

                MessageHandler.getInstance().post(msg);

            }
        });
        sendServiceActionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentStage== STAGE.CAR_ACCEPTED_SERVICE) {
                    ServiceActionCommand cmd = new ServiceActionCommand(
                            new TargetAction(), provider, "PARKING_SERVICE_GERMAN_CITIES"
                    );
                    MessageHandler.getInstance().post(cmd);
                }else if(currentStage== STAGE.CAR_DECLINED_SERVICE){
                    ServiceActionCommand cmd = new ServiceActionCommand(
                            new GoAwayAction(), provider, "PARKING_SERVICE_GERMAN_CITIES"
                    );
                    MessageHandler.getInstance().post(cmd);
                }
                currentStage=STAGE.CAR_PARKED;
                setUpButtons();
            }
        });
        sayGoodbye.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MessageHandler.getInstance().sendToCarla(new CarlaMessage(6));
                currentStage=STAGE.NO_CAR_STARTED;
                setUpButtons();
            }
        });
        hack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(MessageHandler.getInstance().hacked){
                    //nicht mehr gehackt
                    hackLabel.setText("Nicht gehackt");
                    hackLabel.setTextFill(Color.web("#21a810"));
                } else{
                    hackLabel.setText("Gehackt");
                    hackLabel.setTextFill(Color.web("#f50000"));
                }
                MessageHandler.getInstance().hack();
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
            case CAR_INSTALLING_SW:
                setButtonVisibility(new boolean[]{false,false,false,false,false,false});
                break;
            case CAR_DECLINED_SERVICE:
                setButtonVisibility(new boolean[]{false,false,false, false,true,false});
                break;
            case CAR_ACCEPTED_SERVICE:
                setButtonVisibility(new boolean[]{false,false,false, false,true,false});
                break;
            case CAR_PARKED:
                setButtonVisibility(new boolean[]{false,false,false, false,false,true});
                break;

            default:
                break;
        }
    }

    private void setButtonVisibility(boolean[] b) {
        System.err.println("Setting visibility...");
        startScenario.setDisable(!b[0]);
        driveAround.setDisable(!b[1]);
        driveIntoPerceptionAreaButton.setDisable(!b[2]);
        sendServiceRegistrationMessage.setDisable(!b[3]);
        sendServiceActionButton.setDisable(!b[4]);
        sayGoodbye.setDisable(!b[5]);
    }

    public void printToEnvironment(String s) {
        LogPrinter.displayInView(environmentlog, s);
    }
}