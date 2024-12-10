package repl;

import client.Client;
import websocketmessages.Notification;
import websocket.NotificationHandler;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {

    public void run(Client client) {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt(client);
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (!Objects.equals(result, "quit")) {
                    System.out.print(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt(Client client) {
        System.out.print("\n" + RESET_TEXT_COLOR + client.getUser() + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String help() {
        return "Generic REPL";
    }
}
