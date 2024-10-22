package server.handler;

import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public abstract class Handler<T, R> implements Route {
    private final Gson gson = new Gson();

    protected record ErrorMessage(String message) {}
    protected record ErrorResponse(int status, ErrorMessage message) {}

    protected T deserialize(Request httpReq, Class<T> clazz) {
        // Use Gson to deserialize the JSON request body into the specified class type
        return gson.fromJson(httpReq.body(), clazz);
    }

    protected String serialize(R responseObj) {
        // Serialize the response object to JSON
        return gson.toJson(responseObj);
    }

    private void formatSuccess(Response response, R success) {
        response.status(200);
        response.body(serialize(success));
    }

    private void formatError(Response response, ErrorResponse error) {
        response.status(error.status());
        response.body(serialize(error.message()));
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            T requestObj = deserialize(request, getRequestClassType());
            R successResponse = performRequest(requestObj);
            formatSuccess(response, successResponse);
            return response.body();  // Return the response body for the Spark route
        } catch (Exception e) {
            // Here, you need a way to obtain an error code; modify as needed
            ErrorResponse error = new ErrorResponse(500, new ErrorMessage(e.getMessage()));
            formatError(response, error);
            return response.body(); // Return the response body for the Spark route
        }
    }

    // Abstract methods that need to be implemented by subclasses
    protected abstract R performRequest(T reqObj) throws Exception;
    protected abstract Class<T> getRequestClassType();
}
