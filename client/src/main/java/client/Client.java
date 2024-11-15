package client;

import client.exceptions.ResponseException;
import model.GameDataListItem;
import repl.*;
import request.*;
import response.*;
import websocket.NotificationHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
        String msg = String.format("%s %s.\n", action, visitorUsername);
        System.out.print(msg + RESET_TEXT_COLOR);

        Repl repl = (state == State.SIGNEDIN) ? new PostLoginRepl( this): new PreLoginRepl(this);
        notificationHandler = repl;
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
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            //System.out.println(ex.getMessage());
            return handleError(ex.getStatusCode());
        }
    }

    public void dontDupLogIn() throws ResponseException {
        if (state == State.SIGNEDIN) throw new ResponseException(403, "Already Logged In");
    }

    public String register(String... params) throws ResponseException {
        dontDupLogIn();
        if (params.length == 3) {
            visitorUsername = params[0];
            String password = params[1];
            String email = params[2];

            server.register(new RegisterRequest(visitorUsername,password,email));
            enterNextRepl();
            return "";
        }
        System.out.println("Expected: <username> <password> <email>");
        throw new ResponseException(402, "Incorrect number of Parameters");
    }

    public String login(String... params) throws ResponseException {
        dontDupLogIn();
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

    public String list() throws ResponseException {
        ListResponse response = server.list(new ListRequest());
        Collection<GameDataListItem> games = response.games();
        StringBuilder sb = new StringBuilder();
        for (GameDataListItem item: games) {
            sb.append(item.gameID());
            sb.append(".  ");
            sb.append(item.gameName());
            sb.append(": white( ");
            sb.append(item.whiteUsername());
            sb.append(" ) black( ");
            sb.append(item.blackUsername());
            sb.append( ") \n");
        }
        return sb.toString();
    }

    public String create(String... params) throws ResponseException {
        if (params.length != 1) {
            System.out.println("Expected: <GAMENAME>");
            throw new ResponseException(402, "Incorrect number of Parameters");
        }
        CreateGameResponse response = server.createGame(new CreateGameRequest(params[0]));
        return list();
    }

    public String join(String... params) throws ResponseException {
        if (params.length != 1) {
            System.out.println("Expected: <ID>");
            throw new ResponseException(402, "Incorrect number of Parameters");
        }
        return null;
    }

    public String observe(String... params) throws ResponseException {
        if (params.length != 1) {
            System.out.println("Expected: <ID>");
            throw new ResponseException(402, "Incorrect number of Parameters");
        }
        return null;
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
