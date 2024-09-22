package avalon.server;

import java.nio.channels.SocketChannel;

public class Player {
    String name;
    SocketChannel channel;
    public Player(String name, SocketChannel channel) {
        this.name = name;
        this.channel = channel;
    }
}
