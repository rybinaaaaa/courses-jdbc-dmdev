package org.rybina.dto;

public record TicketFilter(Integer limit, Integer offset, String seatNo, String passengerName) {
}
