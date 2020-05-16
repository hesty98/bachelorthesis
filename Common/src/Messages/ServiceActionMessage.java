package Messages;

import Actions.IAction;
import EnvironmentObjects.ServiceProvider;

/**
 * @author Linus Hestermeyer
 *
 * Message which contains the action the car is supposed to do.
 */
public class ServiceActionMessage extends ServiceMessage {
    private IAction action;

    public ServiceActionMessage(IAction action, ServiceProvider serviceProvider) {
        super(serviceProvider);
        this.action=action;
    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

}
