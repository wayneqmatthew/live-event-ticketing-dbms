package main.java.com.dbms.models;

import java.time.LocalDate;

public class CommissionPayout {
    private int payout_ref_id;
    private int payee_id;
    private float commission_amount;
    private float total_ticket_sales;
    private float commission_percentage;
    private LocalDate payout_date;
}
