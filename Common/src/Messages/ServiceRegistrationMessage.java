package Messages;

import EnvironmentObjects.ServiceDescription;

/**
 * @author Linus Hestermeyer
 *
 * The request the actor(i.e. parking-validator / traffic light) sends to the car in order to propose the use of its service.
 */
public class ServiceRegistrationMessage extends Message{
    private ServiceDescription description;
    private long inquiryID;

    public ServiceRegistrationMessage(ServiceDescription description, long inquiryID) {
        this.description = description;
        this.inquiryID = inquiryID;
    }

    public ServiceDescription getDescription() {
        return description;
    }

    public void setDescription(ServiceDescription description) {
        this.description = description;
    }

    public long getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(long inquiryID) {
        this.inquiryID = inquiryID;
    }
}
