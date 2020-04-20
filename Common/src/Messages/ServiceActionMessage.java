package Messages;

import Actions.ActionEnums;

/**
 * @author Linus Hestermeyer
 *
 * Message which contains the action the car is supposed to do.
 */
public class ServiceActionMessage extends ServiceMessage {
    private ActionEnums action;

    public ServiceActionMessage(ActionEnums action, String serviceID, String requiredSWID) {
        super(requiredSWID, serviceID);
        this.action=action;
    }

    public ActionEnums getAction() {
        return action;
    }

    public void setAction(ActionEnums action) {
        this.action = action;
    }

}
