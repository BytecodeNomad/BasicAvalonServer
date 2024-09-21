package avalon.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Player {
    private final SocketChannel playerChannel;
    Character character;
    String username;
    BufferedReader reader;
    PrintWriter writer;

    public Player(String username, SocketChannel playerChannel) {
        this.playerChannel = playerChannel;
        this.username = username;
        reader = new BufferedReader(Channels.newReader(playerChannel, StandardCharsets.UTF_8));
        writer = new PrintWriter(Channels.newOutputStream(playerChannel));
    }
    public void sendMessage (String message) {

        writer.println(message);
        writer.flush();
    }
    public void setCharacter(Character character) {
        this.character = character;
        String loyalty;
        String className = character.getClass().getSimpleName();
        if(className.equals("GoodCharacter")) loyalty = "good";
        else if(className.equals("EvilCharacter")) loyalty = "evil";
        else throw new RuntimeException("Character type does not exist");
        String message = "/character " + loyalty + " " + character;
        sendMessage(message);
    }
}
