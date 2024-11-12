package server.handler;

import request.ListRequest;
import response.ListResponse;
import server.service.ListService;
import spark.Route;

public class ListHandler extends Handler<ListRequest, ListResponse> implements Route {
    @Override
    protected ListResponse performRequest(ListRequest reqObj) throws Exception {
        return new ListService().listGames(authToken);
    }

    @Override
    protected Class<ListRequest> getRequestClassType() {
        return ListRequest.class;
    }
}
