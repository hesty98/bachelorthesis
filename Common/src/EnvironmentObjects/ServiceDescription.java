package EnvironmentObjects;

import Actions.ActionEnums;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Linus Hestermeyer
 *
 * Description of a Service.
 */
public class ServiceDescription implements Serializable {
    private String serviceTitle;
    private String serviceDescription;
    private ServiceProvider serviceProvider;
    private String requiredSWID;
    private ArrayList<String> serviceBuzzwords;
    private Angebot angebot;
    private ArrayList<ActionEnums> actionTypes;

    public ServiceDescription(String serviceTitle, String serviceDescription, ServiceProvider serviceProvider, String requiredSWID, ArrayList<String> serviceBuzzwords, Angebot angebot, ArrayList<ActionEnums> actionTypes) {
        this.serviceTitle = serviceTitle;
        this.serviceDescription = serviceDescription;
        this.serviceProvider = serviceProvider;
        this.requiredSWID = requiredSWID;
        this.serviceBuzzwords = serviceBuzzwords;
        this.angebot = angebot;
        this.actionTypes = actionTypes;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getRequiredSWID() {
        return requiredSWID;
    }

    public void setRequiredSWID(String requiredSWID) {
        this.requiredSWID = requiredSWID;
    }

    public ArrayList<String> getServiceBuzzwords() {
        return serviceBuzzwords;
    }

    public void setServiceBuzzwords(ArrayList<String> serviceBuzzwords) {
        this.serviceBuzzwords = serviceBuzzwords;
    }

    public Angebot getAngebot() {
        return angebot;
    }

    public void setAngebot(Angebot angebot) {
        this.angebot = angebot;
    }

    public ArrayList<ActionEnums> getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(ArrayList<ActionEnums> actionTypes) {
        this.actionTypes = actionTypes;
    }
}
