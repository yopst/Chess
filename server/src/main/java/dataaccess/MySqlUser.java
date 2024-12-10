package dataaccess;

import dataaccess.interfaces.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class MySqlUser implements UserDAO {

    public MySqlUser() throws DataAccessException{
        createUserTable();
    }

    private void createUserTable() throws DataAccessException {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "username VARCHAR(255) PRIMARY KEY, "
                + "password VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255) NOT NULL)";
        executeUpdate(sql);
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (userData == null ||
                userData.username() == null ||
                userData.password() == null ||
                userData.email() == null) {
            throw new DataAccessException("userData with null params supplied");
        }
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(sql, userData.username(), hashedPassword, userData.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (username == null) throw new DataAccessException("no username supplied");

        String sql = "SELECT username, password, email FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String user = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(user, password, email);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        executeUpdate(sql);
    }

    private void executeUpdate(String sql, String... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the PreparedStatement if any
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error executing update: " + e.getMessage());
        }
    }
}
