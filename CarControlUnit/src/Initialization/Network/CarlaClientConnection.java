package Initialization.Network;

import EnvironmentObjects.IConnectionClient;
import Messages.CarlaMessage;
import Messages.IMessage;
import Messages.ServiceDecisionMessage;
import Messages.ServiceRegistrationMessage;
import com.google.common.eventbus.EventBus;
import org.json.JSONObject;

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
        Thread t = new Thread(() -> {
            while(true) {
                System.err.println("Trying to read");
                try {
                    Object o = in.readObject();
                    System.out.println("Read object carla: " + o);
                    bus.post(o);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
 
        }
    }

    @Override
    public void initBootstrap(String host, int port) {
        //currently not needed
        this.HOST=host;
        this.PORT=port;

        try {
            serverSocket= new ServerSocket(PORT);
            System.err.println("Opened up port "+ PORT + " for CarlaConnection: ");
        } catch (Exception e){
            e.printStackTrace();
        }

        new Thread(){
            @Override
            public void run() {
                while(!running) {
                    try {
                        socket = serverSocket.accept();
                        System.err.println("Carla connection successful!");
                        running = true;
                        out = new ObjectOutputStream(socket.getOutputStream()); // get the output stream of client.
                        System.err.println("ObjectOutputStream Carla created");
                        in = new ObjectInputStream(socket.getInputStream());    // get the input stream of client.
                        System.err.println("ObjectInputStream Carla created");
                        startConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


    }

    @Override
    public void sendMessage(IMessage out) {

        if(this.out != null) {
            JSONObject obj  = new JSONObject();
            if(out instanceof CarlaMessage){
                CarlaMessage carlaMessage= (CarlaMessage) out;
                obj.put("action", carlaMessage.getAction());
            }
            StringWriter writer = new StringWriter();
            obj.write(writer);

            try {
                System.err.println("Trying to send an JSON Object.");
                this.out.writeObject(writer.toString());
                this.out.flush();
                System.err.println("Sent to Carla: "+writer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.err.println("Carla not connected - what a bummer.");
        }
    }
}
