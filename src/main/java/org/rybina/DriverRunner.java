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
        String sql1 = """
                SELECT * FROM AIRCRAFT
                """;

        //language=PostgreSQL
        String sql2 = """
                insert into aircraft (model)
                values ('TEST 001')
                """;

        try(Connection con = ConnectionManager.open();
            Statement statement = con.createStatement()
        ) {
            System.out.println(con.getTransactionIsolation());

//            method execute преймущественно используется для ddl операций, но вообще его можно использовать повсеместно

            Boolean result1 = statement.execute(sql1);
            System.out.println(result1);

//            method executeUpdate используется для dml запросов, !КОТОРЫЕ НИЧЕГО НЕ ВОЗВРАЩАЮТ!
            var result = statement.executeUpdate(sql2);
        }
    }
}
