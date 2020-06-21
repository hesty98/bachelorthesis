package Network.OEMVerificationServerConnection;


/**
 * Initializes the Netty-Instance and the ServerConnection.
 *
 * @author Linus Hestermeyer
 *
 * Teile entnommen von: https://netty.io/wiki/user-guide-for-5.x.html
 */
public class NettyClientInitializer {

    private final INettyClient iNettyClient;

    /**
     * Constructor, which injects the nettyclient.
     *
     * @param iNettyClient nettyCLient
     */
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
