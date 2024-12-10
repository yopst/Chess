package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUser implements UserDAO {
    private static final HashMap<String, UserData> USERS = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (userData == null ||
                userData.username() == null ||
                userData.password() == null ||
                userData.email() == null) {
            throw new DataAccessException("userData with null params supplied");
        }
        USERS.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("no username supplied");
        }
        return USERS.get(username);
    }

    @Override
    public void clear() throws DataAccessException {
        USERS.clear();
    }
}
