package Messages;

import Actions.ActionEnums;
import EnvironmentObjects.Service;

public class ServiceActionCommand extends ServiceMessage {
    private ActionEnums action;
    private Service communicatingService;

    public ServiceActionCommand(ActionEnums action, Service communicatingService, String serviceSoftwareID) {
        super(serviceSoftwareID);
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