package client;

import chess.ChessBoard;
import chess.ChessGame.*;
import client.exceptions.ResponseException;
import model.GameDataListItem;
import repl.*;
import request.*;
import response.*;
import ui.ChessBoardUI;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Collection;

public class Client {
    public State state = State.SIGNEDOUT;
    private String visitorUsername = null;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private final String serverUrl;
    private final ServerFacade server;

    private Integer joinedGameID;

    private ChessBoard blankInitializedBoard;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.notificationHandler = notificationHandler;
        this.serverUrl = serverUrl;
        blankInitializedBoard = new ChessBoard();
        blankInitializedBoard.resetBoard();
    }

    public void enterNextRepl() {
        switchState();
        if (state == State.SIGNEDIN) {
            notificationHandler = new PostLoginRepl(this);
        }
        else {
            notificationHandler = new PreLoginRepl(this);
        }
    }

    public void switchState() {
        state = (state == State.SIGNEDIN) ? State.SIGNEDOUT : State.SIGNEDIN;
    }

    public String getUser() {
        return (state == State.SIGNEDOUT) ? "": "[" + visitorUsername + "]\n";
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> quit();
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                default -> notificationHandler.help();
            };
        } catch (ResponseException ex) {
            return handleError(ex.getStatusCode());
        }
    }

    private String quit() {
        state = State.QUIT;
        return "quit";
    }

    public void mustNotBeLoggedIn() throws ResponseException {
        if (state == State.SIGNEDIN) {
            throw new ResponseException(403, "Already Logged In");
        }
    }

    public void mustBeLoggedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(401, "not signed in");
        }
    }

    public String register(String... params) throws ResponseException {
        mustNotBeLoggedIn();
        if (params.length == 3) {
            visitorUsername = params[0];
            String password = params[1];
            String email = params[2];

            server.register(new RegisterRequest(visitorUsername,password,email));
            enterNextRepl();
            return (state == State.QUIT) ? "quit": "";
        }
        System.out.println("Expected: <username> <password> <email>");
        throw new ResponseException(402, "Incorrect number of Parameters");
    }

    public String login(String... params) throws ResponseException {
        mustNotBeLoggedIn();
        if (params.length == 2) {
            visitorUsername = params[0];
            String password = params[1];

            server.login(new LoginRequest(visitorUsername,password));
            enterNextRepl();
            return (state == State.QUIT) ? "quit": "";
        }
        System.out.println("Expected: <username> <password>");
        throw new ResponseException(402, "Incorrect number of Parameters");
    }

    public String logout() throws ResponseException {
        server.logout(new LogoutRequest());

        if (joinedGameID == null) { throw new ResponseException(400, "No joined gameID"); }
        ws.leaveGame(server.authToken, joinedGameID);
        joinedGameID = null;

        enterNextRepl();
        PreLoginRepl preLogin = (PreLoginRepl) notificationHandler;
        preLogin.run(this);
        return "quit";
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
            sb.append( " )\n");
        }
        return sb.toString();
    }

    public String create(String... params) throws ResponseException {
        mustBeLoggedIn();
        if (params.length != 1) {
            System.out.println("Expected: <GAMENAME>");
            throw new ResponseException(402, "Incorrect number of Parameters");
        }
        CreateGameResponse response = server.createGame(new CreateGameRequest(params[0]));
        return list();
    }

    public String join(String... params) throws ResponseException {
        mustBeLoggedIn();
        if (params.length != 2) {
            System.out.println("Expected: <BLACK|WHITE> <ID>");
            throw new ResponseException(402, "Incorrect Number of Parameters");
        }

        TeamColor color = switch (params[0].toLowerCase()) {
            case "white" -> TeamColor.WHITE;
            case "black" -> TeamColor.BLACK;
            default -> throw new ResponseException(402, "<BLACK|WHITE> must be Black or White");
        };

        try {
            int gameID = Integer.parseInt(params[1]);
            server.joinGame(new JoinGameRequest(color,gameID));

            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.enterGame(server.authToken, gameID);
            joinedGameID = gameID;

            return new ChessBoardUI().createChessBoard(color, blankInitializedBoard);
        }
        catch (NumberFormatException e) {
            throw new ResponseException(400, "<ID> Must be a Number");
        }
    }

    public String observe(String... params) throws ResponseException {
        mustBeLoggedIn();
        if (params.length != 1) {
            System.out.println("Expected: <ID>");
            throw new ResponseException(402, "Incorrect number of Parameters");
        }
        int gameID;
        try {
            gameID = Integer.parseInt(params[0]);
        }
        catch (NumberFormatException e) {
            throw new ResponseException(400, "<ID> Must be a Number");
        }
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.enterGame(server.authToken, gameID);
        joinedGameID = gameID;

        return new ChessBoardUI().createChessBoard(TeamColor.WHITE, blankInitializedBoard);
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
