package Initialization.EventBus;

import Initialization.IConnectionClient;
import Initialization.OEMVerificationServerConnection.INettyClient;
import Initialization.OEMVerificationServerConnection.NettyConnectionClient;
import Initialization.OEMVerificationServerConnection.NettyClientInitializer;
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
