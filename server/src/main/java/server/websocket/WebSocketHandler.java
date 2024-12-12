package server.websocket;

import com.google.gson.Gson;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MyDatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;

import model.GameData;
import model.GameDataListItem;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
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
    private AuthDAO auths;
    private GameDAO games;
    private GameData gameData;
    enum UserRole {WHITE, BLACK, OBSERVER}
    UserRole userRole;

    public WebSocketHandler() {
        MyDatabaseManager db = MyDatabaseManager.getInstance();
        this.auths = db.getAuth();
        this.games = db.getGames();
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(action.getAuthToken(), action.getGameID(), session);
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
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

    private ErrorMessage handleErrorMessage(String errorMessage) {
        String error = "Error: ";
        switch (errorMessage) {
            case "Invalid auth: Invalid auth token" -> errorMessage = error + "Invalid auth token";

        }
        return new ErrorMessage(errorMessage);
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
                if (username != null && userName.equals(username)) {
                    userRole = UserRole.WHITE;
                } else {
                    String blackUsername = gameData.blackUsername() != null ? gameData.blackUsername() : null;
                    if (blackUsername != null && userName.equals(blackUsername)) {
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
        gameData = games.getGame(gameID);

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

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}
