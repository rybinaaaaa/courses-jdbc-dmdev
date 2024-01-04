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

        try (Connection con = ConnectionManager.open();
        ) {
            System.out.println(con.getTransactionIsolation());

            /**
             * setFetchSize(int rows): Этот метод устанавливает количество строк, которые должны быть извлечены из базы данных при выполнении запроса. Значение 20 означает, что при каждом обращении к базе данных для извлечения данных, JDBC пытается получить 20 строк за один раз. Это может улучшить производительность за счет снижения количества обращений к базе данных, особенно при обработке больших объемов данных. Но важно выбрать размер, который не будет слишком нагружать память.
             * setQueryTimeout(int seconds): Этот метод устанавливает максимальное время ожидания для выполнения запроса. Значение 10 означает, что если запрос не завершится в течение 10 секунд, то будет выброшено исключение SQLException. Это полезно для предотвращения зависания программы из-за долго выполняющихся запросов.
             * setMaxRows(int max): Устанавливает ограничение на максимальное количество строк, которые могут быть обработаны в результате запроса. Значение 100 означает, что запрос вернет не более 100 строк, даже если в базе данных удовлетворяют условиям запроса больше строк. Это может быть полезно для ограничения объема данных, получаемых из базы данных, особенно при работе с очень большими наборами данных.
             */
            PreparedStatement statement = con.prepareStatement(sql1);
            statement.setFetchSize(20);
            statement.setQueryTimeout(10);
            statement.setMaxRows(100);

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
