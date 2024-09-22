package avalon.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvalonServer {
    ServerSocketChannel serverSocketChannel;
    int PORT;
    final int maximumConnections = 5;
    int connected;
    GameManager gameManager = new GameManager();
    public AvalonServer(int port) {
        PORT = port;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void connect() {
        while(serverSocketChannel.isOpen()) {
            try {
                SocketChannel channel = serverSocketChannel.accept();
                createPlayer(channel);
                connected++;
                if(connected == maximumConnections) break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void createPlayer(SocketChannel channel){
        gameManager.addPlayer(new Player(channel));
    }

    public void startGame() {
        gameManager.run();
    }
}
