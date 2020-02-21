package Messages;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

public interface IMessage extends Serializable {

    //Get ChannelContext from Message
    public Object getCtx();

    //Add ChannelContext to Message
    public void setCtx(ChannelHandlerContext ctx);
}
