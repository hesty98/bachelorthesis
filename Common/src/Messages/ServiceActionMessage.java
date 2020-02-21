package Messages;

import Actions.IAction;
import EnvironmentObjects.Service;

/**
 * @author Linus Hestermeyer
 *
 * Message which contains the action the car is supposed to do.
 */
public class ServiceActionMessage extends Message {
    private IAction action;
    private Service communicatingService;

    public ServiceActionMessage(IAction action, Service communicatingService) {
        this.action = action;
        this.communicatingService = communicatingService;
    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

    public Service getCommunicatingService() {
        return communicatingService;
    }

    public void setCommunicatingService(Service communicatingService) {
        this.communicatingService = communicatingService;
    }
}
