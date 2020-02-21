package Initialization.Netty;

import com.google.inject.Inject;

/**
 * Initializes the Netty-Instance and the ServerConnection.
 *
 * @author Linus Hestermeyer
 */
public class NettyClientInitializer {

    private final INettyClient iNettyClient;

    /**
     * Constructor, which injects the nettyclient.
     *
     * @param iNettyClient nettyCLient
     */
    @Inject
    public NettyClientInitializer(INettyClient iNettyClient)
    {
        this.iNettyClient = iNettyClient;
    }

    /**
     *
     * Calles the needed methods from the nettyClient.
     *
     * @param host serverhost
     * @param port serverport
     *
     * @return nettyClient after successfully building the connection
     */
    public INettyClient createNettyClientConnection(String host, int port)
    {
        this.iNettyClient.initBootstrap(host, port);
        this.iNettyClient.startConnection();

        return iNettyClient;
    }

}
