package avalon.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ArrayList<String> availablePlayers = new ArrayList<>(
      Arrays.asList(
          "Amina", "Liam", "Hiroshi", "Fatima", "Carlos", "Yasmin", "Ananya", "Javier", "Zara", "Omar"
      )
    );
    static Random random = new Random(15);
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        int startIdx = random.nextInt(5);
        int endIdx = startIdx + 5;

        List<String> playersInLobby = availablePlayers.subList(startIdx, endIdx);

        for(String name: playersInLobby) {
            service.execute(() -> new ClientConnectionHandler(name).connect());
        }
    }
}