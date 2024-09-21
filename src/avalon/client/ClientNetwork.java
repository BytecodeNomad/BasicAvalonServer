package avalon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientNetwork {
    private final int PORT = 5000;
    private final String name;
    private SocketChannel socketChannel;

    private ExecutorService executorService;

    public ClientNetwork(String name) {
        executorService = Executors.newFixedThreadPool(2);
        this.name = name;
    }
    public void connect()  {
        try {
            System.out.println(name + ": Establishing connection...");
            String IP_ADDR = "localhost";
            socketChannel = SocketChannel.open(new InetSocketAddress(IP_ADDR, PORT));
            serviceServer(socketChannel);
        } catch (IOException e) {
            System.out.println("The client couldn't connect to the host!");
        }
    }

    private void shutdownClient() {
        try {
            socketChannel.close();
            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serviceServer(SocketChannel channel) {
            executorService.execute(() -> {
                writeToServer(name, channel);
                readFromServer(channel);
            });
            executorService.shutdown();
    }
    private void writeToServer(String message, SocketChannel channel) {
        PrintWriter writer = new PrintWriter(Channels.newOutputStream(channel));
        writer.println(message);
        writer.flush();
    }
    private void readFromServer(SocketChannel channel) {
        BufferedReader reader = new BufferedReader(Channels.newReader(channel, StandardCharsets.UTF_8));
        String message = "";
        try {
            message = reader.readLine();
            System.out.println(message);
        } catch (IOException e) {
            System.out.println("Couldn't read from server");
        }
    }
}
