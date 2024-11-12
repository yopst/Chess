package server.handler;

import chess.ChessGame;
import request.JoinGameRequest;
import response.JoinGameResponse;
import server.exception.EndpointException;
import server.service.JoinGameService;
import spark.Route;

public class JoinGameHandler extends Handler<JoinGameRequest, JoinGameResponse> implements Route {
    @Override
    protected JoinGameResponse performRequest(JoinGameRequest reqObj) throws Exception {
        return new JoinGameService().joinGame(reqObj, authToken);
    }

    @Override
    protected Class<JoinGameRequest> getRequestClassType() {
        return null;
    }
}
