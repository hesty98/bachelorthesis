package Messages;

import EnvironmentObjects.ServiceDescription;

public class ServiceVerificationCommand extends Message{
    private String car_manifest;
    private ServiceDescription serviceDescription;
    private long inquiryID;

    public ServiceVerificationCommand(String car_manifest,ServiceDescription serviceDescription, long inquiryID) {
        this.car_manifest = car_manifest;
        this.serviceDescription=serviceDescription;
        this.inquiryID=inquiryID;
    }

    public String getCar_manifest() {
        return car_manifest;
    }

    public void setCar_manifest(String car_manifest) {
        this.car_manifest = car_manifest;
    }

    public ServiceDescription getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(ServiceDescription serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public long getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(long inquiryID) {
        this.inquiryID = inquiryID;
    }
}
