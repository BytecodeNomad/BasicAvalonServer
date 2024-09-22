package avalon.client;

import java.util.HashMap;
import java.util.Map;

public class TeamAssignment {
    public static TeamAssignment instance = new TeamAssignment();
    public final Map<Pair<Integer, Integer>, Integer> assignments = new HashMap<>();

    private TeamAssignment() {
        // 1st round
        assignments.put(new Pair<>(5, 1), 2);
        assignments.put(new Pair<>(6, 1), 2);
        assignments.put(new Pair<>(7, 1), 2);
        assignments.put(new Pair<>(8, 1), 3);
        assignments.put(new Pair<>(9, 1), 3);
        assignments.put(new Pair<>(10, 1), 3);

        // 2nd round
        assignments.put(new Pair<>(5, 2), 3);
        assignments.put(new Pair<>(6, 2), 3);
        assignments.put(new Pair<>(7, 2), 3);
        assignments.put(new Pair<>(8, 2), 4);
        assignments.put(new Pair<>(9, 2), 4);
        assignments.put(new Pair<>(10, 2), 4);

        //3rd round
        assignments.put(new Pair<>(5, 3), 2);
        assignments.put(new Pair<>(6, 3), 4);
        assignments.put(new Pair<>(7, 3), 3);
        assignments.put(new Pair<>(8, 3), 4);
        assignments.put(new Pair<>(9, 3), 4);
        assignments.put(new Pair<>(10, 3), 4);

        //4th round
        assignments.put(new Pair<>(5, 4), 3);
        assignments.put(new Pair<>(6, 4), 3);
        assignments.put(new Pair<>(7, 4), 4);
        assignments.put(new Pair<>(8, 4), 5);
        assignments.put(new Pair<>(9, 4), 5);
        assignments.put(new Pair<>(10, 4), 5);

        //5th round
        assignments.put(new Pair<>(5, 5), 3);
        assignments.put(new Pair<>(6, 5), 4);
        assignments.put(new Pair<>(7, 5), 4);
        assignments.put(new Pair<>(8, 5), 5);
        assignments.put(new Pair<>(9, 5), 5);
        assignments.put(new Pair<>(10, 5), 5);
    }
}

