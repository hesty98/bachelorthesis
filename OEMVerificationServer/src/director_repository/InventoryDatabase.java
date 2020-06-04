package director_repository;

import java.util.HashMap;

public class InventoryDatabase {

    public static final String CAR_1_ID = "CAR-LA-1";
    public static final String CAR_1_MANIFEST =
            "Software:\r\n" +
            "  DriveAutomaticBasic: 1.0\r\n" +
            "  DriveInMcDonalds: 1.2\r\n";
    private HashMap<String, String> manifests;
    private static InventoryDatabase database;

    public InventoryDatabase() {
        this.manifests = new HashMap<>();
        manifests.put(CAR_1_ID, CAR_1_MANIFEST);
    }

    public static InventoryDatabase getInstance(){
        if(database == null){
            database = new InventoryDatabase();
        }
        return database;
    }

    public String getManifestByKey(String carID){
        return manifests.get(carID);
    }
}
