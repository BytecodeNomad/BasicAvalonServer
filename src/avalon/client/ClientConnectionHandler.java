package avalon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ClientConnectionHandler {
    private final String name;
    SocketChannel channel;
    final int PORT = 5000;
    final String IP_ADDRESS = "localhost";
    BufferedReader reader;
    PrintWriter writer;

    public ClientConnectionHandler(String name) {
        this.name = name;
        try {
            channel = SocketChannel.open(new InetSocketAddress(IP_ADDRESS, PORT));
            reader = new BufferedReader(Channels.newReader(channel, StandardCharsets.UTF_8));
            writer = new PrintWriter(Channels.newOutputStream(channel));
        } catch (IOException e) {
            System.out.println("Connection could not be established.");
        }
    }

    public void connect() {
        while(channel.isOpen()) {
            //send your name
            String outgoing = "/name " + name;
            sendMessage(outgoing);

            String incoming = receiveMessage();

            if(incoming.contains("/welcome")) {
                System.out.println(incoming);
            }
            if(incoming.contains("/start")) {
                System.out.println(incoming);
                break;
            }
        }
    }
    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
        System.out.println(message);
    }
    public String receiveMessage() {
        String message = null;
        try{
            message = reader.readLine();
            if(message == null) message = "";
        } catch(IOException e) {
            System.out.println("Could not read from socket. " + e.getMessage());
        }
        return message;
    }
}
