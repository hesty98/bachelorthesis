package Initialization.Network;

import EnvironmentObjects.IConnectionClient;
import Messages.IMessage;

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

    public boolean isRunning() {
        return running;
    }


    @Override
    public void startConnection() {
        while (true) {
            try {
                Object o = in.readObject();
                System.out.println("Read object: "+o);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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
            ServerSocket ss = new ServerSocket(PORT);
            this.socket=ss.accept();
            out = new ObjectOutputStream(socket.getOutputStream()); // get the output stream of client.
            in = new ObjectInputStream(socket.getInputStream());    // get the input stream of client.
            this.startConnection();
            System.err.println("Carla connection successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(IMessage out) {
        try {
            this.out.writeObject(out);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
