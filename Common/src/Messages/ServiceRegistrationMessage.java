package Messages;

import EnvironmentObjects.ServiceDescription;

/**
 * @author Linus Hestermeyer
 *
 *
 * Muss:
 *   Software für Service vorstellen
 *   Kosten des ServiceProviders aufzeigen -> elternklasse: Angebot
 *
 * The request the actor(i.e. parking-validator / traffic light) sends to the car in order to propose the use of its service.
 */
public class ServiceRegistrationMessage extends ServiceMessage{
    private ServiceDescription description;
    private long inquiryID;
    private boolean installSW;

    public ServiceRegistrationMessage(ServiceDescription description, long inquiryID, String serviceSoftwareID, String requiredSWID) {
        super(requiredSWID,serviceSoftwareID);
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

    public boolean isInstallSW() {
        return installSW;
    }

    public void setInstallSW(boolean installSW) {
        this.installSW = installSW;
    }
}
