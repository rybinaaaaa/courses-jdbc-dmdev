package org.rybina;

import org.rybina.dao.TicketDAO;
import org.rybina.dto.TicketFilter;
import org.rybina.entity.Ticket;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        findAllWithParametersTest();
    }

    private static void findAllWithParametersTest() {
        TicketDAO ticketDAO = TicketDAO.getInstance();
        ticketDAO.findAll(new TicketFilter(2, 1, null, null)).forEach(System.out::println);
        System.out.println("----");
        ticketDAO.findAll(new TicketFilter(20, 0, "A1", null)).forEach(System.out::println);
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
        ticket.setFlight(3L);
        ticket.setSeatNo("B3");
        ticket.setCost(BigDecimal.TEN);

        Ticket savedTicket = ticketDAO.save(ticket);
        System.out.println(savedTicket);
    }
}
