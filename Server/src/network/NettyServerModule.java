package network;

import com.google.inject.AbstractModule;

public class NettyServerModule extends AbstractModule {
    /**
     * Guava Module für den NettyServer damit später eine Instanz mit dem EventBus erzeugt werden kann.
     * [!] Bis jetzt wird der NettyServer nur bei MainDependencies gebraucht und dort erzeugt / nicht injected.
     *
     * @author Mihail Litvinav
     */
    @Override
    protected void configure() {
        bind(INettyServer.class).to(NettyServer.class);
    }
}
