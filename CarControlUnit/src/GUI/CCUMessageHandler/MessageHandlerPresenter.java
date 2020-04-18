package GUI.CCUMessageHandler;

import Car.SoftwareManager;
import EnvironmentObjects.Software;
import Initialization.Network.OEMVerificationServerConnection.NettyConnectionClient;
import Messages.*;
import GUI.LogPrinter;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Diese Klasse repräsentiert den MainPresenter der die Mainview steuert,
 * und somit die einzelnen Views lädt.
 *
 * @version 1.0
 */
public class MessageHandlerPresenter implements Initializable {

    private ArrayList<String> serviceSoftwares = new ArrayList<>();

    //TODO @Inject
    private SoftwareManager mgr;

    @FXML
    public BorderPane mainPane;

    @Inject
    private EventBus eventBus;

    @Inject
    private NettyConnectionClient nettyClient;

    @FXML
    public Label logReceivedMessages;

    @FXML
    public Label logForwardedMessages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register(this);
        this.mgr = new SoftwareManager();

    }

    private ArrayList<ServiceRegistrationMessage> registeringServices = new ArrayList<>();

    /**
     * Reads ServiceRegistrationMessages and passes them to the software being able to handle this Message.
     *
     * @param msg
     */
    @Subscribe
    public void registerService(ServiceRegistrationMessage msg){

        LogPrinter.displayInView(logReceivedMessages,
                "Received new ServiceRegistrationService. InquiryID: "+ msg.getInquiryID()
                        +"\n     Type: "+msg.getDescription().getRequiredSWID()
                        +"\n     Titel: "+msg.getDescription().getServiceTitle()
                        +"\n     Description:"+msg.getDescription().getServiceDescription());

        final String serviceSoftwareID= msg.getDescription().getRequiredSWID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);

        if(handlingSW != null){
            /*handlingSW.handleMessage(msg);
              TODO: delete this and get SDMessage from MMS
             */
            ServiceDecisionMessage sdMSG = new ServiceDecisionMessage(msg.getInquiryID(), true,msg.getDescription().getRequiredSWID(), msg.getServiceID());
            eventBus.post(sdMSG);
        } else{
            registeringServices.add(msg);
            ServiceVerificationCommand cmd = new ServiceVerificationCommand("eins cooles auto manifesto", msg.getDescription(), msg.getInquiryID(), msg.getServiceID());
            nettyClient.sendMessage(cmd);
            LogPrinter.displayInView(logForwardedMessages,
                    "Sent ServiceVerificationCommand to OEM Verification Server."
            );
        }
    }

    @Subscribe
    public void waitForVerification(ServiceVerificationMessage serviceVerificationMessage){
        LogPrinter.displayInView(logReceivedMessages,
                logReceivedMessages.getText()
                        + "\nReceived ServiceVerificationMessage. " +
                        "\n     Providor: "+ serviceVerificationMessage.getDesc().getServiceProvider()+
                        "\n     Description: "+serviceVerificationMessage.getDesc().getServiceDescription()+
                        "\n\nIn future, we need to Forward to MMS. Currently, Service always gets accepted and pushed to the intern eventbus right away."
        );

        if(serviceVerificationMessage.isVerified()){
            for(ServiceRegistrationMessage msg : registeringServices){
                if(serviceVerificationMessage.getServiceID() == msg.getServiceID()){
                    //mgr.submitSoftware(msg);
                    //TODO: get SoftwareDecisionMessage from MMS
                    SoftwareDecisionMessage sdMSG = new SoftwareDecisionMessage(msg.getInquiryID(),true ,msg.getDescription().getRequiredSWID());
                }
            }
        } else{
            System.err.println("Unverified SW got recommended!");
        }
    }

    @Subscribe
    public void handleActionCommand(ServiceActionCommand cmd){
        LogPrinter.displayInView(logReceivedMessages, logReceivedMessages.getText()+"\n"
                + "Received ServiceActionCommand. Creating verified Message and forwarding to carla."
        );
        final String serviceSWID =cmd.getServiceID();
        Software handlingSW = mgr.getSoftware(serviceSWID);
        handlingSW.handleMessage(cmd);
        ServiceActionMessage sam = new ServiceActionMessage(cmd.getAction(), cmd.getCommunicatingService(), cmd.getServiceID());
        eventBus.post(sam);
    }

    @Subscribe
    public void waitForDecisions(ServiceDecisionMessage serviceDecisionMessage){
        final String serviceSoftwareID= serviceDecisionMessage.getSoftwareID();
        Software handlingSW = mgr.getSoftware(serviceSoftwareID);
        if(handlingSW!=null) {
            handlingSW.handleMessage(serviceDecisionMessage);
        }else{
            System.err.println("No Software handling the message. -> MessageHandlerPresenter");
        }
        if(serviceDecisionMessage.isAccepted()) {
            LogPrinter.displayInView(logReceivedMessages,
                    logReceivedMessages.getText()+
                            "\nDriver accepted to use the Service! Forwarding message to Carla-Environment."
            );
        }else{
            LogPrinter.displayInView(logReceivedMessages,
                    logReceivedMessages.getText()+
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
            //forward InstallRequest to the OEMServer
            SoftwareInstallRequest req = new SoftwareInstallRequest(softwareDecisionMessage.getSoftwareID());
            eventBus.post(req);
        } else{
            System.err.println("Driver doesnt want to install Software!");
        }
    }

    @Subscribe
    public void waitForSoftwarePackage(SoftwareInstallationPackage installationPackage){
        Software toBeInstalled = installationPackage.getSoftware();
        mgr.installSoftware(toBeInstalled);
        for(ServiceRegistrationMessage msg : registeringServices){
            if(installationPackage.getSoftwareID() == msg.getServiceID()){
                eventBus.post(msg);
            }
        }
    }

}