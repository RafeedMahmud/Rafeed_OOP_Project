package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    private static final String CHECK_USERNAME_SQL =
            "SELECT 1 FROM users WHERE username = ? LIMIT 1";

    private static final String DELETE_USER_SQL =
            "DELETE FROM users WHERE username = ?";

    private static final String UPDATE_PASSWORD_SQL =
            "UPDATE users SET password = ? WHERE username = ?";

    private static final String UPDATE_ROLE_SQL =
            "UPDATE users SET role = ? WHERE username = ?";

    private static final String FIND_BY_USERNAME_SQL =
            "SELECT username, role FROM users WHERE username = ?";

    private static final String LIST_USERS_SQL =
            "SELECT username, role FROM users ORDER BY username ASC";

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
                try (PreparedStatement ps = conn.prepareStatement(INSERT_USER_SQL)) {
                    ps.setString(1, "admin");
                    ps.setString(2, "admin");
                    ps.setString(3, "ADMIN");
                    ps.executeUpdate();
                }
            }
        }
    }

    // ===== Existing login method (keep as-is) =====
    public ResultSet findByUsernameAndPassword(String username, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(FIND_USER_SQL);
        ps.setString(1, username);
        ps.setString(2, password);
        return ps.executeQuery();
    }

    // ===== Add User =====
    public int insertUser(String username, String password, String role) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(INSERT_USER_SQL)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            return ps.executeUpdate();
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(CHECK_USERNAME_SQL)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ===== Delete User =====
    public int deleteUser(String username) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(DELETE_USER_SQL)) {
            ps.setString(1, username);
            return ps.executeUpdate();
        }
    }

    // ===== Update Password =====
    public int updatePassword(String username, String newPassword) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(UPDATE_PASSWORD_SQL)) {
            ps.setString(1, newPassword);
            ps.setString(2, username);
            return ps.executeUpdate();
        }
    }

    // ===== Update Role =====
    public int updateRole(String username, String newRole) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(UPDATE_ROLE_SQL)) {
            ps.setString(1, newRole);
            ps.setString(2, username);
            return ps.executeUpdate();
        }
    }

    // ===== Search user by username =====
    public String[] findUserByUsername(String username) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(FIND_BY_USERNAME_SQL)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString("username"), rs.getString("role")};
                }
            }
        }
        return null;
    }

    // ===== List users for table =====
    public List<String[]> listUsers() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(LIST_USERS_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new String[]{rs.getString("username"), rs.getString("role")});
            }
        }
        return rows;
    }
}
