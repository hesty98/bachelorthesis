package Car.Software;

import EnvironmentObjects.Software;
import Initialization.CarlaConnection.CarlaClientConnection;
import Initialization.EventBus.CarlaPortModule;
import Initialization.EventBus.MMSPortModule;
import Initialization.MMSConnection.MMSClientConnection;
import Messages.*;
import com.google.inject.Inject;

/**
 * With this Software, simulated cars are supposed to park in a specific location.
 *
 * @version 1.0
 * @author Linus Hestermeyer
 */
public class ParkingServiceSoftware extends Software {
    @Inject
    private MMSClientConnection mmsClientConnection;
    @Inject
    private CarlaClientConnection carlaClientConnection;

    public static final String SOFTWARE_ID = "PARKING_SERVICE_GERMAN_CITIES";

    @Inject
    public ParkingServiceSoftware(String swName, String swDescription) {
        super(SOFTWARE_ID, swName, swDescription);
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
        this.mmsClientConnection.sendMessage(msg);
    }

    /**
     * Forwards the msg to Carla-Actor. In return we expect a ServiceActionCommand
     * @param msg
     */
    private void handleServiceDecisionMessage(ServiceDecisionMessage msg){
        this.carlaClientConnection.sendMessage(msg);
    }

    /**
     * Forwards the ServiceActionMessage to MMS and Carla-Car.
     * @param msg
     */
    private void handleServiceActionCommand(ServiceActionCommand msg){
        this.mmsClientConnection.sendMessage(msg);
        this.carlaClientConnection.sendMessage(msg);
    }
}
