package server.handler;

import request.LoginRequest;
import response.LoginResponse;
import server.service.LoginService;
import spark.Route;

public class LoginHandler extends Handler<LoginRequest, LoginResponse> implements Route {
    @Override
    protected LoginResponse performRequest(LoginRequest reqObj) throws Exception {
        return new LoginService().login(reqObj);
    }

    @Override
    protected Class<LoginRequest> getRequestClassType() {
        return LoginRequest.class;
    }
}
