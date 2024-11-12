package server.handler;

import request.ClearRequest;
import response.ClearResponse;
import server.service.ClearService;
import spark.Route;

public class ClearHandler extends Handler<ClearRequest, ClearResponse> implements Route {
    @Override
    protected ClearResponse performRequest(ClearRequest reqObj) throws Exception {
        return new ClearService().clear();
    }

    @Override
    protected Class<ClearRequest> getRequestClassType() {
        return ClearRequest.class;
    }
}
