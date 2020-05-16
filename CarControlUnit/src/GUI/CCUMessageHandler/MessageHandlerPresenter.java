package GUI.CCUMessageHandler;

import Initialization.Network.OEMVerificationServerConnection.NettyConnectionClient;
import GUI.LogPrinter;
import com.google.common.eventbus.EventBus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import java.net.URL;
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

    @FXML
    public ScrollPane logReceivedMessages;

    @FXML
    public ScrollPane logForwardedMessages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void printToReceived(String s) {
        LogPrinter.displayInView(logReceivedMessages, s);
    }

    public void printToSent(String s) {
        LogPrinter.displayInView(logForwardedMessages, s);
    }
}