package org.rybina.dao;

import org.rybina.entity.Ticket;
import org.rybina.exception.DaoException;
import org.rybina.util.ConnectionPool;

import java.sql.*;

// ДАО должны из себя представлять singletone
public class TicketDAO {
    private static final TicketDAO INSTANCE = new TicketDAO();
    private static final String DELETE_SQL = """
            delete from ticket
            where id = ?
            """;

    private static final String INSERT_SQL = """
            insert into ticket(passenger_no, passenger_name, flight_id, seat_no, cost) VALUES
            (?, ?, ?, ?, ?)
            """;

    private TicketDAO() {
    }

    public static TicketDAO getInstance() {
        return INSTANCE;
    }

    public boolean delete(Long id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)
        ) {
           preparedStatement.setLong(1, id);
           return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Ticket save(Ticket ticket) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
