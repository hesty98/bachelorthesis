package databases;

import EnvironmentObjects.Software.ParkingServiceSoftware;
import EnvironmentObjects.Software.Software;

import java.util.HashMap;

public class SoftwareDatabase {
    private HashMap<String, Software> softwares;
    private static SoftwareDatabase database;

    public SoftwareDatabase() {
        this.softwares = new HashMap<>();
        softwares.put(ParkingServiceSoftware.SOFTWARE_ID, new ParkingServiceSoftware());
    }

    public static SoftwareDatabase getInstance(){
        if(database == null){
            database = new SoftwareDatabase();
        }
        return database;
    }

    public Software getSoftwareByKey(String swID){
        return softwares.get(swID);
    }
}
