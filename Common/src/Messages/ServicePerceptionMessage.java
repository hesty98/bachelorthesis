package Messages;

public class ServicePerceptionMessage extends Message {
    /*TODO: fill with attributes provided by Carla for example:
        actorType == serviceTypeID
        actorID == ServiceProvider

     */
    //for MockUp:
    private String actorType;
    private String actorID;

    public ServicePerceptionMessage(String actorType, String actorID) {
        this.actorType = actorType;
        this.actorID = actorID;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public String getActorID() {
        return actorID;
    }

    public void setActorID(String actorID) {
        this.actorID = actorID;
    }
}
