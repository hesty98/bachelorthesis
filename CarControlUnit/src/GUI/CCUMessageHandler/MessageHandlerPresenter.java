package GUI.CCUMessageHandler;

import GUI.LogPrinter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void printToLog(String s) {
        LogPrinter.displayInView(logReceivedMessages, s);
    }

}