package Messages;

import Actions.ActionEnums;
import Actions.IAction;
import EnvironmentObjects.Service;

public class ServiceActionCommand extends Message {
    private ActionEnums action;
    private Service communicatingService;

    public ServiceActionCommand(ActionEnums action, Service communicatingService) {
        this.action = action;
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