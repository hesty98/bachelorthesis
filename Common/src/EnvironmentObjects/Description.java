package EnvironmentObjects;

import Actions.IAction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Linus Hestermeyer
 *
 * Description of a Service.
 */
public class Description implements Serializable {
    private String serviceTitle;
    private String serviceDescription;
    private ArrayList<String> serviceBuzzwords;
    private Angebot angebot;
    private ArrayList<IAction> actionTypes;

    public Description(String serviceTitle, String serviceDescription, ArrayList<String> serviceBuzzwords, Angebot angebot, ArrayList<IAction> actionTypes) {
        this.serviceTitle = serviceTitle;
        this.serviceDescription = serviceDescription;
        this.serviceBuzzwords = serviceBuzzwords;
        this.angebot = angebot;
        this.actionTypes = actionTypes;
    }

    public String getTitle() {
        return serviceTitle;
    }

    public void setTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getDescription() {
        return serviceDescription;
    }

    public void setDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public ArrayList<String> getBuzzwords() {
        return serviceBuzzwords;
    }

    public void setBuzzwords(ArrayList<String> serviceBuzzwords) {
        this.serviceBuzzwords = serviceBuzzwords;
    }

    public Angebot getAngebot() {
        return angebot;
    }

    public void setAngebot(Angebot angebot) {
        this.angebot = angebot;
    }

    public ArrayList<IAction> getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(ArrayList<IAction> actionTypes) {
        this.actionTypes = actionTypes;
    }
}
