package org.rybina;


import org.postgresql.Driver;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DriverRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;

        //language=PostgreSQL
        String sql = """
                SELECT * FROM AIRCRAFT
                """;

        try(Connection con = ConnectionManager.open();
            Statement statement = con.createStatement()
        ) {
            System.out.println(con.getTransactionIsolation());

//            method execute используется для ddl операций

            Boolean result = statement.execute(sql);
            System.out.println(result);
        }
    }
}
