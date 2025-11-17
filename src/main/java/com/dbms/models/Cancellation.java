package main.java.com.dbms.models;

import java.time.LocalDate;

public class Cancellation {
    private int cancellation_ref_id;
    private int ticket_id;
    private float refund_amount;
    private String seat_number;
    private LocalDate cancellation_date;
    private String status;
}
