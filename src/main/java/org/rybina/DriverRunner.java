package org.rybina;


import org.postgresql.Driver;
import util.ConnectionManager;

import java.sql.*;

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

        //language=PostgreSQL
        String sql3 = """
                select * from aircraft  where id = ?
                """;

        try(Connection con = ConnectionManager.open();
        ) {
            System.out.println(con.getTransactionIsolation());

            PreparedStatement statement = con.prepareStatement(sql1);

            Boolean result1 = statement.execute();
            System.out.println(result1);

            statement = con.prepareStatement(sql2);
            Integer result2 = statement.executeUpdate();

            statement = con.prepareStatement(sql3);
            statement.setLong(1, 1);
            ResultSet result3 = statement.executeQuery();
            while (result3.next()) {
                System.out.println(result3.getString("model"));
            }
        }
    }
}
