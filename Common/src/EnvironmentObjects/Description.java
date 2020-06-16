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
    private String title;
    private String description;
    private ArrayList<String> buzzwords;
    private ArrayList<Angebot> angebote;
    private ArrayList<String> angebotTitel;
    private ArrayList<IAction> actionTypes;

    public Description(String title, String description, ArrayList<String> buzzwords, ArrayList<Angebot> angebote,ArrayList<String> angebotTitel, ArrayList<IAction> actionTypes) {
        this.title = title;
        this.description = description;
        this.buzzwords = buzzwords;
        this.angebote = angebote;
        this.angebotTitel=angebotTitel;
        this.actionTypes = actionTypes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String serviceTitle) {
        this.title = serviceTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String serviceDescription) {
        this.description = serviceDescription;
    }

    public ArrayList<String> getBuzzwords() {
        return buzzwords;
    }

    public void setBuzzwords(ArrayList<String> serviceBuzzwords) {
        this.buzzwords = serviceBuzzwords;
    }

    public ArrayList<IAction> getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(ArrayList<IAction> actionTypes) {
        this.actionTypes = actionTypes;
    }

    public ArrayList<Angebot> getAngebote() {
        return angebote;
    }

    public void setAngebote(ArrayList<Angebot> angebote) {
        this.angebote = angebote;
    }

    public ArrayList<String> getAngebotTitel() {
        return angebotTitel;
    }

    public void setAngebotTitel(ArrayList<String> angebotTitel) {
        this.angebotTitel = angebotTitel;
    }
}
