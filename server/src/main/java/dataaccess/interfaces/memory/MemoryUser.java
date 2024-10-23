package dataaccess.interfaces.memory;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import model.UserData;

public class MemoryUser implements UserDAO {
    MemoryDatabase db;

    public MemoryUser(MemoryDatabase mdb) {
        db = mdb;
    }
    @Override
    public void createUser(UserData userData) throws DataAccessException {
        db.user.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return db.user.get(username);
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        db.user.remove(username);
    }

    @Override
    public void updateUser(UserData userData) throws DataAccessException {
        db.user.put(userData.username(), userData);
    }

    @Override
    public void clear() throws DataAccessException {
        db.user.clear();
    }
}
