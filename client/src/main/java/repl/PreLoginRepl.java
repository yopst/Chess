package repl;

import client.Client;
import webSocketMessages.Notification;
import websocket.NotificationHandler;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLoginRepl extends Repl implements NotificationHandler {
    private static final String INTRO = """
                    ___________________________________________\s
                       ██████  ██   ██ ███████  ██████  ██████ \s
                      ██       ██   ██ ██      ██      ██      \s
                      ██       ███████ █████     █████  ██████ \s
                      ██       ██   ██ ██            ██      ██\s
                       ██████  ██   ██ ███████  ██████  ██████ \s
                                                               \s
                             ♚ || Sign in to start || ♔        \s
                    ___________________________________________\s
                    """;

    public PreLoginRepl(String serverUrl) {
        super(serverUrl);
    }

    public PreLoginRepl(Client client) {
        super(client);
    }

    public void run() {
        System.out.print(INTRO);
        super.run();
    }

    public String help() {
        return """
                - register <USERNAME> <PASSWORD> <EMAIL>
                - login <USERNAME> <PASSWORD>
                - quit
                - help
                """;
    }
}
