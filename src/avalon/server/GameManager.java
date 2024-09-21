package avalon.server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static avalon.server.EvilCharacter.*;
import static avalon.server.GoodCharacter.*;

public class GameManager {
    static Random random = new Random(3);
//    static int maxPlayerCount = random.nextInt(5,11);
static int maxPlayerCount = 5;

    private ArrayList<Character> currentlyPlayableCharacters;
    private ArrayList<Character> importantCharacters =
        new ArrayList<>(
            Arrays.asList(MERLIN, ASSASSIN)
        );
    private ArrayList<Character> selectableGoodCharacters =
        new ArrayList<>(
            Arrays.asList(PERCIVAL, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR, LOYAL_SERVANT_OF_ARTHUR)
        );
    private ArrayList<EvilCharacter> selectableEvilCharacters =
        new ArrayList<>(
          Arrays.asList(MORDRED, MORGANA, OBERON, MINION_OF_MORDRED, MINION_OF_MORDRED, MINION_OF_MORDRED)
        );

    private List<Player> players = new ArrayList<>();
    private List<Player> questTeam = new ArrayList<>();

    ;
    private ArrayList<Boolean> votes = new ArrayList<>();
    private Player leader;
    private int questCount;
    private boolean isMerlinAssassinated = true;
    private int questCountLost;
    private int round = 1;

    public void run() {
        for(Player player: players) {
            player.sendMessage("/start Avalon has started");
        }
        distributeCharacters();
        pickLeader();

    }
    private void distributeCharacters() {
        GoodEvilDistribution.setDistribution();
        Pair<Integer, Integer> goodEvilDistribution = GoodEvilDistribution.distribution.get(maxPlayerCount);
        int goodCharacters = goodEvilDistribution.getFirst() - 1;
        int evilCharacters = goodEvilDistribution.getSecond() - 1;

        Collections.shuffle(players);
        players.get(0).setCharacter(MERLIN);
        players.get(1).setCharacter(ASSASSIN);
        int idxToContinueSelection = 2;
        idxToContinueSelection = assignCharacters(selectableGoodCharacters,goodCharacters, idxToContinueSelection);
        assignCharacters(selectableEvilCharacters, evilCharacters, idxToContinueSelection);
    }
    private void pickLeader() {
        leader = players.remove(0);
        leader.sendMessage("/leader true");
        for(Player player: players) {
            player.sendMessage("/leader " + leader.username);
        }
        players.add(leader);

    }
    private <T> int assignCharacters(List<T> selectableCharacters, int goodCharacters, int idxToContinueSelection) {
        for(int i = 0; i < goodCharacters; i++, idxToContinueSelection++) {
            int idx = random.nextInt(selectableCharacters.size());
            Character character = (Character) selectableCharacters.remove(idx);
            players.get(idxToContinueSelection).setCharacter(character);
        }
        return idxToContinueSelection;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean hasWon() {
        if(questCountLost == 3) {
            return false;
        }
        if(questCount == 0){
            return false;
        }

        return isMerlinAssassinated;
    }
    private void resetGame() {
        votes.clear();
    }
}
