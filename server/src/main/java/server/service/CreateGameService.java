package server.service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.memory.MemoryAuth;
import dataaccess.interfaces.memory.MemoryGame;
import request.CreateGameRequest;
import response.CreateGameResponse;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;


public class CreateGameService {

    GameDAO gamesDB = new MemoryGame();
    AuthDAO auth = new MemoryAuth();

    public CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) throws EndpointException {
        try {
            if (auth.getAuth(authToken) == null) {
                throw new UnauthorizedException("unauthorized");
            }
            //Allows multiple games of the same name.
            return new CreateGameResponse(gamesDB.createGame(createGameRequest.gameName()));
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
    }


}