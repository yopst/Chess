package repl;

import client.Client;
import websocket.NotificationHandler;

import static ui.EscapeSequences.RESET_TEXT_COLOR;

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

    public PreLoginRepl() {
        System.out.print(INTRO);
    }
    public PreLoginRepl(Client client) {
        System.out.println("Successfully signed out " + client.getUser() + RESET_TEXT_COLOR);
        super.run(client);
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
