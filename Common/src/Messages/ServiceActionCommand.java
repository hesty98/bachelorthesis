package Messages;

import Actions.IAction;
import EnvironmentObjects.ServiceProvider;

/**
 * 
 */
public class ServiceActionCommand extends ServiceMessage {
    //private ActionEnums action;
    private IAction action;

    public ServiceActionCommand(IAction action, ServiceProvider serviceProvider) {
        super(serviceProvider);
        this.action = action;

    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

}