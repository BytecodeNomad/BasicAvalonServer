package avalon.server;

import java.util.HashMap;
import java.util.Map;

public class GoodEvilDistribution {
    static Map<Integer, Pair> distribution = new HashMap<>();

    public static void setDistribution() {
        distribution.put(5, new Pair(3,2));
        distribution.put(6, new Pair(4,2));
        distribution.put(7, new Pair(4,3));
        distribution.put(8, new Pair(5,3));
        distribution.put(9, new Pair(6,3));
        distribution.put(10, new Pair(6,4));
    }
}
