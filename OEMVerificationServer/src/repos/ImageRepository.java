package repos;

import EnvironmentObjects.Software.ParkingServiceSoftware;
import EnvironmentObjects.Software.Software;

import java.util.HashMap;

public class ImageRepository {
    private HashMap<String, Software> softwares;
    private static ImageRepository database;

    public ImageRepository() {
        this.softwares = new HashMap<>();
        softwares.put(ParkingServiceSoftware.SOFTWARE_ID, new ParkingServiceSoftware());
    }

    public static ImageRepository getInstance(){
        if(database == null){
            database = new ImageRepository();
        }
        return database;
    }

    public Software getSoftwareByKey(String swID){
        return softwares.get(swID);
    }
}
