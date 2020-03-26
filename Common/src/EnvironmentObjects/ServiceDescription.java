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
    private String softwareID;
    private String servceDescription;
    //private ArrayList<Image> images;
    //private ArrayList<String> imageDescriptions
    private String serviceTitle;
    private ArrayList<ActionEnums> actionTypes;
    private Angebot softwareAngebot;
    private String serviceVerificationURL;
    private String serviceProvider;
    private float pricePerHour;

    /**
     *
     * @param softwareID if it is contained in installed softwareIDs
     * @param servceDescription
     * @param serviceTitle
     * @param actionTypes
     * @param softwareAngebot
     * @param serviceVerificationURL
     * @param serviceProvider
     * @param pricePerHour
     */
    public ServiceDescription(String softwareID, String servceDescription, String serviceTitle, ArrayList<ActionEnums> actionTypes, Angebot softwareAngebot, String serviceVerificationURL, String serviceProvider, float pricePerHour) {
        this.softwareID = softwareID;
        this.servceDescription = servceDescription;
        this.serviceTitle = serviceTitle;
        this.actionTypes = actionTypes;
        this.softwareAngebot = softwareAngebot;
        this.serviceVerificationURL = serviceVerificationURL;
        this.serviceProvider=serviceProvider;

        //TODO: auslagern in ParkingServiceDescription(extends ServiceDescription)
        this.pricePerHour=pricePerHour;
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

    public String getSoftwareID() {
        return softwareID;
    }

    public void setSoftwareID(String softwareID) {
        this.softwareID = softwareID;
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

    public Angebot getSoftwareAngebot() {
        return softwareAngebot;
    }

    public void setSoftwareAngebot(Angebot softwareAngebot) {
        this.softwareAngebot = softwareAngebot;
    }
}
