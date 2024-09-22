package avalon.server;

public class Main {
    public static void main(String[] args) {
        AvalonServer server = new AvalonServer(5000);
        server.connect();
        server.startGame();
    }
}
