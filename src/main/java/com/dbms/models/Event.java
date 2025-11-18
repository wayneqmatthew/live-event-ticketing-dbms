package main.java.com.dbms.models;

import java.time.LocalTime;
import java.time.LocalDate;

public class Event {
    private int event_id;
    private int venue_id;
    private int artist_id;
    private String event_name;
    private LocalTime time;
    private LocalDate date;
    private int capacity;
    private String status;
    private String category;
    private float price;
}
