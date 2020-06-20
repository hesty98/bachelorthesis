package Initialization.Network.OEMVerificationServerConnection;

import Messages.IMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * Handles Netty-Initialization.Network.Events (i.e. messages, connection status etc.).
 *
 * @author Linus Hestermeyer
 */

public class NettyClientHandler extends ChannelHandlerAdapter {
    private NettyConnectionClient nettyClient;

    /**
     * Constructor, which injects the nettyclient.
     *
     * @param nettyClient NettyClient-Instance
     */
    public NettyClientHandler(NettyConnectionClient nettyClient)
    {
        this.nettyClient = nettyClient;
    }


    /**
     * Method which automatically gets called at start of a connection.
     *
     * @param ctx connection-context
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) { }

    /**
     * Method which handles incoming messages.
     *
     * @param ctx connection-context
     * @param msg incoming message
     *
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //System.out.println("Recieved a Message! "+msg);
        this.nettyClient.receivedMessage((IMessage)msg);
    }


    /**
     * Gets called when last Message of channelRead got read.
     *
     * @param ctx connection-context
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    /**
     * Gets called when the connection is inactive or if the connection ran out.
     *
     * @param ctx connection-context
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.nettyClient.connectionInterrupted();
        ctx.fireChannelInactive();
    }


    /**
     * Gets called if unhandled Exception occurs in connection-context.
     * *
     * @param ctx connection-context
     * @param cause exception
     *
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
