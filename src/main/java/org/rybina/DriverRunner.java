package org.rybina;


import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String password = "aqwsde322";
        String username = "postgres";
        String url = "jdbc:postgresql://localhost:5432/jdbc-course" ;
        try(Connection con = DriverManager
                .getConnection(url, username, password)) {
            System.out.println(con.getTransactionIsolation());
        }
    }
}
