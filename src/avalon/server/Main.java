package avalon.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        try {
            new AvalonServer().connect();
        } catch (IOException e) {
            System.out.println("Could not start the server");
        }

    }
}
