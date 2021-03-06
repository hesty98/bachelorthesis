package Car;

import Actions.GoAwayAction;
import Actions.TargetAction;
import EnvironmentObjects.IConnectionClient;
import EnvironmentObjects.Provider;
import EnvironmentObjects.Software.Software;
import GUI.CCUMessageHandler.MessageHandlerPresenter;
import GUI.Carla.CarlaPresenter;
import Network.CarlaClientConnection;
import Network.MMSClientConnection;
import Network.NetworkConfig;
import Network.OEMVerificationServerConnection.NettyClientInitializer;
import Network.OEMVerificationServerConnection.NettyConnectionClient;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import Messages.*;

import java.util.ArrayList;

public class MessageHandler {


    public static String MANIFEST =
            "Software:\r\n" +
                    "  DriveAutomaticBasic: 1.0\r\n" +
                    "  DriveInMcDonalds: 1.2\r\n";

    public static String MANIFEST_HACKED =
            "Software:\r\n" +
                    "  DriveAutomaticBasic: 1.0\r\n" +
                    "  DriveInMcDonalds: 1.2\r\n"+
                    "hacked muhahahaha";

    public boolean hacked = false;
    public boolean suggestionReady = true;
    public boolean providerVerified = true;

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

        this.carlaConnection.initBootstrap("127.0.0.1", 22898);
        this.mmsConnection.initBootstrap("127.0.0.1",22620);
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
            messageHandlerPresenter.printToLog("Tried to send "+out.toString()+ "but mms is not connected;");
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
        final String serviceSoftwareID= msg.getRequiredSWID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);
        if(handlingSW != null && handlingSW.isUpTpDate()){
            boolean found = false;
            for(Provider provider : handlingSW.getVerifiedServiceProviders()){
                if(provider.getPublicProviderID() == msg.getProvider().getPublicProviderID())
                    found= true;
            }
            if(!found && !providerVerified){//todo: verschönern, damit der boolean nicht weiter benötigt wird.
                messageHandlerPresenter.printToLog(
                        "Service wird nicht vorgeschlagen, da der Service Provider nicht verifiziert ist:"+ msg.getDescription().getTitle());

                carlaPresenter.currentStage = CarlaPresenter.STAGE.CAR_DECLINED_SERVICE_OR_SOFTWARE;
                carlaPresenter.setUpButtons();
                return;
            }
            messageHandlerPresenter.printToLog(
                    "Service wird zur Nutzung vorgeschlagen! "+ msg.getDescription().getTitle());

            mmsConnection.sendMessage(msg);

            carlaPresenter.currentStage = CarlaPresenter.STAGE.CAR_INSTALLING_SW;
            carlaPresenter.setUpButtons();
        } else{
            messageHandlerPresenter.printToLog(
                    "Service möchte sich registrieren!"
                    +"\n   Für den Service wird eine neue Software benötigt! Speichere Anfrage... InquiryID: "+ msg.getInquiryID()
                    +"\n     Provider name: "+msg.getProvider().getProviderName()
                    +"\n     Service: "+msg.getDescription().getTitle()
                    +"\n     Beschreibung: "+msg.getDescription().getDescription()
                    +"\n     Software ID: "+msg.getRequiredSWID()
            );

            msg.setInstallSW(true);
            registeringServices.add(msg);
            SoftwareContentRequest cmd = new SoftwareContentRequest(msg.getDescription(), msg.getProvider(),msg.getInquiryID(), msg.getRequiredSWID());
            swsConnection.sendMessage(cmd);
            messageHandlerPresenter.printToLog("Software Beschreibung wird heruntergeladen.");

            carlaPresenter.currentStage = CarlaPresenter.STAGE.CAR_INSTALLING_SW;
            carlaPresenter.setUpButtons();
        }
    }

    @Subscribe
    public void waitForVerification(SoftwareContentMessage softwareContentMessage){
        messageHandlerPresenter.printToLog("Software Beschreibung vom Server erhalten:"
                +"\n     Software ID: "+softwareContentMessage.getDesc().getTitle()
                +"\n     Entwickler: "+softwareContentMessage.getProvider().getProviderName()
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
                    new Thread(){
                        @Override
                        public void run() {
                            while(true) {
                                System.err.println(suggestionReady);
                                if(suggestionReady) {
                                    sendToMMS(swRegistration);
                                    break;
                                }
                            }

                        }
                    }.start();
                }
            }
        } else{
            System.err.println("Unverified SW got recommended!");
        }
    }

    @Subscribe
    public void handleActionCommand(ServiceActionCommand cmd){
        if(cmd.getAction() instanceof GoAwayAction){
            messageHandlerPresenter.printToLog("Fahranweisung vom Parkautomaten erhalten, ich entferne mich.");
        }else if (cmd.getAction() instanceof TargetAction){
            messageHandlerPresenter.printToLog("Parkticket gebucht und Parkplatz zugewiesen bekommen, ich parke!");

        }
        if(cmd.getAction() instanceof GoAwayAction)
            sendToCarla(new CarlaMessage(5));
        else
            sendToCarla(new CarlaMessage(4));

        final String serviceSWID =cmd.getRequiredSWID();
        Software handlingSW = mgr.getSoftware(serviceSWID);
        if(handlingSW != null)
            handlingSW.handleMessage(cmd);

    }

    @Subscribe
    public void waitForDecisions(ServiceDecisionMessage serviceDecisionMessage){
        final String serviceSoftwareID= serviceDecisionMessage.getRequiredSWID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);
        if(handlingSW!=null) {
            //Todo: in handleMessage der Softwares muss die id des genutzten Angebots ausgewertet werden.
            //handleMessage wird nicht genutzt, da nicht notwendig für Prototypen
            //handlingSW.handleMessage(serviceDecisionMessage);
        }else{
            System.err.println("Die notwendige Software konnte nicht gestartet werden...");
        }
        if(serviceDecisionMessage.isAccepted()) {
            messageHandlerPresenter.printToLog(
                    "Fahrer hat den Service angenommen. Leite die Entscheidung an den Parkautomaten weiter."
            );
            carlaPresenter.printToEnvironment(
                    "Anfrage "+ serviceDecisionMessage.getInquiryID() +" wurde angenommen, bereit einen Parkplatz zuzuweisen.");

            carlaPresenter.currentStage = CarlaPresenter.STAGE.CAR_ACCEPTED_SERVICE;
            carlaPresenter.setUpButtons();
        }else{
            messageHandlerPresenter.printToLog(
                    "Fahrer hat den Service nicht angenommen. Leite die Entscheidung an den Parkautomaten weiter."
            );
            carlaPresenter.printToEnvironment(
                    "Anfrage "+ serviceDecisionMessage.getInquiryID() +" wurde abgelehnt, Fahrzeug kann verabschiedet werden.");
            carlaPresenter.currentStage = CarlaPresenter.STAGE.CAR_DECLINED_SERVICE_OR_SOFTWARE;
            carlaPresenter.setUpButtons();
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
                    getManifest());
            //todo: gewähltes Angebot berechnen
            messageHandlerPresenter.printToLog("Fahrer möchte neue Software installieren. Fordere diese vom Server an...");
            sendToSWS(req);
        } else{
            ServiceRegistrationMessage registrationMessage = null;
            for(ServiceRegistrationMessage msg : registeringServices){
                if(msg.getRequiredSWID().equals(softwareDecisionMessage.getSoftwareID())){
                    registrationMessage= msg;
                }
            }
            registeringServices.remove(registrationMessage);

            messageHandlerPresenter.printToLog("Fahrer möchte keine Software installieren, ich entferne mich!");
            carlaPresenter.currentStage= CarlaPresenter.STAGE.CAR_DECLINED_SERVICE_OR_SOFTWARE;
            carlaPresenter.setUpButtons();
        }
    }


    @Subscribe
    public void waitForSoftwarePackage(SoftwareInstallationPackage installationPackage){
        if(installationPackage.getUpdatedManifest().equals(getManifest())){
            //Keine neue Software
            messageHandlerPresenter.printToLog("Error 502: Bad Request. Ihr Fahrzeug wurde nicht verifiziert. Sicherheitslücke!");

            ServiceRegistrationMessage registrationMessage = null;
            for(ServiceRegistrationMessage msg : registeringServices){
                if(installationPackage.getSoftwareID().equals(msg.getRequiredSWID())
                        && msg.isInstallSW()){
                    registrationMessage =msg;
                }
            }
            registeringServices.remove(registrationMessage);
            bus.post(new SoftwareDecisionMessage(registrationMessage.getInquiryID(),
                    false,
                    installationPackage.getSoftwareID(),
                    installationPackage.getProvider()));
        } else{
            //Neue Software
            messageHandlerPresenter.printToLog("Neue Software installiert: "+installationPackage.getSoftware().getDescription().getTitle());
            Software toBeInstalled = installationPackage.getSoftware();

            //Kommunikationskanäle setzen
            toBeInstalled.setCarlaConnection(carlaConnection);
            toBeInstalled.setMmsConnection(mmsConnection);
            //toBeInstalled.setSwsConnection(swsConnection);

            //Software hinzufügen
            mgr.installSoftware(toBeInstalled);
            MANIFEST =installationPackage.getUpdatedManifest();
            carlaPresenter.setSwInstalled();

            System.err.println(getManifest());
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
    }

    public String getManifest() {
        if(hacked)
            return MANIFEST_HACKED;
        else
            return MANIFEST;
    }

    public void post(IMessage msg){
        bus.post(msg);
    }

    public void hack() {
        if(hacked){
            hacked = false;
        } else{
            hacked=true;
        }
    }
    public void verifyProvider() {
        if(providerVerified){
            providerVerified = false;
        } else{
            providerVerified=true;
        }
    }
    public void showSWSuggestions() {
        if(suggestionReady){
            suggestionReady = false;
        } else{
            suggestionReady=true;
        }
    }
}
