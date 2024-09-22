package avalon.server;

import java.util.HashMap;
import java.util.Map;

public class CharacterAssignment {
    public static Map<Integer, Pair<Integer, Integer>> assignments = new HashMap<>();

    public static Map<Integer, Pair<Integer, Integer>> getAssignments() {
        assignments.put(5, new Pair<>(3,2));
        assignments.put(6, new Pair<>(4,2));
        assignments.put(7, new Pair<>(4,3));
        assignments.put(8, new Pair<>(5,3));
        assignments.put(9, new Pair<>(6,3));
        assignments.put(10, new Pair<>(6,4));

        return assignments;
    }
}
