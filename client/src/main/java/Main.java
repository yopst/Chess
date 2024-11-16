import repl.PreLoginRepl;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(0);

        var serverUrl = "http://localhost:" + port;

        if (args.length == 1) {
            serverUrl = args[0];
        }

        System.out.println("♕ 240 Chess client: ");
        new PreLoginRepl(serverUrl).run();
        server.stop();
    }
}