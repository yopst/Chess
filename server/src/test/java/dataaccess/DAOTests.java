package dataaccess;

import dataaccess.interfaces.*;
import model.*;
import org.junit.jupiter.api.*;

public class DAOTests {
    static AuthDAO auths;
    static GameDAO games;
    static UserDAO users;

    private static String validAuth;
    private static final String VALID_USERNAME = "valid_user";
    private static final String VALID_PASSWORD = "valid_password";


    @FunctionalInterface
    public interface DAOFunction<R, T> {
        T apply(R request) throws DataAccessException;
    }

    private static <T, R> T exceptionWrapper(R input, DAOFunction<R, T> function) {
        try {
            return function.apply(input);
        } catch (DataAccessException e) {
            Assertions.fail("DAO operation failed with exception: " + e.getMessage());
            return null;
        }
    }

    @BeforeAll
    public static void getDB() {
        MyDatabaseManager db = MyDatabaseManager.getInstance();
        auths = db.getAuth();
        games = db.getGames();
        users = db.getUsers();
    }

    @BeforeEach
    public void init() {
        try {
            validAuth = auths.createAuth(VALID_USERNAME).authToken();
            users.createUser(new UserData(VALID_USERNAME,VALID_PASSWORD,""));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //positive clears
    @Test
    @DisplayName("Clear Auth")
    public void clearAuth(){
        Assertions.assertDoesNotThrow(() -> auths.clear());
    }

    @Test
    @DisplayName("Clear User")
    public void clearUser(){
        Assertions.assertDoesNotThrow(() -> users.clear());
    }

    @Test
    @DisplayName("Clear Game")
    public void clearGame(){
        Assertions.assertDoesNotThrow(() -> games.clear());
    }

    //AuthDAO
    @Test
    @DisplayName("Delete Auth")
    public void deleteAuth(){
        Assertions.assertDoesNotThrow(() -> auths.deleteAuth(validAuth));
    }

    @Test
    @DisplayName("Negative Delete Auth")
    public void negativeDeleteAuth(){
        Assertions.assertThrows(DataAccessException.class, () -> auths.deleteAuth("invalid_auth"));
        Assertions.assertThrows(DataAccessException.class, () -> auths.deleteAuth(""));
    }

    @Test
    @DisplayName("Create Auth")
    public void createAuth(){
        AuthData authData = exceptionWrapper(VALID_USERNAME, auths::createAuth);
        AuthData authData1 = exceptionWrapper(VALID_USERNAME, auths::createAuth);
        Assertions.assertNotNull(authData);
        Assertions.assertNotNull(authData1);
        Assertions.assertEquals(authData1.username(), authData.username());
        Assertions.assertNotEquals(authData1.authToken(), authData.authToken());
    }

    @Test
    @DisplayName("Negative Create Auth")
    public void negativeCreateAuth(){
        Assertions.assertThrows(DataAccessException.class, () -> auths.createAuth(null));
    }

    @Test
    @DisplayName("Get Auth")
    public void getAuth(){
        AuthData authData = exceptionWrapper(validAuth, auths::getAuth);
        Assertions.assertNotNull(authData);
    }

    @Test
    @DisplayName("Negative Get Auth")
    public void negativeGetAuth(){
        Assertions.assertThrows(DataAccessException.class, () -> auths.getAuth(null));

        AuthData authData = exceptionWrapper("invalidAuth", auths::getAuth);
        Assertions.assertNull(authData);
    }

    //UserDAO
    @Test
    @DisplayName("Get User")
    public void getUser() {
        UserData userData = exceptionWrapper(VALID_USERNAME, users::getUser);
        Assertions.assertNotNull(userData);
        Assertions.assertEquals(VALID_USERNAME, userData.username());
    }

    @Test
    @DisplayName("Negative Get User")
    public void negativeGetUser(){
        Assertions.assertThrows(DataAccessException.class, () -> users.getUser(null));

        UserData userData = exceptionWrapper("new_user", users::getUser);
        Assertions.assertNull(userData);
    }

    @Test
    @DisplayName("Create User")
    public void createUser() {
        UserData newUser = new UserData("valid", "valid", "valid");
        Assertions.assertDoesNotThrow(() -> users.createUser(newUser));
    }

    @Test
    @DisplayName("Negative Create User")
    public void negativeCreateUser(){
        Assertions.assertThrows(DataAccessException.class, () -> users.createUser(null));

        UserData invalidUser = new UserData(null, VALID_PASSWORD, "");
        Assertions.assertThrows(DataAccessException.class, () -> users.createUser(invalidUser));

        UserData invalidUser1 = new UserData(VALID_USERNAME, null, "");
        Assertions.assertThrows(DataAccessException.class, () -> users.createUser(invalidUser1));
    }






}