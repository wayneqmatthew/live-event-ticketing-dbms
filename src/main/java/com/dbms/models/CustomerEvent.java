package com.dbms.models;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerEvent {
    private final IntegerProperty event_id;
    private final StringProperty event_name;
    private final ObjectProperty<LocalTime> time;
    private final ObjectProperty<LocalDate> date;
    private final IntegerProperty available_capacity;
    private final FloatProperty ticket_price;

    public CustomerEvent(int event_id, String event_name, LocalTime time, LocalDate date, int available_capacity, Float ticket_price){
        this.event_id = new SimpleIntegerProperty(event_id);
        this.event_name = new SimpleStringProperty(event_name);
        this.time = new SimpleObjectProperty<>(time);
        this.date = new SimpleObjectProperty<>(date);
        this.available_capacity = new SimpleIntegerProperty(available_capacity);
        this.ticket_price = new SimpleFloatProperty(ticket_price);
    }

    //Property getters
    public IntegerProperty event_idProperty(){return event_id;}
    public StringProperty event_nameProperty(){return event_name;}
    public ObjectProperty<LocalTime> timeProperty(){return time;}
    public ObjectProperty<LocalDate> dateProperty(){return date;}
    public IntegerProperty available_capacityProperty(){return available_capacity;}
    public FloatProperty ticket_priceProperty(){return ticket_price;}

    //Standard getters
    public int getEvent_id(){return event_id.get();}
    public String getEvent_name(){return event_name.get();}
    public LocalTime getTime(){return time.get();}
    public LocalDate getDate(){return date.get();}
    public int getAvailable_capacity(){return available_capacity.get();}
    public float getTicket_price(){return ticket_price.get();}
}
