package databases;

import EnvironmentObjects.Software.ParkingServiceSoftware;
import EnvironmentObjects.Software.Software;

import java.util.HashMap;

public class ScenarioDatabase {
    private HashMap<String, String> scenarios;
    private static ScenarioDatabase database;

    public ScenarioDatabase() {
        this.scenarios = new HashMap<>();
        //add scenarios
    }

    public static ScenarioDatabase getInstance(){
        if(database == null){
            database = new ScenarioDatabase();
        }
        return database;
    }

    public String getScenario(String swID){
        return scenarios.get(swID);
    }
}
