package Messages;

import EnvironmentObjects.ServiceProvider;

public class ServiceMessage extends Message {
    /**
     * Gets used from the Software so that this knows what to do.
     */
    private ServiceProvider serviceProvider;

    public ServiceMessage(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
