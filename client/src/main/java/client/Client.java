package client;

import client.exceptions.ResponseException;
import repl.PreLoginRepl;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;
import websocket.NotificationHandler;

import java.util.Arrays;

public class Client {
    public State state = State.SIGNEDOUT;

    private String visitorUsername = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
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
            state = State.SIGNEDIN;
            visitorUsername = params[0];
            String password = params[1];
            String email = params[2];

            server.register(new RegisterRequest(visitorUsername,password,email));

            return String.format("You signed in as %s.", visitorUsername);
        }
        System.out.println("Expected: <username> <password> <email>");
        throw new ResponseException(402, "Incorrect number of Parameters");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            visitorUsername = params[0];
            String password = params[1];

            server.login(new LoginRequest(visitorUsername,password));

            state = State.SIGNEDIN;

            return String.format("You signed in as %s.", visitorUsername);
        }
        System.out.println("Expected: <username> <password>");
        throw new ResponseException(402, "Incorrect number of Parameters");
    }

    public String help() {
        return notificationHandler.help();
    }

    private String handleError(int status) {
        return switch (status) {
            case 500 -> "Server Error";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized.";
            case 402 -> "Wrong number of Parameters";
            case 403 -> "User Already Exists";
            case 404 -> "Not Found";
            default -> "Unknown";
        };
    }
}
