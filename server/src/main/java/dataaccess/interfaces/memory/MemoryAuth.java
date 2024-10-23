package dataaccess.interfaces.memory;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;

public class MemoryAuth implements AuthDAO {
    MemoryDatabase db;

    public MemoryAuth(MemoryDatabase mdb) {
        db = mdb;
    }

    @Override
    public void createAuth(String username) throws DataAccessException {
        AuthData authData = AuthData.createWithRandomToken(username);
        db.auth.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return db.auth.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        db.auth.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        db.auth.clear();
    }
}
