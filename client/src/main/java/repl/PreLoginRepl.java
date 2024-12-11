package repl;

import client.Client;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

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
        System.out.println("Successfully signed out " + client.getUserPrompt() + RESET_TEXT_COLOR);
    }

    public String help() {
        return """
                - register <USERNAME> <PASSWORD> <EMAIL>
                - login <USERNAME> <PASSWORD>
                - quit
                - help
                """;
    }

    @Override
    public void notify(ServerMessage notification) {

    }
}
