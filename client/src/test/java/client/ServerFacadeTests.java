package client;

import client.exceptions.ResponseException;
import org.junit.jupiter.api.*;
import request.*;
import response.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    private static String authToken;
    private static String authToken2;
    private static Integer failureCode;

    private static final String BAD_AUTH = "evil Auth";
    private static final String REGISTERED_USERNAME = "first_user";
    private static final String UNREGISTERED_USERNAME = "second_user";
    private static final String FIRST_GAME_NAME = "first game";
    private static final String SHARED_VALID_PASSWORD = "password";

    private enum TestType { POSITIVE, NEGATIVE }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        var serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void initialTestState() {
        try {
            serverFacade.clear(new ClearRequest());
        }
        catch (ResponseException e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            serverFacade.register
                    (new RegisterRequest(REGISTERED_USERNAME,SHARED_VALID_PASSWORD,"whatever@email.com"));
        }
        catch (ResponseException e) {
            throw new RuntimeException(e.getMessage());
        }
        failureCode = 0;
    }

    @FunctionalInterface
    public interface CheckedFunction<R, T> {
        T apply(R request) throws ResponseException;
    }

    private static <T, R> T exceptionWrapper(R request, CheckedFunction<R, T> function, TestType type) {
        try {
            return function.apply(request);
        } catch (ResponseException e) {
            if (type == TestType.POSITIVE) {
                Assertions.fail(e.getMessage());
            }
            else {
                failureCode = e.getStatusCode();
            }
            return null;
        }
    }

    @Test
    @DisplayName("Clear")
    public void clear() {
        ClearResponse response = exceptionWrapper(new ClearRequest(), serverFacade::clear, TestType.POSITIVE);
        Assertions.assertInstanceOf(ClearResponse.class, response);
    }

    @Test
    @Order(1)
    @DisplayName("Login")
    public void login() {
        //can login with registered user
        LoginRequest request = new LoginRequest(REGISTERED_USERNAME, SHARED_VALID_PASSWORD);
        LoginResponse response = exceptionWrapper
                (request, serverFacade::login, TestType.POSITIVE);
        Assertions.assertInstanceOf(LoginResponse.class, response);
    }

    @Test
    @DisplayName("Negative Login")
    public void negativeLogin() {
        LoginRequest request = new LoginRequest(UNREGISTERED_USERNAME, SHARED_VALID_PASSWORD);
        LoginResponse response = exceptionWrapper
                (request, serverFacade::login, TestType.NEGATIVE);
        Assertions.assertNull(response);
        Assertions.assertEquals(401, failureCode);
    }

    @Test
    @Order(2)
    @DisplayName("Logout")
    public void logout() {
        //can logout with registered user
        LogoutRequest request = new LogoutRequest();
        LogoutResponse response = exceptionWrapper
                (request, serverFacade::logout, TestType.POSITIVE);
        Assertions.assertInstanceOf(LogoutResponse.class, response);
        Assertions.assertNull(serverFacade.authToken);
    }

    @Test
    @DisplayName("Negative Logout")
    public void negativeLogout() {
        serverFacade.authToken = null;
        LogoutRequest request = new LogoutRequest();
        LogoutResponse response = exceptionWrapper
                (request, serverFacade::logout, TestType.NEGATIVE);
        Assertions.assertNull(response);
        Assertions.assertEquals(401, failureCode);
    }

}
