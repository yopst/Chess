package server.service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.interfaces.memory.MemoryAuth;
import dataaccess.interfaces.memory.MemoryUser;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import response.LoginResponse;
import server.exception.BadRequestException;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;

public class LoginService {
    AuthDAO auth = new MemoryAuth();
    UserDAO user = new MemoryUser();

    public LoginResponse login(LoginRequest loginRequest) throws EndpointException {
        try {
            if (loginRequest.username() == null ||
                    loginRequest.password() == null) {
                throw new BadRequestException("bad request");
            }
            UserData userData = user.getUser(loginRequest.username());
            if (userData == null || !loginRequest.password().equals(userData.password())) {
                throw new UnauthorizedException("unauthorized");
            }
            AuthData authData = AuthData.createWithRandomToken(userData.username());
            auth.createAuth(userData.username());
            return new LoginResponse(userData.username(), authData.authToken());
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
    }
}
