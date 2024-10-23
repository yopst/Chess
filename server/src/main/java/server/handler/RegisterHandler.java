package server.handler;

import request.RegisterRequest;
import response.RegisterResponse;
import server.service.RegisterService;
import spark.Route;

public class RegisterHandler extends Handler<RegisterRequest, RegisterResponse> implements Route {
    @Override
    protected RegisterResponse performRequest(RegisterRequest reqObj) throws Exception {
        return new RegisterService().register(reqObj);
    }

    @Override
    protected Class<RegisterRequest> getRequestClassType() {
        return RegisterRequest.class;
    }
}
