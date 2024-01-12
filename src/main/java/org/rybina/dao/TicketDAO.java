package org.rybina.dao;

// ДАО должны из себя представлять singletone
public class TicketDAO {
    private static final TicketDAO INSTANCE = new TicketDAO();

    private TicketDAO() {
    }

    public static TicketDAO getInstance() {
        return INSTANCE;
    }
}
