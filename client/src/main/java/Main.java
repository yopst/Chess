import chess.*;
import repl.PreLoginRepl;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        System.out.println("♕ 240 Chess client.Client: ");

        if (args.length == 1) {
            serverUrl = args[0];
        }
        new PreLoginRepl(serverUrl).run();
    }
}