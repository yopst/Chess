package repl;

import client.Client;
import client.State;
import websocket.NotificationHandler;

import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class PostLoginRepl extends Repl implements NotificationHandler{

    public PostLoginRepl(Client client) {
        System.out.println("You signed in as " + client.getUser() + RESET_TEXT_COLOR);
        super.run(client);
    }


    public String help() {
        return """
                - create <GAMENAME>
                - join <WHITE|BLACK> <ID>
                - observe <ID>
                - list
                - logout
                - quit
                - help
                """;
    }
}
