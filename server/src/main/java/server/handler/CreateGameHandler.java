package server.handler;

import request.CreateGameRequest;
import response.CreateGameResponse;
import server.service.CreateGameService;
import spark.Route;

public class CreateGameHandler extends Handler<CreateGameRequest, CreateGameResponse> implements Route {
    @Override
    protected CreateGameResponse performRequest(CreateGameRequest reqObj) throws Exception {
        return new CreateGameService().createGame(reqObj, authToken);
    }
    @Override
    protected Class<CreateGameRequest> getRequestClassType() {
        return CreateGameRequest.class;
    }

}