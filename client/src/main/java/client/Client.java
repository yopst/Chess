package client;

import client.exceptions.ResponseException;
import repl.*;
import request.*;
import response.*;
import websocket.NotificationHandler;

import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class Client {
    public State state = State.SIGNEDOUT;

    private String visitorUsername = null;
    private final ServerFacade server;
    private final String serverUrl;
    private NotificationHandler notificationHandler;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public void enterNextRepl() {
        switchState();

        String action = (state == State.SIGNEDIN) ? "You signed in as" : "You signed out";
        String msg = String.format("%s %s.", action, visitorUsername);
        System.out.print(msg + RESET_TEXT_COLOR);

        Repl repl = (state == State.SIGNEDIN) ? new PreLoginRepl(serverUrl): new PostLoginRepl(serverUrl);
        repl.run();
    }

    public void switchState() {
        if (state == State.SIGNEDIN) {
            state = State.SIGNEDOUT;
        }
        else {
            state = State.SIGNEDIN;
        }
    }

    public String getUser() {
        if (state == State.SIGNEDOUT) {
            return "";
        }
        else {
            return "[" + visitorUsername + "]\n";
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
            return handleError(ex.getStatusCode());
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            visitorUsername = params[0];
            String password = params[1];
            String email = params[2];

            server.register(new RegisterRequest(visitorUsername,password,email));
            login(visitorUsername,password);
            return "";
        }
        System.out.println("Expected: <username> <password> <email>");
        throw new ResponseException(402, "Incorrect number of Parameters");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            visitorUsername = params[0];
            String password = params[1];

            server.login(new LoginRequest(visitorUsername,password));
            enterNextRepl();
            return "";
        }
        System.out.println("Expected: <username> <password>");
        throw new ResponseException(402, "Incorrect number of Parameters");
    }

    public String help() {
        return notificationHandler.help();
    }

    public String logout() throws ResponseException {
        server.logout(new LogoutRequest());

        enterNextRepl();
        return "";
    }

    private String handleError(int status) {
        return switch (status) {
            case 500 -> "Server Error";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 402 -> "Wrong number of Parameters";
            case 403 -> "User Already Exists";
            case 404 -> "Not Found";
            default -> "Unknown";
        };
    }
}
