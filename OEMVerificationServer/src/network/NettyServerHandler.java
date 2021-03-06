package network;

import Messages.IMessage;
import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Netty Handler der read and write Operationen durchführt.
 *
 * @author Linus Hestermeyer
 *
 * teile entnommen von: https://netty.io/wiki/user-guide-for-5.x.html
 */
public class NettyServerHandler extends ChannelHandlerAdapter {

    private EventBus eb;

    public NettyServerHandler(EventBus eventBus) {
        this.eb = eventBus;
    }

    /**
     * Liest den Input aus und postet Messages auf den EventBus
     *
     * @param ctx Channel Context
     * @param msg Was ankommt bei Netty
     *
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //Alles was kein IMessage ist muss geblockt werden!
        System.err.println("Received: "+msg);
        if(msg instanceof IMessage){
            ((IMessage) msg).setCtx(ctx);
            this.eb.post(msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * Wenn eine Exception in der Verbindung auftritt, wird diese Verbindung geschlossen und
     * postet ein LogoutCommand sodass der User des Clienten aus der chat.Lobby entfernt werden kann.
     *
     * @param ctx Channel Context
     * @param cause Exception
     *
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();

        ctx.close();
    }
}