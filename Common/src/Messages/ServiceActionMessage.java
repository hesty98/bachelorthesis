package Messages;

import Actions.IAction;
import EnvironmentObjects.Provider;

/**
 * @author Linus Hestermeyer
 *
 * Message which contains the action the car is supposed to do.
 */
public class ServiceActionMessage extends ServiceMessage {
    private IAction action;

    public ServiceActionMessage(IAction action, Provider provider, String requiredSWID) {
        super(provider, requiredSWID);
        this.action=action;
    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

}
