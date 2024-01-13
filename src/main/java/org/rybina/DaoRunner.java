package org.rybina;

import org.rybina.dao.TicketDAO;
import org.rybina.entity.Ticket;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        findAllTest();
    }

    private static void findAllTest() {
        TicketDAO ticketDAO = TicketDAO.getInstance();
        ticketDAO.findAll().forEach(System.out::println);
    }

    private static void updateTest() {
        TicketDAO ticketDAO = TicketDAO.getInstance();
        Optional<Ticket> maybeTicket = ticketDAO.findById(2L);
        System.out.println(maybeTicket);
        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.99));
            ticketDAO.update(ticket);
            System.out.println(ticket);
        });
    }

    private static void deleteTest() {
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
