package dataaccess;

import dataaccess.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DAOTests {
    static AuthDAO auths;
    static GameDAO games;
    static UserDAO users;

    @BeforeAll
    public static void getDB() {
        MyDatabaseManager db = MyDatabaseManager.getInstance();
        auths = db.getAuth();
        games = db.getGames();
        users = db.getUsers();
    }

    @Test
    @DisplayName("Clear Tests") {
        try {
            Assertions.assertDoesNotThrow(DataAccessException.class, auths.clear());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
