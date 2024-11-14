package repl;

import webSocketMessages.Notification;
import websocket.NotificationHandler;

public class PostLoginRepl extends Repl implements NotificationHandler{
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
