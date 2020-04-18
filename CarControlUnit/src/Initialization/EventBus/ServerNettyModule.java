package Initialization.EventBus;

import Initialization.Network.OEMVerificationServerConnection.INettyClient;
import Initialization.Network.OEMVerificationServerConnection.NettyConnectionClient;
import Initialization.Network.OEMVerificationServerConnection.NettyClientInitializer;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ServerNettyModule extends AbstractModule
{
    @Override
    protected void configure() {
        bind(NettyClientInitializer.class).in(Singleton.class);
        bind(INettyClient.class).to(NettyConnectionClient.class);
    }
}
