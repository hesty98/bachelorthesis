package EnvironmentObjects.Software;

import Actions.GoAwayAction;
import Actions.IAction;
import Actions.MovementHintAction;
import Actions.TargetAction;
import EnvironmentObjects.*;
import Messages.*;

import java.util.ArrayList;

/**
 * With this Software, simulated cars are supposed to park in a specific location.
 *
 * @version 1.0
 * @author Linus Hestermeyer
 */
public class ParkingServiceSoftware extends Software {
    public static final String SOFTWARE_ID = "PARKING_SERVICE_GERMAN_CITIES";
    private static final Provider PROVIDER = new Provider("GER_PARK", "Parken in Deutschland GmbH", "linushestermeyer.de");
    private static final String NAME = "Parken in Deutschland";
    private static final String DESCRIPTION = "Mit dieser Software kann in jeder Stadt Deutschlands auf städtischen Parkplätzen geparkt werden." +
            "\n    Berechtigungen: Zieleingabe, V2X-Kommunikation" +
            "\n    Kennzahlen:     5000 tägliche Verwendungen im Umkreis von 10km der aktuellen Position.";

    private static final ArrayList<String> ANGEBOT_TITEL=new ArrayList<String>(){
        {
            add("Kauf");
            add("Leihe für 6 Monate");
            add("Monatsabo");
        }
    };

    private static final ArrayList<Angebot> ANGEBOTE=new ArrayList<Angebot>(){
        {
            add(new Angebot(49.99)); //Kauf
            add(new Angebot(22.50)); //Leihe für 6 Monate
            add(new Angebot(4.99)); //Abo für 1 Monat
        }
    };
    private static final ArrayList<IAction> ACTIONTYPES=new ArrayList<IAction>(){
        {
            add(new MovementHintAction());
            add(new TargetAction());
        }
    };
    private static final ArrayList<String> BUZZWORDS=new ArrayList<String>(){
        {
            add("Parken");
            add("Deutschland");
            add("Überall");
            add("Günstig");
        }
    };
    private static final ArrayList<Provider> VERIFIEDPROVIDERS = new ArrayList<Provider>(){
        {
            add(new Provider("ger_park_328_nds", "Parken in Oldenburg", "parken.stadt-oldenburg.de"));
        }
    };

    public static final int VERSION = 1;

    public ParkingServiceSoftware() {
        super(SOFTWARE_ID, PROVIDER, new Description(   
                    NAME,
                    DESCRIPTION,
                    BUZZWORDS,
                    ANGEBOTE,
                    ANGEBOT_TITEL,
                    ACTIONTYPES
                ),
                VERIFIEDPROVIDERS,
                VERSION
        );
    }

    @Override
    public void handleMessage(IMessage msg) {
        if(msg instanceof ServiceRegistrationMessage){
            handleServiceRegistration((ServiceRegistrationMessage)msg);
        }else if(msg instanceof ServiceDecisionMessage){
            handleServiceDecisionMessage((ServiceDecisionMessage)msg);
        }else if(msg instanceof ServiceActionCommand){
            handleServiceActionCommand((ServiceActionCommand)msg);
        }
    }

    /**
     * Puts the ServiceAngebot onto the MMS together with desciption etc
     * @param msg
     */
    private void handleServiceRegistration(ServiceRegistrationMessage msg){
        msg.setInstallSW(false);
        getMmsConnection().sendMessage(msg);
    }

    /**
     * Forwards the msg to Carla-Actor. In return we expect a ServiceActionCommand
     * @param msg
     */
    private void handleServiceDecisionMessage(ServiceDecisionMessage msg){
        if(msg.isAccepted()){
            getCarlaConnection().sendMessage(new CarlaMessage(4));
        }

    }

    /**
     * Forwards the ServiceActionMessage to MMS and Carla-Car.
     * @param msg
     */
    private void handleServiceActionCommand(ServiceActionCommand msg){
        getMmsConnection().sendMessage(msg);

    }
}
