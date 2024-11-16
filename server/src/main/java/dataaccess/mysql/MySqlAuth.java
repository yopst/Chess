package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;

public class MySqlAuth implements AuthDAO {
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
