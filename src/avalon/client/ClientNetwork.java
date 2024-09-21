package avalon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientNetwork {
    private final int PORT = 5000;
    private final String name;
    BufferedReader reader;
    PrintWriter writer;
    private SocketChannel socketChannel;
    private ExecutorService executorService;
    private boolean isLeader;
    private boolean isOnQuestTeam;
    private Character character;
    private String loyalty;
    private ArrayList<String> gameInformation;

    public ClientNetwork(String name) {
        executorService = Executors.newFixedThreadPool(2);
        this.name = name;
    }

    public void connect() {
        try {
            System.out.println(name + ": Establishing connection...");
            String IP_ADDR = "localhost";
            socketChannel = SocketChannel.open(new InetSocketAddress(IP_ADDR, PORT));
            serviceServer();
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

    private void serviceServer() {
        reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
        writer = new PrintWriter(Channels.newOutputStream(socketChannel));
        // write name to server
        writeToServer(name);
        // read response from server
        readFromServer();
        // set character
        setCharacter();
        // check if you are Leader
        setLeader();
        executorService.shutdown();
    }

    private void setLeader() {
        String message = readFromServer();
        String[] commands = message.split(" ");
        if(commands[1].equals("true")) {
            System.out.println("You are the leader");
            isLeader = Boolean.parseBoolean(commands[1]);
        } else{
            System.out.println("Leader: " + commands[1]);
        }
    }

    private void setCharacter() {
        String message = readFromServer();
        String[] commands = message.split(" ");
        if (commands[0].equals("/character")) {
            loyalty = commands[1];

            if (loyalty.equals("good")) {
                character = Enum.valueOf(GoodCharacter.class, commands[2]);
            } else if (loyalty.equals("evil")) {
                character = Enum.valueOf(EvilCharacter.class, commands[2]);
            }
        }
        System.out.println(name + " is " + character + ". Loyalty: " + loyalty + ".");
    }

    private void writeToServer(String message) {
        writer.println(message);
        writer.flush();
    }

    private String readFromServer() {
        String message = "";
        try {
            if(
                (message = reader.readLine()) != null
            ){
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Couldn't read from server");
        }
        return message;
    }
}
