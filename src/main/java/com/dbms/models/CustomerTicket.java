package com.dbms.models;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerTicket {
    private final IntegerProperty ticket_id;
    private final StringProperty event_name;
    private final ObjectProperty<LocalDate> purchase_date;
    private final FloatProperty ticket_price;
    private final StringProperty status;

    public CustomerTicket(int ticket_id, String event_name, LocalDate purchase_date, float ticket_price, String status){
        this.ticket_id = new SimpleIntegerProperty(ticket_id);
        this.event_name = new SimpleStringProperty(event_name);
        this.purchase_date = new SimpleObjectProperty<>(purchase_date);
        this.ticket_price = new SimpleFloatProperty(ticket_price);
        this.status = new SimpleStringProperty(status);
    }

    //Property getters
    public IntegerProperty ticket_idProperty(){return ticket_id;}
    public StringProperty event_nameProperty(){return event_name;}
    public ObjectProperty<LocalDate> purchase_dateProperty(){return purchase_date;}
    public FloatProperty ticket_priceProperty(){return ticket_price;}
    public StringProperty statusProperty(){return status;}

    //Standard getters
    public int getTicket_id(){return ticket_id.get();}
    public String getEvent_name(){return event_name.get();}
    public LocalDate getPurchase_date(){return purchase_date.get();}
    public float getTicket_price(){return ticket_price.get();}
    public String getStatus(){return status.get();}
}
