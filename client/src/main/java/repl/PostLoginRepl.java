package repl;

import client.Client;
import client.State;
import client.exceptions.ResponseException;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class PostLoginRepl extends Repl implements NotificationHandler{
    Client client;

    public PostLoginRepl(Client client) {
        this.client = client;
        System.out.println("You signed in as " + client.getUserPrompt() + RESET_TEXT_COLOR);
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

    @Override
    public void notify(ServerMessage notification) {

    }
}
