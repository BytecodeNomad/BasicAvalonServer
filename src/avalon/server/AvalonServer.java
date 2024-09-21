package avalon.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

//Todo: Implement a latch to countdown when the room is full
public class AvalonServer {
    private final int PORT = 5000;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService executorService;
    private AtomicInteger maxConnections = new AtomicInteger(GameManager.maxPlayerCount);
    BufferedReader reader;
    GameManager gameManager;
    public AvalonServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        executorService = Executors.newCachedThreadPool();

    }
    public void connect() {
        CountDownLatch latch = new CountDownLatch(maxConnections.get());
        gameManager = new GameManager();
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
               SocketChannel player = channel;
               connected++;
               executorService.execute(() -> createPlayer(player, latch));
            }
        }
        startGame(latch);
//        executorService.shutdown();
    }

    private void startGame(CountDownLatch latch) {
        try{
            latch.await();
        } catch(Exception e) {
            e.printStackTrace();
        }
        gameManager.run();
    }

    private synchronized void createPlayer(SocketChannel playerChannel, CountDownLatch latch) {
        reader = new BufferedReader(Channels.newReader(playerChannel, StandardCharsets.UTF_8));
        String username = "Default";
        try {
            username = reader.readLine();
            System.out.println(username + " has connected!");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
        gameManager.addPlayer(new Player(username, playerChannel));
        latch.countDown();
    }
}
