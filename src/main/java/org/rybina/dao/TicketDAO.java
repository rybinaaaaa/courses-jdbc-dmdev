package org.rybina.dao;

import org.rybina.dto.TicketFilter;
import org.rybina.entity.Ticket;
import org.rybina.exception.DaoException;
import org.rybina.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

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

    private static final String UPDATE_SQL = """
            update ticket set passenger_no = ?, passenger_name = ?, flight_id = ?, seat_no = ?, cost = ?
            where id = ?
            """;

    private static final String FIND_BY_ID_SQL = """
            select id, passenger_no, passenger_name, flight_id, seat_no, cost from ticket where id = ?
            """;

    private static final String FIND_ALL_SQL = """
            select id, passenger_no, passenger_name, flight_id, seat_no, cost from ticket
            """;

    private static final String FIND_ALL_WITH_PAG_SQL = """
            select id, passenger_no, passenger_name, flight_id, seat_no, cost from ticket
            limit ? offset ?
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

    public Ticket update(Ticket ticket) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.setLong(6, ticket.getId());

            return ticket;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Ticket ticket = null;

            if (resultSet.next()) {
                ticket = buildTicket(resultSet);
            }
            return Optional.of(ticket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ticket> findAll() {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();

            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Ticket buildTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                resultSet.getLong("flight_id"),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }

    public List<Ticket> findAll(TicketFilter ticketFilter) {
        List<Object> parameters = new ArrayList<>();

        List<String> whereSql = new ArrayList<>();
        if (ticketFilter.seatNo() != null) {
            whereSql.add("seat_no like ?");
            parameters.add("%" + ticketFilter.seatNo() + "%");
        }
        if (ticketFilter.passengerName() != null) {
            whereSql.add("passenger_name like ?");
            parameters.add(ticketFilter.passengerName());
        }

        parameters.add(ticketFilter.limit());
        parameters.add(ticketFilter.offset());

        String where;
        if (whereSql.isEmpty()) {
            where = whereSql.stream().collect(joining(" AND ", "", " LIMIT ? OFFSET ?"));
        } else {
            where = whereSql.stream().collect(joining(" AND ", " WHERE ", " LIMIT ? OFFSET ?"));
        }
        String sql = FIND_ALL_SQL + where ;

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();

            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
             throw new RuntimeException(e);
        }
    }
}
