package server.handler;

import server.exception.EndpointException;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public abstract class Handler<T, R> implements Route {
    //R to denote Generic Response Type
    private final Gson gson = new Gson();

    protected record ErrorMessage(String message) {}
    protected record ErrorResponse(int status, ErrorMessage message) {}

    protected String authToken;

    protected T deserialize(Request httpReq, Class<T> clazz) {
        // Use Gson to deserialize the JSON request body into the specified class type
        authToken = httpReq.headers("authorization");
        return gson.fromJson(httpReq.body(), clazz);
    }

    protected String serialize(R responseObj) {
        // Serialize the response object to JSON
        return gson.toJson(responseObj);
    }

    private void sendError(Response response, int status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status, new ErrorMessage(message));
        response.status(status);
        response.header("Content-Type", "application/json");  // Set content type to JSON
        response.body(gson.toJson(errorResponse));  // Serialize error response
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            T requestObj = deserialize(request, getRequestClassType());
            R successResponse = performRequest(requestObj);
            //successfully performed request
            response.status(200);
            response.header("Content-Type", "application/json");  // Set content type to JSON
            String serializedResponse = serialize(successResponse);
            response.body(serializedResponse);
            return serializedResponse;  // Return the entire response
        } catch (EndpointException e) {
            // Handle specific endpoint exceptions
            sendError(response, e.getErrorCode(), e.getMessage());
            return response;
        } catch (Exception e) {
            // Handle general exceptions
            sendError(response, 500, e.getMessage());
            return response;
        }
    }

    // Abstract methods that need to be implemented by subclasses
    protected abstract R performRequest(T reqObj) throws Exception;
    protected abstract Class<T> getRequestClassType();
}
