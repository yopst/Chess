package server.service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import model.GameData;
import request.JoinGameRequest;
import response.JoinGameResponse;
import server.exception.BadRequestException;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;

public class JoinGameService {
    private final GameDAO games;
    private final AuthDAO auth;

    public JoinGameService() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        games = dbManager.getGames();
        auth = dbManager.getAuth();
    }

    private Boolean canJoin(GameData gameData, ChessGame.TeamColor teamColor) {
        boolean alreadyWhitePlayer = gameData.whiteUsername() != null;
        boolean alreadyBlackPlayer = gameData.blackUsername() != null;
        if (teamColor == ChessGame.TeamColor.WHITE) return !alreadyWhitePlayer;
        return !alreadyBlackPlayer;
    }

    public JoinGameResponse joinGame(JoinGameRequest joinRequest, String authToken) throws EndpointException {
        try {
            if (auth.getAuth(authToken) == null) {
                throw new UnauthorizedException("unauthorized");
            }

            String username = auth.getAuth(authToken).username();
            if (username == null) throw new BadRequestException("bad request");

            GameData gameData = games.getGame(joinRequest.gameID());

            if (canJoin(gameData, joinRequest.teamColor())) {
                games.updateGame(gameData.GameID(),username,joinRequest.teamColor());
            }
            else {
                throw new EndpointException("team color already taken", 500);
            }

            return new JoinGameResponse();
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
    }
}
