package EnvironmentObjects.Software;

import Actions.IAction;
import Actions.MovementHintAction;
import Actions.TargetAction;
import EnvironmentObjects.Angebot;
import EnvironmentObjects.Description;
import Messages.*;
import com.google.inject.Inject;

import java.util.ArrayList;

/**
 * With this Software, simulated cars are supposed to park in a specific location.
 *
 * @version 1.0
 * @author Linus Hestermeyer
 */
public class ParkingServiceSoftware extends Software {
    public static final String SOFTWARE_ID = "PARKING_SERVICE_GERMAN_CITIES";
    private static final String PROVIDER_ID = "GER_28";
    private static final String NAME = "Parking in Germany";
    private static final String DESCRIPTION = "Mit dieser SOftware kann in jeder Stadt Deutschlands auf den Parkplätzen geparkt werden.";
    private static final Angebot ANGEBOT = new Angebot(2000);
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


    @Inject
    public ParkingServiceSoftware() {
        super(SOFTWARE_ID,
                PROVIDER_ID,
                new Description(
                NAME,
                DESCRIPTION,
                BUZZWORDS,
                ANGEBOT,
                ACTIONTYPES
        ));
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
        getCarlaConnection().sendMessage(msg);
    }

    /**
     * Forwards the ServiceActionMessage to MMS and Carla-Car.
     * @param msg
     */
    private void handleServiceActionCommand(ServiceActionCommand msg){
        getMmsConnection().sendMessage(msg);
        getCarlaConnection().sendMessage(msg);
    }
}
