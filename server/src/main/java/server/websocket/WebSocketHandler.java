package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MyDatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;

import model.GameData;
import model.GameDataListItem;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Collection;

import model.AuthData;
import websocket.messages.ErrorMessage;

import javax.xml.crypto.Data;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;

@WebSocket
public class WebSocketHandler {
    private final AuthDAO auths;
    private final GameDAO games;

    enum UserRole {WHITE, BLACK, OBSERVER}
    UserRole userRole;
    String authToken;

    public WebSocketHandler() {
        MyDatabaseManager db = MyDatabaseManager.getInstance();
        this.auths = db.getAuth();
        this.games = db.getGames();
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        authToken = action.getAuthToken();
        switch (action.getCommandType()) {
            case CONNECT -> connect(authToken, action.getGameID(), session);
            case MAKE_MOVE -> makeMove(message);
            case LEAVE -> leave(authToken);
            case RESIGN -> resign(authToken);
        }
    }

    private String getUserName(String authToken) {
        AuthData authData = null;
        try {
            authData = auths.getAuth(authToken);
        } catch (DataAccessException e) {
            handleErrorMessage("Invalid auth: " + e.getMessage());
        }
        if (authData != null) { return authData.username(); }
        return null;
    }

    private void handleErrorMessage(String errorMessage) {
        String error = "Error: " + errorMessage;
        try {
            connections.get(authToken).send(new ErrorMessage(error).toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }
    
    private void setRole(String userName, int gameId) {
        Collection<GameDataListItem> listGames = games.listGames();
        boolean isPresent = false;

        String whiteUserName = null;
        String blackUserName = null;
        
        for (GameDataListItem gameData : listGames) {
            if (gameData.gameID() == gameId) {
                isPresent = true;
                String username = gameData.whiteUsername() != null ? gameData.whiteUsername() : null;
                if (userName.equals(username)) {
                    userRole = UserRole.WHITE;
                } else {
                    String blackUsername = gameData.blackUsername() != null ? gameData.blackUsername() : null;
                    if (userName.equals(blackUsername)) {
                        userRole = UserRole.BLACK;
                    }
                }
                return;
            }
        }

        if (!isPresent) {
            handleErrorMessage("Game not found");
        }
        else if (userRole == null){
            userRole = UserRole.OBSERVER;
        }
    }
    public String getRole(String userName, int gameID) {
        setRole(userName, gameID);
        switch (userRole) {
            case WHITE -> {
                return "white";
            }
            case BLACK -> {
                return "black";
            }
            case OBSERVER -> {
                return "observer";
            }
        }
        return null;
    }


    private void connect(String authToken, Integer gameID ,Session session) {
        try {
        String userName = getUserName(authToken);
        connections.add(authToken, userName, session);
            GameData gameData = games.getGame(gameID);

        ChessGame chessGame = gameData.game();
        String gameState = serializeGame(chessGame);

        LoadGameMessage loadGameMessage = new LoadGameMessage(gameState);
        connections.get(authToken).send(loadGameMessage.toString());


        String message = String.format("%s has joined the game as %s", userName, getRole(userName, gameID));
        Notification broadcastMessage = new Notification(message);
        connections.broadcastExcept(userName, broadcastMessage);
        } catch (Exception e) {
            handleErrorMessage(e.getMessage());
        }
    }

    private void makeMove(String serializedMakeMoveCommand) {
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(serializedMakeMoveCommand, MakeMoveCommand.class);
        ChessMove move = makeMoveCommand.deserializeChessMove();
        String authToken = makeMoveCommand.getAuthToken();
        getUserName(authToken);
        Integer gameId = makeMoveCommand.getGameID();
        try {
            GameData gameData = games.getGame(gameId);
            ChessGame chessGame = gameData.game();
            chessGame.makeMove(move);
            String gameState = serializeGame(chessGame);

            LoadGameMessage loadGameMessage = new LoadGameMessage(gameState);
            connections.broadcast(loadGameMessage);
        }
        catch (Exception e) {
            handleErrorMessage(e.getMessage());
        }
    }

    private void leave(String authToken) {
        String userName = getUserName(authToken);
        connections.remove(authToken);
        String message = String.format("%s has left.", userName);
        Notification notification = new Notification(message);
        try {
            connections.broadcastExcept(userName, notification);
        }
        catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }
    }

    private void resign(String authToken) {
        String userName = getUserName(authToken);
        String message = String.format("%s has resigned.", userName);
        Notification notification = new Notification(message);
        try {
            connections.broadcastExcept(userName, notification);
        }
        catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }
    }
}
