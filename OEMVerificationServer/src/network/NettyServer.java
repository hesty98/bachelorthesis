package network;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import config.NetworkConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Start der Netty-Kommunikation
 *
 * @author Mihail Litvinav
 */
public class NettyServer implements INettyServer{
    private EventBus eventBus;
    private int port;

    @Inject
    public NettyServer(EventBus eventBus) {
        this.eventBus = eventBus;
        this.start(NetworkConfig.serverPort);
    }

    /**
     * Startet den Server.
     */
    public void start(int port){
        System.out.println("NettyServer is starting...");

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(9048576 ,ClassResolvers.cacheDisabled(null)));
                            ch.pipeline().addLast(new NettyServerHandler(eventBus));
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * Setzt den Port
     * @param port Port
     */
    public  void setPort(int port){
        this.port = port;
    }
}
