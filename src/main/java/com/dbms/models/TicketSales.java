package com.dbms.models;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TicketSales {
    private final IntegerProperty event_id;
    private final StringProperty event_name;
    private final IntegerProperty tickets_sold;
    private final FloatProperty total_revenue;

    public TicketSales(int event_id, String event_name, int tickets_sold, float total_revenue){
        this.event_id = new SimpleIntegerProperty(event_id);
        this.event_name = new SimpleStringProperty(event_name);
        this.tickets_sold = new SimpleIntegerProperty(tickets_sold);
        this.total_revenue = new SimpleFloatProperty(total_revenue);
    }
    //Property getters
    public IntegerProperty event_idProperty(){return event_id;}
    public StringProperty event_nameProperty(){return event_name;}
    public IntegerProperty tickets_soldProperty(){return tickets_sold;}
    public FloatProperty total_revenueProperty(){return total_revenue;}

    //Standard getters
    public int getEvent_id(){return event_id.get();}
    public String getEvent_name(){return event_name.get();}
    public int getTickets_sold(){return tickets_sold.get();}
    public float getTotal_revenue(){return total_revenue.get();}
}
