package org.rybina;


import org.postgresql.Driver;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        try(Connection con = ConnectionManager.open()) {
            System.out.println(con.getTransactionIsolation());
        }
    }
}
