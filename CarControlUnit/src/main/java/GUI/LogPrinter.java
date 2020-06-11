package GUI;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LogPrinter {
    /**
     * @author Linus Hestermeyer
     *
     * Static Utilsmethod for displaying Strings indise the passed Label. Introduced because of to much same code.
     * @param scrollPane
     * @param output
     */
    public static void displayInView(ScrollPane scrollPane, String output){

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        Date date = new Date(System.currentTimeMillis());
                        DateFormat formatter = new SimpleDateFormat("ss.SSS");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        VBox root = new VBox();

                        Node old = scrollPane.getContent();
                        Node newNode = new Text("\n"+formatter.format(date)+"  "+output);

                        if(old != null){
                            root.getChildren().addAll(old, newNode);
                        } else{
                            root.getChildren().add(newNode);
                        }


                        scrollPane.setContent(root);
                    }
                };
                Platform.runLater(updater);

            }

        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();

    }

}
