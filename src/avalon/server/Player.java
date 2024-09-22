package avalon.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Player {
    String name;
    Character character;
    SocketChannel channel;
    BufferedReader reader;
    PrintWriter writer;
    public Player(SocketChannel channel) {
        this.channel = channel;
        reader = new BufferedReader(Channels.newReader(channel, StandardCharsets.UTF_8));
        writer = new PrintWriter(Channels.newWriter(channel, StandardCharsets.UTF_8), true);
    }

    public void setCharacter(Character character) {
        this.character = character;
        String message = "/character ";
        if(character.getClass().getSimpleName().equals("GoodCharacter")) {
            message += "/arthur ";
        } else if(character.getClass().getSimpleName().equals("EvilCharacter")) {
            message += "/mordred ";
        }
        message += character;

        sendMessage(message);
        System.out.println(message);
    }
    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }
    public String receiveMessage() {
        try{
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
