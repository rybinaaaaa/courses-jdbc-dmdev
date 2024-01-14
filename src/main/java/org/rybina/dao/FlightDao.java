package org.rybina.dao;

import org.rybina.entity.Flight;
import org.rybina.util.ConnectionPool;

import java.sql.*;

public class FlightDao {
    public static final FlightDao INSTANCE = new FlightDao();

    private static final String FIND_ALL_SQL = """
                        select id, flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status from flight
            """;


    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where flight.id = ?
            """;


    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    public Flight getById(Long id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            Flight flight = null;
            if (resultSet.next()) {
                flight = buildFlight(resultSet);
            }

            return flight;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Flight getById(Long id, Connection connection) {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            Flight flight = null;
            if (resultSet.next()) {
                flight = buildFlight(resultSet);
            }

            return flight;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Flight buildFlight(ResultSet resultSet) throws SQLException {
        return new Flight(
                resultSet.getLong("flight_id"),
                resultSet.getString("flight_no"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("arrival_airport_code"),
                resultSet.getInt("aircraft_id"),
                resultSet.getString("status")
        );
    }
}
