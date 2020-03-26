package Initialization.OEMVerificationServerConnection;

import Messages.IMessage;

/**
 * The interface for the NettyClient initialization
 *
 * @author Linus Hestermeyer
 */
public interface INettyClient {
    /**
     * Starts the Connection with the Server. Also inits channelread, send etc for Ports.
     */
    void startConnection();

    /**
     * Cancels the connection with the Server
     */
    void stopConnection();


    /**
     * Creates the netty-Bootstrap which creates the connection.
     *
     * @param host serverhost
     * @param port serverport
     */
    void initBootstrap(String host, int port);


    /**
     * Send a message from the ControlUnit to the Server
     *
     * @param out message
     */
    void sendMessage(IMessage out);
}
