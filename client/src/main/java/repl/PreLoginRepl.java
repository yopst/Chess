package repl;

import client.Client;
import webSocketMessages.Notification;
import websocket.NotificationHandler;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLoginRepl extends Repl implements NotificationHandler {
    private final Client client;

    public PreLoginRepl(String serverUrl) {
        this.client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.print(
        """
        ___________________________________________\s
           ██████  ██   ██ ███████  ██████  ██████ \s
          ██       ██   ██ ██      ██      ██      \s
          ██       ███████ █████     █████  ██████ \s
          ██       ██   ██ ██            ██      ██\s
           ██████  ██   ██ ███████  ██████  ██████ \s
                                                   \s
                 ♚ || Sign in to start || ♔        \s
        ___________________________________________\s
        """
        );

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notify(Notification notification) {
        System.out.println(SET_BG_COLOR_RED + notification.message());
        printPrompt();
    }
}
