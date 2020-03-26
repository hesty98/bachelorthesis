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
    private Service communicatingService;

    public ServiceActionMessage(ActionEnums action, Service communicatingService, String serviceSoftwareID) {
        super(serviceSoftwareID);
        this.communicatingService = communicatingService;
    }

    public ActionEnums getAction() {
        return action;
    }

    public void setAction(ActionEnums action) {
        this.action = action;
    }

    public Service getCommunicatingService() {
        return communicatingService;
    }

    public void setCommunicatingService(Service communicatingService) {
        this.communicatingService = communicatingService;
    }
}
