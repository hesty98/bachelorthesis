package View.CCUMessageHandler;

import Initialization.Netty.NettyClient;
import Messages.*;
import View.LogPrinter;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
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

    @FXML
    public BorderPane mainPane;

    @Inject
    private EventBus eventBus;

    @Inject
    private NettyClient nettyClient;

    @FXML
    public Label logReceivedMessages;

    @FXML
    public Label logForwardedMessages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register(this);
    }

    private ArrayList<ServiceRegistrationMessage> registeredServices = new ArrayList<>();

    @Subscribe
    public void registerService(ServiceRegistrationMessage msg){
        registeredServices.add(msg);

        LogPrinter.displayInView(logReceivedMessages,
                "Received new ServiceRegistrationService. InquiryID: "+ msg.getInquiryID()
                        +"\n     Type: "+msg.getDescription().getServiceTypeID()
                        +"\n     Titel: "+msg.getDescription().getServiceTitle()
                        +"\n     Description:"+msg.getDescription().getServceDescription()
                        +"\n... verifying ...");
        ServiceVerificationCommand cmd = new ServiceVerificationCommand("eins cooles auto manifesto", msg.getDescription(), msg.getInquiryID());
        nettyClient.sendMessage(cmd);
        LogPrinter.displayInView(logForwardedMessages,
                "Sent ServiceVerificationCommand to OEM Verification Server."
        );
    }

    @Subscribe
    public void waitForVerification(ServiceVerificationMessage serviceVerificationMessage){
        LogPrinter.displayInView(logReceivedMessages,
                        logReceivedMessages.getText()
                                + "\nReceived ServiceVerificationMessage. " +
                                "\n     Providor: "+ serviceVerificationMessage.getDesc().getServiceProvider()+
                                "\n     Description: "+serviceVerificationMessage.getDesc().getServceDescription()+
                                "\n\nIn future, we need to Forward to MMS. Currently, Service always gets accepted and pushed to the intern eventbus right away."
                );

        for(ServiceRegistrationMessage registrationMessage : registeredServices){
            if(registrationMessage.getInquiryID()== serviceVerificationMessage.getInquiryID()){
                //future: send(registrationMessage); -> to MMS, log this in forwardedMessagesLog
                ServiceDecisionMessage decisionMessage = new ServiceDecisionMessage(serviceVerificationMessage.getInquiryID(), true);
                eventBus.post(decisionMessage);
            }
        }
    }

    @Subscribe
    public void handleActionCommand(ServiceActionCommand cmd){
        //TODO future: send to MMS
        LogPrinter.displayInView(logReceivedMessages, logReceivedMessages.getText()+"\n"
                        + "Received ServiceActionCommand. Creating verified Message and forwarding to carla."
                );


        ServiceActionMessage msg = new ServiceActionMessage(cmd.getAction(), cmd.getCommunicatingService());
        eventBus.post(msg);
    }

    @Subscribe
    public void waitForDecisions(ServiceDecisionMessage serviceDecisionMessage){
        if(serviceDecisionMessage.isAccepted()){
            LogPrinter.displayInView(logReceivedMessages,
                logReceivedMessages.getText()+
                        "\nDriver accepted to use the Service! Forwarding message to Carla-Environment."
            );
        }
    }

}