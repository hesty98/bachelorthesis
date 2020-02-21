package Messages;

/**
 * @author Linus Hestermeyer
 *
 * Message which inits a Software-Search on the server.
 */
public class SoftwareSearchRequest extends Message {
    private String openScenarioFile; //TODO: to this different.
    private long requestingCarID;
    private String carManifest;
}
