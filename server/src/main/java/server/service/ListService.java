package server.service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.memory.MemoryAuth;
import dataaccess.interfaces.memory.MemoryGame;
import model.AuthData;
import model.GameData;
import request.ListRequest;
import response.ListResponse;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;

import java.util.Collection;

public class ListService {
    GameDAO gamesDB = new MemoryGame();
    AuthDAO auth = new MemoryAuth();

    public ListResponse listGames(String authToken) throws EndpointException {
        try {
            if (auth.getAuth(authToken) == null) {
                throw new UnauthorizedException("unauthorized");
            }
            Collection<GameData> games = gamesDB.listGames();
            return new ListResponse(games);
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
    }
}
