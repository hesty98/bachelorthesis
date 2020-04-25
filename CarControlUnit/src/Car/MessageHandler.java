package Car;

import Actions.ActionEnums;
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

    private static MessageHandler handler;

    private MessageHandler() {
        this.carlaConnection = new CarlaClientConnection();
        this.mmsConnection = new MMSClientConnection();
        nettyClientInitializer = new NettyClientInitializer();
        this.swsConnection = (NettyConnectionClient)nettyClientInitializer.createNettyClientConnection(NetworkConfig.serverUrl,NetworkConfig.serverPort);

        //this.carlaConnection.initBootstrap("127.0.0.1", 22898);
        //this.mmsConnection.initBootstrap("127.0.0.1",28620);
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
    public void setEventBus(EventBus bus) {
        if(this.bus==null) {
            this.bus = bus;
            bus.register(this);
        }
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
                +"\n     Type: "+msg.getRequiredSWID()
                +"\n     Titel: "+msg.getDescription().getServiceTitle()
                +"\n     Description:"+msg.getDescription().getServiceDescription());
        final String serviceSoftwareID= msg.getRequiredSWID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);

        if(handlingSW != null && handlingSW.isUpTpDate()){
            /*handlingSW.handleMessage(msg);
              TODO: delete this and get SDMessage from MMS
             */
            ServiceDecisionMessage sdMSG = new ServiceDecisionMessage(msg.getInquiryID(), true,msg.getRequiredSWID(), msg.getRequiredSWID());
            bus.post(sdMSG);
        } else{
            registeringServices.add(msg);
            ServiceVerificationCommand cmd = new ServiceVerificationCommand("eins cooles auto manifesto", msg.getDescription(), msg.getRequiredSWID(),msg.getServiceID(),msg.getInquiryID());
            swsConnection.sendMessage(cmd);
            messageHandlerPresenter.printToSent("Sent ServiceVerificationCommand to OEM Verification Server.");
        }
    }

    @Subscribe
    public void waitForVerification(ServiceVerificationMessage serviceVerificationMessage){
        messageHandlerPresenter.printToReceived(
                     "\nReceived ServiceVerificationMessage. " +
                        "\n     Providor: "+ serviceVerificationMessage.getDesc().getServiceProvider()+
                        "\n     Description: "+serviceVerificationMessage.getDesc().getServiceDescription()+
                        "\n\nIn future, we need to Forward to MMS. Currently, Service always gets accepted and pushed to the intern eventbus right away."
        );

        if(serviceVerificationMessage.isVerified()){
            for(ServiceRegistrationMessage msg : registeringServices){
                if(serviceVerificationMessage.getRequiredSWID() == msg.getRequiredSWID()){
                    //mgr.submitSoftware(msg);
                    //TODO: get SoftwareDecisionMessage from MMS, delete lower and uncomment upper line
                    SoftwareDecisionMessage sdMSG = new SoftwareDecisionMessage(msg.getInquiryID(),true ,msg.getRequiredSWID());
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
        ServiceActionMessage sam = new ServiceActionMessage(cmd.getAction(), cmd.getServiceID(), cmd.getRequiredSWID());
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

            //TODO: DecisionMessage an CarlaEnv schicken und auf ServiceActionCommand von CarlaEnv warten; unten stehendes entfernen
            ServiceActionCommand serviceActionCommand = new ServiceActionCommand(ActionEnums.MOVEMENT, serviceDecisionMessage.getServiceID(), serviceDecisionMessage.getRequiredSWID());
            bus.post(serviceActionCommand);
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
            //TODO: forward InstallRequest to the SwS and send him the SoftwareInstallRequzest with swID contained. SwS returns the wanted Software.
            //sendToSWS();
            SoftwareInstallRequest req = new SoftwareInstallRequest(softwareDecisionMessage.getSoftwareID());
            sendToSWS(req);
        } else{
            System.err.println("Driver doesnt want to install Software!");
        }
    }


    @Subscribe
    public void waitForSoftwarePackage(SoftwareInstallationPackage installationPackage){
        Software toBeInstalled = installationPackage.getSoftware();

        //Kommunikationskanäle setzen
        toBeInstalled.setCarlaConnection(carlaConnection);
        toBeInstalled.setMmsConnection(mmsConnection);
        toBeInstalled.setSwsConnection(swsConnection);

        //Software hinzufügen
        mgr.installSoftware(toBeInstalled);

        for(ServiceRegistrationMessage msg : registeringServices){
            if(installationPackage.getSoftwareID() == msg.getRequiredSWID()){
                msg.setInstallSW(false);
                registeringServices.remove(msg);
                bus.post(msg);
            }
        }
    }

    /**
     * Method belongs to Carla Car, needed for GUI Log.
     *
     * @param serviceActionMessage
     */
    @Subscribe
    public void readyForAction(ServiceActionMessage serviceActionMessage){
        carlaPresenter.printToCar("Received a new Action, yeeeey!");
        sendToCarla(serviceActionMessage);
    }


    /**
     * TODO: Über NEtty und OEMServer machen, da gehört das hin.
     * @param msg
     */
    @Subscribe
    public void handleInstallRequest(SoftwareInstallRequest msg){
        SoftwareInstallationPackage sw= new SoftwareInstallationPackage(msg.getSoftwareID(), new ParkingServiceSoftware("Parken in Deutschalnds Städten",
                "Hier steht üblicherweise eine ausführliche Beschreibung der Software"));
        bus.post(sw);
    }

    public void post(IMessage msg){
        bus.post(msg);
    }
}
