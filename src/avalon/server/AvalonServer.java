package avalon.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AvalonServer {
    final int PORT = 5000;
    ServerSocketChannel serverSocketChannel;
    ExecutorService executorService;
    AtomicInteger maxConnections = new AtomicInteger(5);
    public AvalonServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        executorService = Executors.newCachedThreadPool();

    }
    public void connect() {
        int connected = 0;
        System.out.println("The server is up and running");
        while(connected < maxConnections.get()) {
            SocketChannel channel = null;
            try {
                channel = serverSocketChannel.accept();
            } catch (IOException e) {
                System.out.println("No players yet");
            }
            if(channel != null) {
                System.out.println("A new player has connected");
               SocketChannel player = channel;
               connected++;
               executorService.execute(() -> servicePlayer(player));
            }
        }
        executorService.shutdown();
    }
    public void servicePlayer(SocketChannel player) {
        try (
            BufferedReader reader = new BufferedReader(Channels.newReader(player, StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(Channels.newOutputStream(player));
            ) {
            String name = reader.readLine();
            String message = name + " is acknowledged";
            writer.println(message);
            writer.flush();
            System.out.println(message);
        } catch(Exception e) {
            System.out.println("Player could not connect");
        }

    }
}
