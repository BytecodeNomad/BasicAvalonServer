package avalon.server;

import java.util.*;

import static avalon.server.EvilCharacter.*;
import static avalon.server.GoodCharacter.*;

public class GameManager {
    private final int maxPlayers = 5;
    private final ArrayList<EvilCharacter> selectableEvilCharacters = new ArrayList(
        Arrays.asList(MORDRED, MORGANA, ASSASSIN, OBERON, MINION_OF_MORDRED, MINION_OF_MORDRED, MINION_OF_MORDRED)
    );
    private final ArrayList<GoodCharacter> selectableGoodCharacters = new ArrayList(
        Arrays.asList(PERCIVAL, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR)
    );
    Map<String, Player> namePlayerMap = new HashMap<>();
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> questTeam = new ArrayList<>();
    ArrayList<String> votes = new ArrayList<>();
    int round = 1;
    int goodWin;
    int evilWin;
    Player leader;
    int voteOnTeamCount = 0;
    boolean isTeamApproved;
    Player assassin;
    private Player merlin;

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void run() {
        setUp();
        setPlayerCharacters();
        broadCastAllPlayerNames();
        broadCastEvilPlayerNames();
        broadCastRound();
        while (!isTeamApproved) {
            selectLeader();
            getTeamForQuest();
            isTeamApproved = collateVotes("approve", this.players);
            voteOnTeamCount++;
            broadCastResults();
            if (isTeamApproved) {
                //vote on quest
                for (Player player : questTeam) {
                    player.sendMessage("/quest-vote");
//                    String command = player.receiveMessage();
//                    if(command.contains("success")) {
//                        votes.add("success");
//                    }
                }
                // collate votes
                boolean success = collateVotes("success", this.questTeam);
                if (success) goodWin++;
                else evilWin++;
                voteOnTeamCount = 0;
            }
            if (voteOnTeamCount == 5) {
                for (Player player : players) {
                    player.sendMessage("/five-rejects");
                    break;
                }
            }
            if (goodWin == 3) {
                for (Player player : players) {
                    player.sendMessage("/assassinate");
                }
                String guess = assassin.receiveMessage();
                System.out.println("Guess Merlin: " + assassin.name + "-" + guess);
                String name = guess.replace("/guess ", "");
                if (namePlayerMap.get(name).equals(merlin)) {
                    for (Player player : players) {
                        player.sendMessage("/game-over " + assassin.name + " merlin?");
                    }
                } else {
                    for (Player player : players) {
                        player.sendMessage("/game-over Arthur's knights win!");
                    }
                }
                break;
            }
            isTeamApproved = false;
        }
    }

    private void broadCastResults() {
        String message = "/vote-results";
        if (isTeamApproved) {
            message += " The team has been approved";
        } else {
            message += " The team has not been approved";
        }
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    private boolean collateVotes(String voteType, ArrayList<Player> players) {

        for (Player player : players) {
            String voteCommand = player.receiveMessage();
            if (voteCommand.contains(voteType)) {
                votes.add(voteType);
            }
        }
        if (votes.size() > (players.size() - votes.size())) {
            votes.clear();
            return true;
        }
        votes.clear();
        return false;
    }

    public void setUp() {
        for (Player player : players) {
            String command = player.receiveMessage();
            System.out.println(command);
            if (command.contains("/name")) {
                player.name = command.split(" ")[1];
                namePlayerMap.put(player.name, player);
                String welcome = "/welcome " + player.name;
                player.sendMessage(welcome);
            }
            player.sendMessage("/start");
        }
    }

    private void getTeamForQuest() {
        // Receive the message from the leader (team selection command)
        String command = leader.receiveMessage();
        System.out.println(command);

        // Make sure the message starts with "/team"
        if (command.startsWith("/team")) {
            String teamMates = command.replace("/team ", "");
            String[] questTeamArray = teamMates.split(" ");
            for (String teamMate : questTeamArray) {
                questTeam.add(namePlayerMap.get(teamMate));
            }
            String message = "/team-proposal " + teamMates;

            // Broadcast the team proposal to all players
            for (Player player : players) {
                player.sendMessage(message);
            }

            System.out.println(message);  // Debugging to ensure the correct message is sent
        } else {
            System.out.println("Unexpected command from leader: " + command);
        }
    }

    private void selectLeader() {
        leader = players.remove(0);
        leader.sendMessage("/leader true");
        String message = "/leader " + leader.name;
        for (Player player : players) {
            player.sendMessage(message);
        }
        System.out.println(message);
        players.add(leader);
    }

    private void setPlayerCharacters() {
        Collections.shuffle(players);
        merlin = players.get(0);
        merlin.setCharacter(MERLIN);
        assassin = players.get(1);
        assassin.setCharacter(ASSASSIN);

        int numOfGoodCharacters = CharacterAssignment.getAssignments().get(maxPlayers).getFirst() - 1;
        int numOfBadCharacters = CharacterAssignment.getAssignments().get(maxPlayers).getSecond() - 1;

        int startIdx = 2;
        startIdx = selectCharacters(startIdx, numOfGoodCharacters, selectableGoodCharacters);
        selectCharacters(startIdx, numOfBadCharacters, selectableEvilCharacters);
    }

    private void broadCastAllPlayerNames() {
        String message = "/players";
        for (Player player : players) {
            message += (" " + player.name);
        }
        for (Player player : players) {
            player.sendMessage(message);
        }
        System.out.println(message);
    }

    private void broadCastEvilPlayerNames() {
        String message = "/evil-players";
        for (Player player : players) {
            if (player.character.getClass().getSimpleName().equals("EvilCharacter")) {
                message += (" " + player.name);
            }
        }
        for (Player player : players) {
            if (player.character.getClass().getSimpleName().equals("EvilCharacter")
                || player.character == MERLIN) {
                player.sendMessage(message.replace((" " + player.name), ""));

            }
        }
        System.out.println(message);
    }

    private void broadCastRound() {
        String message = "/round " + round;
        for (Player player : players) {
            player.sendMessage(message);
        }
        Collections.shuffle(players);
    }

    public int selectCharacters(int startIdx, int selectLimit, ArrayList<? extends Character> selectableCharacter) {
        Collections.shuffle(selectableCharacter);
        for (int i = 1; i <= selectLimit; i++, startIdx++) {
            players.get(startIdx).setCharacter((Character) selectableCharacter.get(i));
        }
        return startIdx;
    }
}
