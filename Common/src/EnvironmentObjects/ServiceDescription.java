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
    private String serviceTypeID;
    private String servceDescription;
    //private ArrayList<Image> images;
    //private ArrayList<String> imageDescriptions
    private String serviceTitle;
    private ArrayList<ActionEnums> actionTypes;
    private Angebot angebot;
    private String serviceVerificationURL;
    private String serviceProvider;

    public ServiceDescription(String serviceTypeID, String servceDescription, String serviceTitle, ArrayList<ActionEnums> actionTypes, Angebot angebot, String serviceVerificationURL, String serviceProvider) {
        this.serviceTypeID = serviceTypeID;
        this.servceDescription = servceDescription;
        this.serviceTitle = serviceTitle;
        this.actionTypes = actionTypes;
        this.angebot = angebot;
        this.serviceVerificationURL = serviceVerificationURL;
        this.serviceProvider=serviceProvider;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getServiceVerificationURL() {
        return serviceVerificationURL;
    }

    public void setServiceVerificationURL(String serviceVerificationURL) {
        this.serviceVerificationURL = serviceVerificationURL;
    }

    public String getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(String serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getServceDescription() {
        return servceDescription;
    }

    public void setServceDescription(String servceDescription) {
        this.servceDescription = servceDescription;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public ArrayList<ActionEnums> getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(ArrayList<ActionEnums> actionTypes) {
        this.actionTypes = actionTypes;
    }

    public Angebot getAngebot() {
        return angebot;
    }

    public void setAngebot(Angebot angebot) {
        this.angebot = angebot;
    }
}
