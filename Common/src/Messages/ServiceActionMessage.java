package Messages;

import Actions.ActionEnums;
import Actions.IAction;
import EnvironmentObjects.Service;

/**
 * @author Linus Hestermeyer
 *
 * Message which contains the action the car is supposed to do.
 */
public class ServiceActionMessage extends ServiceMessage {
    private ActionEnums action;
    private String serviceID;

    public ServiceActionMessage(ActionEnums action, String serviceID, String serviceSoftwareID) {
        super(serviceSoftwareID);
        this.serviceID = serviceID;
    }

    public ActionEnums getAction() {
        return action;
    }

    public void setAction(ActionEnums action) {
        this.action = action;
    }

    public String getCommunicatingService() {
        return serviceID;
    }

    public void setCommunicatingService(String serviceID) {
        this.serviceID = serviceID;
    }
}
