package service;

import dao.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {

    private final UserDAO userDAO;

    public LoginService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public LoggedUser login(String username, String password) throws SQLException {
        ResultSet rs = userDAO.findByUsernameAndPassword(username, password);

        if (rs.next()) {
            int id = rs.getInt("id");
            String role = rs.getString("role");
            return new LoggedUser(id, username, role);
        }

        return null;
    }
}
