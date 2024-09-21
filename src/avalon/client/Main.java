package avalon.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ArrayList<String> names = new ArrayList<>(
        Arrays.asList(
            "Amina",
            "Javier",
            "Mei Ling",
            "Rajesh",
            "Fatima",
            "Liam",
            "Chinedu",
            "Sofia",
            "Carlos",
            "Zainab"
        )
    );
    //"Amina",
    //    "Javier",
    //    "Mei Ling",
    //    "Rajesh",
    //    "Fatima",
    //    "Liam",
    //    "Chinedu",
    //    "Sofia",
    //    "Carlos",
    //    "Zainab"
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

//        int num = new Random().nextInt(11);
//        Collections.shuffle(names);
//
//        for(int i = 0; i < num; i++) {
//
//        }
        service.execute(() -> new ClientNetwork("Alice").connect());
        service.execute(() -> new ClientNetwork("Greg").connect());
        service.execute(() -> new ClientNetwork("Melinda").connect());
        service.execute(() -> new ClientNetwork("Bob").connect());
        service.execute(() -> new ClientNetwork("Musa").connect());

    }
}
