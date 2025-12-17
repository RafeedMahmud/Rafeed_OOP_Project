package service;

import dao.UserDAO;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private boolean isValidRole(String role) {
        if (role == null) return false;
        String r = role.trim().toUpperCase();
        return r.equals("ADMIN") || r.equals("EMPLOYEE");
    }

    public boolean addUser(String username, String password, String role) throws SQLException {
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;
        if (!isValidRole(role)) return false;

        String u = username.trim();
        String p = password.trim();
        String r = role.trim().toUpperCase();

        if (userDAO.usernameExists(u)) return false;

        int rows = userDAO.insertUser(u, p, r);
        return rows > 0;
    }

    public boolean deleteUser(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) return false;
        String u = username.trim();

        // حماية بسيطة: ما نحذفوش admin الافتراضي
        if (u.equalsIgnoreCase("admin")) return false;

        int rows = userDAO.deleteUser(u);
        return rows > 0;
    }

    public boolean changePassword(String username, String newPassword) throws SQLException {
        if (username == null || username.trim().isEmpty()) return false;
        if (newPassword == null || newPassword.trim().isEmpty()) return false;

        String u = username.trim();
        String p = newPassword.trim();

        int rows = userDAO.updatePassword(u, p);
        return rows > 0;
    }

    public boolean changeRole(String username, String newRole) throws SQLException {
        if (username == null || username.trim().isEmpty()) return false;
        if (!isValidRole(newRole)) return false;

        String u = username.trim();
        String r = newRole.trim().toUpperCase();

        // حماية بسيطة: ما نبدلوش role متاع admin الافتراضي
        if (u.equalsIgnoreCase("admin") && !r.equals("ADMIN")) return false;

        int rows = userDAO.updateRole(u, r);
        return rows > 0;
    }

    public String[] findUser(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) return null;
        return userDAO.findUserByUsername(username.trim());
    }

    public List<String[]> listUsers() throws SQLException {
        return userDAO.listUsers();
    }
}
