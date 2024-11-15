package repl;

import client.Client;
import webSocketMessages.Notification;
import websocket.NotificationHandler;

public class PostLoginRepl extends Repl implements NotificationHandler{
    public PostLoginRepl(String serverUrl) {
        super(serverUrl);
    }

    public PostLoginRepl(Client client) {
        super(client);
    }


    public String help() {
        return """
                - create <GAMENAME>
                - join <ID>
                - list
                - logout
                - quit
                - help
                - observe <ID>
                """;
    }
}
