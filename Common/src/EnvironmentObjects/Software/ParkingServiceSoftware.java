package EnvironmentObjects.Software;

import Messages.*;
import com.google.inject.Inject;

/**
 * With this Software, simulated cars are supposed to park in a specific location.
 *
 * @version 1.0
 * @author Linus Hestermeyer
 */
public class ParkingServiceSoftware extends Software {

    public static final String SOFTWARE_ID = "PARKING_SERVICE_GERMAN_CITIES";

    @Inject
    public ParkingServiceSoftware(String swName, String swDescription) {
        super(SOFTWARE_ID, swName, swDescription, SOFTWARE_ID);
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
