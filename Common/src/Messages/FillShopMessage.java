package Messages;

import EnvironmentObjects.Software.Software;

import java.util.ArrayList;

public class FillShopMessage extends Message {
    private ArrayList<Software> softwares;
    private ArrayList<Software> advertisedSoftwares;

    public FillShopMessage(ArrayList<Software> softwares, ArrayList<Software> advertisedSoftwares) {
        this.softwares = softwares;
        this.advertisedSoftwares = advertisedSoftwares;
    }

    public ArrayList<Software> getSoftwares() {
        return softwares;
    }

    public void setSoftwares(ArrayList<Software> softwares) {
        this.softwares = softwares;
    }

    public ArrayList<Software> getAdvertisedSoftwares() {
        return advertisedSoftwares;
    }

    public void setAdvertisedSoftwares(ArrayList<Software> advertisedSoftwares) {
        this.advertisedSoftwares = advertisedSoftwares;
    }
}
