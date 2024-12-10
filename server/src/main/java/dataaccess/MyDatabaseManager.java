package dataaccess;

import dataaccess.interfaces.*;
import dataaccess.memory.*;

public class MyDatabaseManager extends DatabaseManager {
    private static MyDatabaseManager instance;
    private final GameDAO games;
    private final AuthDAO auth;
    private final UserDAO users;

    private MyDatabaseManager(boolean useMySql) {
        if (useMySql) {

            try {
                games = new MySqlGame();
                auth = new MySqlAuth();
                users = new MySqlUser();
                createDatabase();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            games = new MemoryGame();
            auth = new MemoryAuth();
            users = new MemoryUser();
        }
    }

    public static MyDatabaseManager getInstance() {
        if (instance == null) {
            instance = new MyDatabaseManager(false); //Change Database type with different Constructor
        }
        return instance;
    }

    public GameDAO getGames() {
        return games;
    }

    public AuthDAO getAuth() {
        return auth;
    }

    public UserDAO getUsers() {
        return users;
    }
}