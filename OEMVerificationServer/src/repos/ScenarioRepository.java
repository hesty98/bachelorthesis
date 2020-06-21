package repos;

import java.util.HashMap;

public class ScenarioRepository {
    private HashMap<String, String> scenarios;
    private static ScenarioRepository database;

    public ScenarioRepository() {
        this.scenarios = new HashMap<>();
        //add scenarios
    }

    public static ScenarioRepository getInstance(){
        if(database == null){
            database = new ScenarioRepository();
        }
        return database;
    }

    public String getScenario(String swID){
        return scenarios.get(swID);
    }
}
