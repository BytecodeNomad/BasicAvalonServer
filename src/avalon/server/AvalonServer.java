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
    private final int PORT = 5000;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService service;
    private GameManager gameManager;
    final int maxConnections = 5;
    private int connected = 0;
    public AvalonServer() {
        service = Executors.newCachedThreadPool();
        gameManager = new GameManager();
    }

    public void connect() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
//            serverSocketChannel.configureBlocking(false);
            while (serverSocketChannel.isOpen()) {
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    if(maxConnections > connected) {
                        connected++;
                        service.execute(() -> handleConnection(channel));
                    } else if(maxConnections == connected){
                        serverSocketChannel.close();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not establish channel");
        }
        service.shutdownNow();
    }

    private void handleConnection(SocketChannel channel) {
        try (BufferedReader reader = new BufferedReader(Channels.newReader(channel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newOutputStream(channel));
        ) {

            while (channel.isOpen()) {
                String incoming = receiveMessage(reader);
                if (incoming.contains("/name")) {
                    System.out.println(incoming);

                    String name = incoming.split(" ")[1];
                    createPlayer(name, channel);
                    String welcome = "/welcome " + name;

                    sendMessage(welcome, writer);

                    sendMessage("/start", writer);

                    break;
                }

            }
            channel.close();
        } catch (IOException e) {
            System.out.println("Reader or writer could not be created. Check your connection");
            try {
                channel.close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

    private void createPlayer(String name, SocketChannel channel) {
        Player player = new Player(name, channel);
        gameManager.addPlayer(player);

    }

    private void sendMessage(String message, PrintWriter writer) {
        writer.println(message);
        writer.flush();
        System.out.println(message);
    }

    private String receiveMessage(BufferedReader reader) {
        String message = "";
        try {
            message = reader.readLine();
        } catch (Exception e) {
            System.out.println("Something must have happened while reading");
        }
        return message;
    }
}
