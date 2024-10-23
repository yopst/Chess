package dataaccess.interfaces.memory;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUser implements UserDAO {
    private static final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        if (getUser(username) == null) {
            throw new DataAccessException("Username not found in database to remove.");
        }
        users.remove(username);
    }

    @Override
    public void updateUser(UserData userData) throws DataAccessException {
        if (getUser(userData.username()) == null) {
            throw new DataAccessException("Username from data not found in database. Create a new user instead.");
        }
        users.put(userData.username(), userData);
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}
