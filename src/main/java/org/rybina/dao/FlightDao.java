package org.rybina.dao;

public class FlightDao {
    public static final FlightDao INSTANCE = new FlightDao();

    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

//      TODO: аналогично FlightDao реализуем все
}
