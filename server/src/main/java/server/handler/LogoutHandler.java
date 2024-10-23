package server.handler;

import request.LogoutRequest;
import response.LogoutResponse;
import server.service.LogoutService;
import spark.Route;

public class LogoutHandler extends Handler<LogoutRequest, LogoutResponse> implements Route {
    @Override
    protected LogoutResponse performRequest(LogoutRequest reqObj) throws Exception {
        return new LogoutService().logout(authToken);
    }

    @Override
    protected Class<LogoutRequest> getRequestClassType() {
        return LogoutRequest.class;
    }
}
