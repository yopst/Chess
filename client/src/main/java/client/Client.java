package client;

import client.exceptions.ResponseException;
import repl.PreLoginRepl;

import java.util.Arrays;

public class Client {
    private State state = State.SIGNEDOUT;

    public Client(String serverUrl, PreLoginRepl preLoginRepl) {
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> quit(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        return null;
    }

    public String login(String... params) throws ResponseException{
        return null;
    }

    public String quit(String... params) throws ResponseException{
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <username> <password> <email> - to create an account
                    - signIn <username>  <password>
                    - quit
                    - help - with possible commands
                    """;
        }
        else {
            return """
                    
                    """;

        }
    }
}
