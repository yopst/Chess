package server.service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.memory.MemoryAuth;
import response.LogoutResponse;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;

public class LogoutService {
    AuthDAO auth = new MemoryAuth();

    public LogoutResponse logout(String authToken) throws EndpointException {
        try {
            // could expand to have a user associated with the request that can be checked against the database
            // to throw an unauthorized exception if they don't match
            if (authToken == null) {
                throw new UnauthorizedException("unauthorized");
            }
            auth.deleteAuth(authToken); //throws data access if authToken not mapped
            return new LogoutResponse();
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
    }
}
