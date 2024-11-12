package dataaccess;

import dataaccess.interfaces.*;
import dataaccess.memory.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final GameDAO games;
    private final AuthDAO auth;
    private final UserDAO users;

    private DatabaseManager() {
        games = new MemoryGame();
        auth = new MemoryAuth();
        users = new MemoryUser();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
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