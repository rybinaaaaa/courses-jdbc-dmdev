package org.rybina;

import org.rybina.dao.TicketDAO;
import org.rybina.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {
    public static void main(String[] args) {
        TicketDAO ticketDAO = TicketDAO.getInstance();

        boolean res = ticketDAO.delete(57L);
        System.out.println(res);
    }

    private static void saveTest() {
        TicketDAO ticketDAO = TicketDAO.getInstance();

        Ticket ticket = new Ticket();
        ticket.setPassengerNo("123");
        ticket.setPassengerName("Test");
        ticket.setFlightId(3L);
        ticket.setSeatNo("B3");
        ticket.setCost(BigDecimal.TEN);

        Ticket savedTicket = ticketDAO.save(ticket);
        System.out.println(savedTicket);
    }
}
