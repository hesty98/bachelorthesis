package Initialization;

import Initialization.Netty.INettyClient;
import Initialization.Netty.NettyClient;
import Initialization.Netty.NettyClientInitializer;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ServerNettyModule extends AbstractModule
{
    @Override
    protected void configure() {
        bind(NettyClientInitializer.class).in(Singleton.class);
        bind(INettyClient.class).to(NettyClient.class);
    }
}
