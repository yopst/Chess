import client.Client;
import repl.PreLoginRepl;
import repl.Repl;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        var port = 8080;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
            serverUrl = "http://localhost:" + args[0];
        }

        System.out.println("♕ 240 Chess client: " + port);
        Repl repl = new PreLoginRepl();
        Client client = new Client(serverUrl, repl);
        repl.run(client);
    }
}