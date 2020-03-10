package View;

import Messages.IMessage;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LogPrinter {
    /**
     * @author Linus Hestermeyer
     *
     * Static Utilsmethod for displaying Strings indise the passed Label. Introduced because of to much same code.
     * @param label
     * @param output
     */
    public static void displayInView(Label label, String output){

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        Date date = new Date(System.currentTimeMillis());
                        DateFormat formatter = new SimpleDateFormat("ss.SSS");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        label.setText(label.getText()+"\n"+formatter.format(date)+"  "+output);
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }

        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();

    }

}
