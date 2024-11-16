package dataaccess;

import dataaccess.interfaces.*;
import dataaccess.memory.*;
import dataaccess.mysql.*;

public class MyDatabaseManager extends DatabaseManager{
    private static MyDatabaseManager instance;
    private final GameDAO games;
    private final AuthDAO auth;
    private final UserDAO users;

    private MyDatabaseManager() {
        games = new MemoryGame();
        auth = new MemoryAuth();
        users = new MemoryUser();
    }

    private MyDatabaseManager(boolean useMySql) {
        if (useMySql) {
            games = new MySqlGame();
            auth = new MySqlAuth();
            users = new MySqlUser();
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