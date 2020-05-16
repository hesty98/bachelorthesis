package Initialization.Network;

import EnvironmentObjects.IConnectionClient;
import Messages.IMessage;
import Messages.ServiceDecisionMessage;
import com.google.common.eventbus.EventBus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CarlaClientConnection implements IConnectionClient {
    private static String HOST;
    private static int PORT;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running =false;
    private EventBus bus;
    private ServerSocket serverSocket;

    public CarlaClientConnection(EventBus bus) {
        this.bus=bus;
    }

    public boolean isRunning() {
        return running;
    }


    @Override
    public void startConnection() {
        Thread t = new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Object o = in.readObject();
                        System.out.println("Read object: " + o);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        t.run();
    }

    @Override
    public void stopConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        }catch (Exception e){
 
        }
    }

    @Override
    public void initBootstrap(String host, int port) {
        //currently not needed
        this.HOST=host;
        this.PORT=port;
        try {
            serverSocket= new ServerSocket(PORT);
        } catch (Exception e){
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    socket=serverSocket.accept();
                    out = new ObjectOutputStream(socket.getOutputStream()); // get the output stream of client.
                    in = new ObjectInputStream(socket.getInputStream());    // get the input stream of client.
                    running=true;
                    startConnection();
                    System.err.println("Carla connection successful!");
                } catch (IOException e) {
                    e.printStackTrace();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.err.println("Carla isnt running - use the Buttons!");
        }
    }
}
