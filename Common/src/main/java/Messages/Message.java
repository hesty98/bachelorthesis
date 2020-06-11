package Messages;

import io.netty.channel.ChannelHandlerContext;

abstract public class Message implements IMessage {
    
    ChannelHandlerContext ctx = null;

    @Override
    public ChannelHandlerContext getCtx() {
        return this.ctx;
    }

    @Override
    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
