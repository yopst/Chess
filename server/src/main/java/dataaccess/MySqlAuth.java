package dataaccess;

import dataaccess.interfaces.AuthDAO;
import model.AuthData;
import java.sql.*;

public class MySqlAuth implements AuthDAO {
    DatabaseManager dbManager;

    public MySqlAuth(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("no authToken supplied");
        }

        AuthData authData = AuthData.createWithRandomToken(username);
        String sql = "INSERT INTO auth_tokens (username, token) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, authData.authToken());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return authData;
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
