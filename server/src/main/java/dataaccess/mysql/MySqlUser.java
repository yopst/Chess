package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import model.UserData;

public class MySqlUser implements UserDAO {
    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
