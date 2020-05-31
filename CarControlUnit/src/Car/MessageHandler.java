package Car;

import EnvironmentObjects.IConnectionClient;
import EnvironmentObjects.Software.ParkingServiceSoftware;
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

    private String MANIFEST;
    private static MessageHandler handler;

    private MessageHandler() {
        this.bus=new EventBus();
        bus.register(this);
        this.carlaConnection = new CarlaClientConnection(bus);
        this.mmsConnection = new MMSClientConnection(bus);
        this.swsConnection= new NettyConnectionClient(bus);
        nettyClientInitializer = new NettyClientInitializer(swsConnection);
        this.swsConnection = (NettyConnectionClient)nettyClientInitializer.createNettyClientConnection(NetworkConfig.serverUrl,NetworkConfig.serverPort);

        this.carlaConnection.initBootstrap("127.0.0.1", 22898);
        this.mmsConnection.initBootstrap("127.0.0.1",22620);
        this.MANIFEST ="Manifest\r\n";
        this.mgr =new SoftwareManager();
        this.mgr.installSoftware(new ParkingServiceSoftware());
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

    public String getMANIFEST() {
        return MANIFEST;
    }

    public void setMANIFEST(String MANIFEST) {
        this.MANIFEST = MANIFEST;
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
                +"\n     Providername: "+msg.getProvider().getProviderName()
                +"\n     Service title: "+msg.getDescription().getTitle()
                +"\n     Service description:"+msg.getDescription().getDescription());

        final String serviceSoftwareID= msg.getRequiredSWID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);

        if(handlingSW != null && handlingSW.isUpTpDate()){
            System.err.println("necessary SW already installed ");
            mmsConnection.sendMessage(msg);
        } else{
            msg.setInstallSW(true);
            registeringServices.add(msg);
            SoftwareContentRequest cmd = new SoftwareContentRequest(msg.getDescription(), msg.getProvider(),msg.getInquiryID(), msg.getRequiredSWID());
            swsConnection.sendMessage(cmd);
            messageHandlerPresenter.printToSent("Sent SoftwareContentRequest to OEM Verification Server.");
        }
    }

    @Subscribe
    public void waitForVerification(SoftwareContentMessage softwareContentMessage){
        messageHandlerPresenter.printToReceived(
                     "\nReceived ServiceVerificationMessage. " +
                        "\n     Providor: "+ softwareContentMessage.getProvider().getProviderName()+
                        "\n     Description: "+ softwareContentMessage.getDesc().getDescription()+
                        "\n\nIn future, we need to Forward to MMS. Currently, Service always gets accepted and pushed to the intern eventbus right away."
        );

        if(softwareContentMessage.isVerified()){
            for(ServiceRegistrationMessage msg : registeringServices){
                if(softwareContentMessage.getSoftwareID().equals(msg.getRequiredSWID())){
                    SoftwareRegistrationMessage swRegistration = new SoftwareRegistrationMessage(
                            softwareContentMessage.getDesc(),
                            softwareContentMessage.getInquiryID(),
                            softwareContentMessage.getSoftwareID(),
                            softwareContentMessage.getProvider()
                    );
                    MessageHandler.getInstance().sendToMMS(swRegistration);
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
        final String serviceSWID =cmd.getRequiredSWID();
        Software handlingSW = mgr.getSoftware(serviceSWID);
        handlingSW.handleMessage(cmd);
        ServiceActionMessage sam = new ServiceActionMessage(cmd.getAction(), cmd.getProvider(), cmd.getRequiredSWID());
        bus.post(sam);
    }

    @Subscribe
    public void waitForDecisions(ServiceDecisionMessage serviceDecisionMessage){
        final String serviceSoftwareID= serviceDecisionMessage.getRequiredSWID();
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
            SoftwareInstallRequest req = new SoftwareInstallRequest(
                    softwareDecisionMessage.getSoftwareID(),
                    softwareDecisionMessage.getProvider(),
                    getMANIFEST());
            messageHandlerPresenter.printToSent("Requesting a SoftwareInstallationPackage form the Server...");
            sendToSWS(req);
        } else{
            System.err.println("Driver doesn't want to install Software, driving away!");
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


        //Now that Software is installed, if a Sevrice is waiting for this to be installed the Process must continue
        ServiceRegistrationMessage registrationMessage = null;
        for(ServiceRegistrationMessage msg : registeringServices){
            if(installationPackage.getSoftwareID().equals(msg.getRequiredSWID())
                    && msg.isInstallSW()){
                registrationMessage =msg;
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
        //sendToCarla(serviceActionMessage);
    }

    public void post(IMessage msg){
        bus.post(msg);
    }
}
