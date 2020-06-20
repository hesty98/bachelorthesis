package Initialization.Network.OEMVerificationServerConnection;

import Initialization.Network.Events.NoConnectionEvent;
import Messages.IMessage;
import com.google.common.eventbus.EventBus;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Properties;


/**
 * handles the networks connections as well as incoming & outgoing messages.
 *
 * @author Linus Hestermeyer
 */
public class NettyConnectionClient implements INettyClient {
    static String HOST;
    static int PORT;

    private Properties properties;
    private Channel channel;
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private ChannelFuture future;
    private EventBus eventBus;

    private boolean connectionSuccess;

    /**
     * Constructor inits the needed attributes for building a network connection.
     *
     * @param eventBus bus
     */
    public NettyConnectionClient(EventBus eventBus) {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.eventBus= eventBus;
    }


    /**
     * Initializes bootstrap for the server connection
     *
     * @param host serverhost
     * @param port serverport
     */
    public void initBootstrap(String host, int port) {
        try {
            this.HOST = host;
            this.PORT = port;
            this.bootstrap.group(this.eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .remoteAddress(host, port)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ObjectEncoder());
                            p.addLast(new ObjectDecoder(9048576 ,ClassResolvers.cacheDisabled(null)));
                            p.addLast(new NettyClientHandler(NettyConnectionClient.this));
                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Baut den Channel zum Server auf.
     *
     * Bevor diese Methode aufgerufen werden sollte, muss initBootstrap() aufgerufen worden sein.
     *
     */
    public void startConnection() {
        System.err.println("Trying to connect to server...");
        try {
            this.future = bootstrap.connect(this.HOST, this.PORT).sync();
            //this.future.channel().closeFuture().sync();
            System.err.println("[Netty] Connected to localhost:" + PORT + ".");
            this.connectionSuccess = true;
        }
        catch (Exception e) {
            System.err.println("Connection couldn't be built.");
            this.connectionSuccess = false;
        }
    }


    /**
     * Stopt die Verbindung zum Server
     *
     */
    public void stopConnection()
    {
        this.eventLoopGroup.shutdownGracefully();
    }


    /**
     * Wird ausgefuehrt wenn die Verbindung unplanmäßig unterbrochen wird.
     *
     */
    public void connectionInterrupted()
    {
        this.eventBus.post(new NoConnectionEvent());
    }


    /**
     * Eingehende Messages werden von hier auf den Eventbus geschrieben.
     *
     * @param in Die eingehende Nachricht vom Server
     */
    public void receivedMessage(IMessage in)
    {
        this.eventBus.post(in);
    }


    /**
     * Ausgehende Nachrichten werden ueber diese Methode an den Server übermittelt.
     *
     * @param out Eine Nachricht für den Server
     */
    public void sendMessage(IMessage out)
    {
        try {
            //System.err.println("trying to send message " + out);
            this.future.channel().writeAndFlush(out);
            //System.err.println("SUCCESS");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     *
     * Gibt true zurück wenn Connection erfolgreich aufgebaut wurde.
     *
     * @return connectionSuccess
     *
     * */
    public boolean isConnectionSuccess() {
        return connectionSuccess;
    }
}
