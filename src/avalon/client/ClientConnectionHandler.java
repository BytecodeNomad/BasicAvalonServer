package avalon.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static avalon.client.EvilCharacter.ASSASSIN;
import static avalon.client.GoodCharacter.MERLIN;
import static avalon.client.QuestCard.SUCCESS;

public class ClientConnectionHandler {
    final int PORT = 5000;
    final String IP_ADDRESS = "localhost";
    private final String name;
    SocketChannel channel;
    BufferedReader reader;
    PrintWriter writer;
    Player player = new Player();


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
        String incoming;
        boolean firstTime = true;
        while (channel.isOpen()) {
            if(firstTime) {
                sendName(name);
                firstTime = false;
            }
            incoming = receiveMessage();
            if (incoming == null) break;
            if (incoming.contains("/welcome")) {
                System.out.println(incoming);
            }
            if (incoming.contains("/start")) {
                System.out.println(incoming);
            }
            if (incoming.contains("/character")) {
                System.out.println(incoming);
                if (incoming.contains("/arthur")) {
                    Character character = Enum.valueOf(GoodCharacter.class, incoming.split(" ")[2]);
                    player.setCharacter(character);
                    if (character == MERLIN) {
                        player.setMerlin(true);
                    }
                } else if (incoming.contains("/mordred")) {
                    Character character = Enum.valueOf(EvilCharacter.class, incoming.split(" ")[2]);
                    player.setCharacter(character);
                    if (character == ASSASSIN) {
                        player.setAssassin(true);
                    }
                }
            }
            if (incoming.contains("/players")) {
                String playersPortion = incoming.replace("/players ", "");
                player.playersInLobby.addAll(Arrays.asList(playersPortion.split(" ")));
                System.out.println("Players in room: " + player.playersInLobby);
            }
            if (incoming.contains("/evil-players")) {
                if(!incoming.equals("/evil-players")) {
                    String playersPortion = incoming.replace("/evil-players ", "");
                    player.evilPlayers.addAll(Arrays.asList(playersPortion.split(" ")));
                    System.out.println("Evil Players in room: " + player.evilPlayers);
                }
            }
            if(incoming.contains("/round")) {
                player.round = Integer.parseInt(incoming.split(" ")[1]);
                System.out.println(incoming);
            }
            if(incoming.contains("/leader")) {
                if(incoming.contains("true")) {
                    player.setLeader(true);
                    String message = player.selectTeamMatesForQuest();
                    sendMessage(message);

                } else{
                    System.out.println(incoming);
                }
            }
            if(incoming.contains("/team-proposal")) {
                String voteMessage = "/vote " + player.voteForQuestTeam();
                sendMessage(voteMessage);
            }
            if(incoming.contains("/vote-results")) {
                System.out.println(incoming.replace("/vote-results ", ""));
            }
            if(incoming.contains("/quest-vote")){
                System.out.println(incoming);
                String message = "/quest-result ";
                message += (player.character.voteOnQuests() == SUCCESS) ? "success" : "failure";
                sendMessage(message);
            }
            if(incoming.contains("five-rejects")) {
                System.out.println("Evil won!");
                break;
            }
            if(incoming.contains("assassinate")) {
                System.out.println(incoming);
                if(player.isAssassin()){
                    String message = "/guess ";
                    player.playersInLobby.remove(name);
                    String guess = player.playersInLobby
                        .get(new Random().nextInt(player.playersInLobby.size()));
                    message += guess;
                    sendMessage(message);
                }
            }
            if(incoming.contains("game-over")) {
                System.out.println(incoming.replace("/game-over ", ""));
                break;
            }


        }

    }

    private void sendName(String name) {
        String nameMessage = "/name " + name;
        sendMessage(nameMessage);
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
        System.out.println(message);
    }

    public String receiveMessage() {
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            System.out.println(this.name + "Could not read from socket. " + e.getMessage());
        }
        return message;
    }
}
