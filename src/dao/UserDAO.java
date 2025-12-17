package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL" +
            ");";

    private static final String INSERT_USER_SQL =
            "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

    private static final String FIND_USER_SQL =
            "SELECT * FROM users WHERE username = ? AND password = ?";

    public UserDAO() {
        try {
            ensureTableExists();
            ensureDefaultAdmin();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureTableExists() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        }
    }

    private void ensureDefaultAdmin() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ps =
                             conn.prepareStatement(INSERT_USER_SQL)) {
                    ps.setString(1, "admin");
                    ps.setString(2, "admin");
                    ps.setString(3, "ADMIN");
                    ps.executeUpdate();
                }
            }
        }
    }

    public ResultSet findByUsernameAndPassword(String username, String password)
            throws SQLException {

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(FIND_USER_SQL);
        ps.setString(1, username);
        ps.setString(2, password);

        return ps.executeQuery();
    }
}
