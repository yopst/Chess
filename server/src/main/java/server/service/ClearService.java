package server.service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.interfaces.memory.MemoryAuth;
import dataaccess.interfaces.memory.MemoryGame;
import dataaccess.interfaces.memory.MemoryUser;
import response.ClearResponse;
import server.exception.EndpointException;

public class ClearService {
    GameDAO games = new MemoryGame();
    AuthDAO auth = new MemoryAuth();
    UserDAO users = new MemoryUser();

    public ClearResponse clear() throws EndpointException {
        try {
            games.clear();
            auth.clear();
            users.clear();
        }
        catch(DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
        return new ClearResponse();
    }
}
