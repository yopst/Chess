package repl;

import webSocketMessages.Notification;
import websocket.NotificationHandler;

public class PostLoginRepl extends Repl implements NotificationHandler{
    public PostLoginRepl(String serverUrl) {
        super(serverUrl);
    }

    public String help() {
        return """
                - create <GAMENAME>
                - join <ID>
                - list
                - logout
                - quit
                - help
                """;
    }
}
