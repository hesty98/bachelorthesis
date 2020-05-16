package Car;

import EnvironmentObjects.IConnectionClient;
import EnvironmentObjects.Software.Software;
import GUI.CCUMessageHandler.MessageHandlerPresenter;
import GUI.Carla.CarlaPresenter;
import Initialization.Network.CarlaClientConnection;
import Initialization.Network.MMSClientConnection;
import Initialization.Network.NetworkConfig;
import Initialization.Network.OEMVerificationServerConnection.NettyClientInitializer;
import Initialization.Network.OEMVerificationServerConnection.NettyConnectionClient;
import Messages.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class MessageHandler {
    private IConnectionClient carlaConnection;
    private IConnectionClient mmsConnection;

    private final NettyClientInitializer nettyClientInitializer;
    private NettyConnectionClient swsConnection;
    private EventBus bus;
    private CarlaPresenter carlaPresenter;
    private MessageHandlerPresenter messageHandlerPresenter;
    private SoftwareManager mgr;

    private static MessageHandler handler;

    private MessageHandler() {
        this.bus=new EventBus();
        bus.register(this);
        this.carlaConnection = new CarlaClientConnection(bus);
        this.mmsConnection = new MMSClientConnection(bus);
        this.swsConnection= new NettyConnectionClient(bus);
        nettyClientInitializer = new NettyClientInitializer(swsConnection);
        this.swsConnection = (NettyConnectionClient)nettyClientInitializer.createNettyClientConnection(NetworkConfig.serverUrl,NetworkConfig.serverPort);

        //this.carlaConnection.initBootstrap("127.0.0.1", 22898);
        this.mmsConnection.initBootstrap("127.0.0.1",22620);
        //this.swsConnection.initBootstrap
        this.mgr =new SoftwareManager();
    }

    public static MessageHandler getInstance(){
        if(handler ==null){
            handler=new MessageHandler();
        }
        return handler;
    }
    public void sendToCarla(IMessage out){
        if(((CarlaClientConnection)this.carlaConnection).isRunning()) {
            this.carlaConnection.sendMessage(out);
        }

    }
    public void sendToMMS(IMessage out){
        if(((MMSClientConnection)this.mmsConnection).isRunning()){
            this.mmsConnection.sendMessage(out);
        } else {
            messageHandlerPresenter.printToSent("Tried to send "+out.toString()+ "but mms is not connected;");
        }
    }
    public void sendToSWS(IMessage out){
        this.swsConnection.sendMessage(out);
    }
    public void setMessageHandlerPresenter(MessageHandlerPresenter messageHandlerPresenter) {
        this.messageHandlerPresenter=messageHandlerPresenter;
    }
    public void setCarlaPresenter(CarlaPresenter carlaPresenter) {
        this.carlaPresenter=carlaPresenter;
    }

    private ArrayList<ServiceRegistrationMessage> registeringServices = new ArrayList<>();

    /**
     * Reads ServiceRegistrationMessages and passes them to the software being able to handle this Message.
     *
     * @param msg
     */
    @Subscribe
    public void registerService(ServiceRegistrationMessage msg){
        messageHandlerPresenter.printToReceived("Received new ServiceRegistrationService. InquiryID: "+ msg.getInquiryID()
                +"\n     Type: "+msg.getServiceProvider().getProviderName()
                +"\n     Titel: "+msg.getDescription().getTitle()
                +"\n     Description:"+msg.getDescription().getDescription()
                +"\n     Install:"+msg.isInstallSW());
        final String serviceSoftwareID= msg.getServiceProvider().getRequiredSoftwareID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);

        if(handlingSW != null && handlingSW.isUpTpDate()){
            //Wait for ServiceDecisionMessage from MMS when this is sent
            System.err.println("necessary SW already installed "+ msg.isInstallSW());
            mmsConnection.sendMessage(msg);
        } else{
            msg.setInstallSW(true);
            registeringServices.add(msg);
            ServiceVerificationCommand cmd = new ServiceVerificationCommand("eins cooles auto manifesto", msg.getDescription(), msg.getServiceProvider(),msg.getInquiryID());
            swsConnection.sendMessage(cmd);
            messageHandlerPresenter.printToSent("Sent ServiceVerificationCommand to OEM Verification Server.");
        }
    }

    @Subscribe
    public void waitForVerification(ServiceVerificationMessage serviceVerificationMessage){
        messageHandlerPresenter.printToReceived(
                     "\nReceived ServiceVerificationMessage. " +
                        "\n     Providor: "+ serviceVerificationMessage.getServiceProvider().getProviderName()+
                        "\n     Description: "+serviceVerificationMessage.getDesc().getDescription()+
                        "\n\nIn future, we need to Forward to MMS. Currently, Service always gets accepted and pushed to the intern eventbus right away."
        );

        if(serviceVerificationMessage.isVerified()){
            for(ServiceRegistrationMessage msg : registeringServices){
                if(serviceVerificationMessage.getServiceProvider().getRequiredSoftwareID().equals(msg.getServiceProvider().getRequiredSoftwareID())){
                    System.err.println(msg +": "+ msg.isInstallSW());
                    mgr.submitSoftware(msg);
                }
            }
        } else{
            System.err.println("Unverified SW got recommended!");
        }
    }

    @Subscribe
    public void handleActionCommand(ServiceActionCommand cmd){
        messageHandlerPresenter.printToReceived("\n"
                + "Received ServiceActionCommand. Creating verified Message and forwarding to carla."
        );
        final String serviceSWID =cmd.getServiceProvider().getRequiredSoftwareID();
        Software handlingSW = mgr.getSoftware(serviceSWID);
        handlingSW.handleMessage(cmd);
        ServiceActionMessage sam = new ServiceActionMessage(cmd.getAction(), cmd.getServiceProvider());
        bus.post(sam);
    }

    @Subscribe
    public void waitForDecisions(ServiceDecisionMessage serviceDecisionMessage){
        final String serviceSoftwareID= serviceDecisionMessage.getServiceProvider().getRequiredSoftwareID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);
        if(handlingSW!=null) {
            handlingSW.handleMessage(serviceDecisionMessage);
        }else{
            System.err.println("No Software handling the message. -> MessageHandler");
        }
        if(serviceDecisionMessage.isAccepted()) {
            messageHandlerPresenter.printToReceived(
                            "\nDriver accepted to use the Service! Forwarding message to Carla-Environment."
            );
            carlaPresenter.printToEnvironment(
                    "\nDriver accepted to use the Service! Ready to send the ServiceActionCommand");

            carlaPresenter.currentStage = CarlaPresenter.STAGE.CAR_ACCEPTED_SERVICE;
            carlaPresenter.setUpButtons();
        }else{
            messageHandlerPresenter.printToReceived(
                            "\nDriver did not accept to use the Service! Forwarding message to Carla-Environment (TODO)."
            );
        }
    }


    /**
     * Sends a SWInstallRequest to the OEMVerificationServer. Return expected form server: SoftwareInstallationMessage
     * @param softwareDecisionMessage
     */
    @Subscribe
    public void waitForSoftwareDecisions(SoftwareDecisionMessage softwareDecisionMessage){
        if(softwareDecisionMessage.isAccepted()){
            SoftwareInstallRequest req = new SoftwareInstallRequest(softwareDecisionMessage.getSoftwareID());
            messageHandlerPresenter.printToSent("Requesting a SoftwareInstallationPackage form the Server...");
            sendToSWS(req);
        } else{
            System.err.println("Driver doesnt want to install Software!");
        }
    }


    @Subscribe
    public void waitForSoftwarePackage(SoftwareInstallationPackage installationPackage){
        messageHandlerPresenter.printToReceived("A new Software has been sent by the Server: "+installationPackage.getSoftware().getDescription().getTitle());
        Software toBeInstalled = installationPackage.getSoftware();

        //Kommunikationskanäle setzen
        toBeInstalled.setCarlaConnection(carlaConnection);
        toBeInstalled.setMmsConnection(mmsConnection);
        //toBeInstalled.setSwsConnection(swsConnection);

        //Software hinzufügen
        mgr.installSoftware(toBeInstalled);

        ServiceRegistrationMessage registrationMessage = null;
        for(ServiceRegistrationMessage msg : registeringServices){
            if(installationPackage.getSoftwareID().equals(msg.getServiceProvider().getRequiredSoftwareID())){
                registrationMessage =msg;
                registrationMessage.setInstallSW(false);
            }
        }
        registeringServices.remove(registrationMessage);
        bus.post(registrationMessage);
    }

    /**
     * Method belongs to Carla Car, needed for GUI Log.
     *
     * @param serviceActionMessage
     */
    @Subscribe
    public void readyForAction(ServiceActionMessage serviceActionMessage){
        carlaPresenter.printToCar("My Car is now parking!");
        sendToCarla(serviceActionMessage);
    }

    public void post(IMessage msg){
        bus.post(msg);
    }
}
