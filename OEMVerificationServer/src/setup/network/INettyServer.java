package setup.network;

/**
 * Ein NettyServer Interface, um später die NettyServer Klasse zu binden über Guava.
 *
 * @author Mihail Litvinav
 */
public interface INettyServer {
public void start(int port);
}
