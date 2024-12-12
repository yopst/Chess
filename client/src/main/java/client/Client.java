package client;

import chess.ChessBoard;
import chess.ChessGame.*;
import chess.ChessMove;
import chess.ChessPosition;
import client.exceptions.ResponseException;
import com.google.gson.Gson;
import model.GameDataListItem;
import repl.*;
import request.*;
import response.*;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class Client {
    public State state = State.SIGNEDOUT;
    private String visitorUsername = null;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private final String serverUrl;
    private final ServerFacade server;

    private Integer joinedGameID;

    public ChessBoard blankInitializedBoard;

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

    public void enterGameRepl(TeamColor color) throws ResponseException {
        if (state != State.GAMING && state != State.OBSERVING) {
            throw new RuntimeException("Stop it right there!");
        }
        else {
            if (state == State.OBSERVING) {color = null;}
            notificationHandler = new GameRepl(this, visitorUsername, joinedGameID, color);

            ws = new WebSocketFacade(serverUrl,
                    notificationHandler,
                    color,
                    joinedGameID);

            ws.enterGame(server.authToken, joinedGameID);

            GameRepl gr = (GameRepl) notificationHandler;
            gr.run(this);
        }
    }

    public void switchState() {
        state = (state == State.SIGNEDIN) ? State.SIGNEDOUT : State.SIGNEDIN;
    }

    public String getUserPrompt() {
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
                case "resign" -> resign();
                case "move" -> move(params);
                case "redraw" -> redraw();
                case "leave" -> leave();
                default -> notificationHandler.help();
            };
        } catch (ResponseException ex) {
            return handleError(ex.getStatusCode());
        }
    }

    private String resign() throws ResponseException {
        mustBeGaming();

        ws.resignGame(server.authToken, joinedGameID);
        state = State.OBSERVING;
        return "";
    }

    private String move(String... params) throws ResponseException {
        mustBeGaming();
        if (params.length != 2) {
            System.out.println("Expected: <start> <end>");
            throw new ResponseException(402, "Incorrect number of Parameters");
        }
        ChessPosition start = stringPositionToChessPosition(params[0]);
        ChessPosition end = stringPositionToChessPosition(params[1]);
        ChessMove move = new ChessMove(start, end);
        String stringMove = serialize(move);

        ws.makeMove(server.authToken, joinedGameID, stringMove);
        return "";
    }

    private String serialize(Object o) {
        return new Gson().toJson(o);
    }

    private ChessPosition stringPositionToChessPosition(String position) throws ResponseException{
        if (position == null || position.length() != 2 ||
                !Character.isLetter(position.charAt(0)) ||
                !Character.isDigit(position.charAt(1))) {
            System.out.println("Expected the column letter followed by the row number. \n " +
                    "For example: 'd1' is the starting position of the White Queen.");
            throw new ResponseException(400, "Invalid chess position:" + position);
        }

        // Normalize column to lowercase to handle both 'a'-'h' and 'A'-'H'
        char columnChar = Character.toLowerCase(position.charAt(0));
        int col = columnChar - 'a' + 1; // 'a' maps to 1
        int row = position.charAt(1) - '0'; // Convert digit to int

        // Ensure the values are within valid chessboard bounds
        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new ResponseException(400, "Position out of chessboard bounds: " + position);
        }

        return new ChessPosition(row, col);
    }


    private String redraw() throws ResponseException{
        mustNotBeLoggedIn();
        System.out.println("Are you sure that you want to resign?");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    private String leave() throws ResponseException {

        if (state != State.GAMING && state != State.OBSERVING) {
            throw new ResponseException(400, "not in a game");
        }
        if (joinedGameID == null) {
            throw new ResponseException(400, "No joined gameID");
        }
        ws.leaveGame(server.authToken, joinedGameID);
        joinedGameID = null;

        return "quit";
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

    public void mustBeGaming() throws ResponseException {
        if (state == State.OBSERVING) {
            throw new ResponseException(401, "not playing");
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
        mustBeLoggedIn();
        server.logout(new LogoutRequest());
        enterNextRepl();
        PreLoginRepl preLogin = (PreLoginRepl) notificationHandler;
        preLogin.run(this);
        return "quit";
    }

    public String list() throws ResponseException {
        mustBeLoggedIn();
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

            joinedGameID = gameID;
            state = State.GAMING;
            enterGameRepl(color);
        }
        catch (NumberFormatException e) {
            throw new ResponseException(400, "<ID> Must be a Number");
        }

        return "";
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
        joinedGameID = gameID;
        state = State.OBSERVING;
        enterGameRepl(TeamColor.WHITE);

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
