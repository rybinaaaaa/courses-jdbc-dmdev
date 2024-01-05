package org.rybina;

import util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchRunner {
    public static void main(String[] args) throws SQLException {
        Long flightId = 9L;
        String deleteFlightSql = "DELETE FROM flight WHERE id = " + flightId;
        String deleteTicketsSql = "DELETE FROM ticket WHERE flight_id = " + flightId;

        Connection connection = null;
        Statement batchStatement = null;
        try {
            connection = ConnectionManager.open();

//            ВАЖНО setAutoCommit(false), чтобы указать, что мы делаем в рамках одной транзакции
            connection.setAutoCommit(false);

            batchStatement = connection.createStatement();
            batchStatement.addBatch(deleteTicketsSql);
            batchStatement.addBatch(deleteFlightSql);

            batchStatement.executeBatch();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (batchStatement != null) batchStatement.close();
        }
    }
}
