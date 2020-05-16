package Messages;

import EnvironmentObjects.Description;
import EnvironmentObjects.ServiceProvider;

public class ServiceVerificationCommand extends ServiceMessage{
    private String car_manifest;
    private Description description;
    private long inquiryID;

    public ServiceVerificationCommand(String car_manifest, Description description, ServiceProvider serviceProvider, long inquiryID) {
        super(serviceProvider);
        this.car_manifest = car_manifest;
        this.description = description;
        this.inquiryID=inquiryID;
    }

    public String getCar_manifest() {
        return car_manifest;
    }

    public void setCar_manifest(String car_manifest) {
        this.car_manifest = car_manifest;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public long getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(long inquiryID) {
        this.inquiryID = inquiryID;
    }
}
