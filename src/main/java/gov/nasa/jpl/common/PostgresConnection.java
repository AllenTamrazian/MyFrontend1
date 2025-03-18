package gov.nasa.jpl.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class PostgresConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/ViperWS";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123viperr0cks!";
    static {
        try {
            Class.forName("org.postgresql.Driver"); // Explicitly load driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}