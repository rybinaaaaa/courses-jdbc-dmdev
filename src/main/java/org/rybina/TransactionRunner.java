package org.rybina;

import org.rybina.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        String deleteFlightSql = "DELETE FROM flight WHERE id = ?";
        String deleteTicketsSql = "DELETE FROM ticket WHERE flight_id = ?";
        Long flightId = 9L;

        Connection connection = null;
        PreparedStatement deleteFlightStatement = null;
        PreparedStatement deleteTicketStatement = null;
        try {
            connection = ConnectionPool.get();

//            ВАЖНО setAutoCommit(false), чтобы указать, что мы делаем в рамках одной транзакции
            connection.setAutoCommit(false);

            deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
            deleteTicketStatement = connection.prepareStatement(deleteTicketsSql);
            deleteTicketStatement.setLong(1, flightId);
            deleteFlightStatement.setLong(1, flightId);

            deleteTicketStatement.executeUpdate();

            if (true) {
            throw new RuntimeException("end");
            }

            deleteFlightStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (deleteFlightStatement != null) deleteFlightStatement.close();
            if (deleteTicketStatement != null) deleteTicketStatement.close();
        }
    }
}
