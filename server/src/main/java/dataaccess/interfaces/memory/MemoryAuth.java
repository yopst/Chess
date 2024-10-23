package dataaccess.interfaces.memory;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryAuth implements AuthDAO {
    private static final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void createAuth(String username) throws DataAccessException {
        AuthData authData = AuthData.createWithRandomToken(username);
        auths.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (getAuth(authToken) == null) throw new DataAccessException("no such authData to remove");
        auths.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
