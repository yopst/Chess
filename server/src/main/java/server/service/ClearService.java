package server.service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.interfaces.*;
import response.ClearResponse;
import server.exception.EndpointException;

public class ClearService {
    private final GameDAO games;
    private final AuthDAO auth;
    private final UserDAO users;

    public ClearService() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        games = dbManager.getGames();
        auth = dbManager.getAuth();
        users = dbManager.getUsers();
    }
    public ClearResponse clear() throws EndpointException {
        try {
            games.clear();
            auth.clear();
            users.clear();
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
        return new ClearResponse();
    }
}
