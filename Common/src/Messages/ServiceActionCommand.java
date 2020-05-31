package Messages;

import Actions.IAction;
import EnvironmentObjects.Provider;

/**
 * 
 */
public class ServiceActionCommand extends ServiceMessage {
    //private ActionEnums action;
    private IAction action;

    public ServiceActionCommand(IAction action, Provider provider, String requiredSWID) {
        super(provider, requiredSWID);
        this.action = action;

    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

}