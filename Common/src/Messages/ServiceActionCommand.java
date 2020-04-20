package Messages;

import Actions.ActionEnums;

/**
 * 
 */
public class ServiceActionCommand extends ServiceMessage {
    private ActionEnums action;

    public ServiceActionCommand(ActionEnums action, String serviceID,  String requiredSWID) {
        super(requiredSWID,serviceID);
        this.action = action;

    }

    public ActionEnums getAction() {
        return action;
    }

    public void setAction(ActionEnums action) {
        this.action = action;
    }

}