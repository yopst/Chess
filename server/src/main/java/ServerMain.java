import chess.*;
import server.Server;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server();
        var port = 8080;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        server.run(8080);
        System.out.println("♕ 240 Chess Server: " + port);
    }
}