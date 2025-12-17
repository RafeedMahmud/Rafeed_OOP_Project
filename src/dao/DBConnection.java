package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:al_rahhala.db";
    private static Connection connection;

    private DBConnection() {
    }

    public static synchronized Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            configure(connection);
        }

        return connection;
    }

    private static void configure(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // مهم جدًا: يمنع lock
            stmt.execute("PRAGMA journal_mode=WAL;");
            stmt.execute("PRAGMA synchronous=NORMAL;");
            stmt.execute("PRAGMA busy_timeout=5000;");
        }
    }
}
