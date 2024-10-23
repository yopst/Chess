package server.service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import dataaccess.interfaces.memory.MemoryUser;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.RegisterResponse;
import server.exception.BadRequestException;
import server.exception.EndpointException;
import server.exception.UserAlreadyExistsException;

public class RegisterService {
    private final UserDAO user = new MemoryUser();
    private final LoginService loginService = new LoginService();

    public RegisterResponse register(RegisterRequest registerRequest) throws EndpointException {
        try {
            if (registerRequest.username() == null ||
                    registerRequest.password() == null ||
                    registerRequest.email() == null) {
                throw new BadRequestException("bad request");
            }
            if (user.getUser(registerRequest.username()) != null)
                throw new UserAlreadyExistsException("already taken");
            user.createUser(new UserData(
                    registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()));
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }

        //login after successful registration (login creates and adds an AuthData entry)
        String authToken = loginService.login(
                new LoginRequest(registerRequest.username(),registerRequest.email())).authToken();
        return new RegisterResponse(registerRequest.username(), authToken);
    }
}
