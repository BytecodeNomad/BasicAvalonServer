package avalon.server;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager {
    CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }
}
