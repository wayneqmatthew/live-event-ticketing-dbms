package main.java.com.dbms.models;

import java.time.LocalDate;

public class Ticket {
    private int ticket_id;
    private int event_id;
    private int customer_id;
    private String seat_number;
    private LocalDate purchase_date;
    private float price;
    private String status;
}
