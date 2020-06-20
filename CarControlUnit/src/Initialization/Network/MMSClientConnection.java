package Initialization.Network;

import EnvironmentObjects.IConnectionClient;
import Messages.IMessage;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MMSClientConnection implements IConnectionClient {
    private static String HOST;
    private static int PORT;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running =false;
    private EventBus bus;
    private ServerSocket serverSocket;

    public MMSClientConnection(EventBus bus) {
        this.bus=bus;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void startConnection() {
        Thread t = new Thread(() -> {
            while(isRunning()) {
                //System.err.println("Trying to read");
                try {
                    Object o = in.readObject();
                    System.out.println("Read Message from MMS: " + o);
                    bus.post(o);
                } catch (IOException | ClassNotFoundException e) {
                    running=false;
                    e.printStackTrace();
                }
            }
            //stopConnection();
            //startConnection();
        });
        t.start();
    }

    @Override
    public void stopConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initBootstrap(String host, int port) {
        this.HOST=host;
        this.PORT=port;

        try {
            serverSocket= new ServerSocket(PORT);
            System.err.println("Opened up port "+ PORT + " for MMSConnection: ");
        } catch (Exception e){
            e.printStackTrace();
        }

        new Thread(){
            @Override
            public void run() {
                while(!running) {
                    try {
                        socket = serverSocket.accept();
                        System.err.println("MMS connection successful!");
                        running = true;
                        out = new ObjectOutputStream(socket.getOutputStream()); // get the output stream of client.
                        System.err.println("ObjectOutputStream MMS created");
                        in = new ObjectInputStream(socket.getInputStream());    // get the input stream of client.
                        System.err.println("ObjectInputStream MMS created");
                        startConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                        running=false;
                    }
                }
            }
        }.start();

    }

    @Override
    public void sendMessage(IMessage out) {
        if(this.out != null) {
            try {
                this.out.writeObject(out);
                this.out.flush();
                System.err.println("Sent to MMS: "+out);;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.err.println("MMS not connected - what a bummer.");
        }
    }
}