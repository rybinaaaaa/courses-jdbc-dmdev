package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    public static String PASSWORD = "aqwsde322";
    public static String USERNAME = "postgres";
    public static String URL = "jdbc:postgresql://localhost:5432/jdbc-course";

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager
                    .getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
