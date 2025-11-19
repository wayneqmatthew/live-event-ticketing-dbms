package com.dbms.models;

import java.time.LocalTime;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Event {
    private final IntegerProperty event_id;
    private final IntegerProperty venue_id;
    private final IntegerProperty artist_id;
    private final IntegerProperty organizer_id;
    private final StringProperty event_name;
    private final ObjectProperty<LocalTime> time;
    private final ObjectProperty<LocalDate> date;
    private final IntegerProperty capacity;
    private final FloatProperty ticket_price;
    private final StringProperty status;

    public Event(int event_id, int venue_id, int artist_id, int organizer_id, String event_name, LocalTime time, LocalDate date, int capacity, float ticket_price, String status){
        this.event_id = new SimpleIntegerProperty(event_id);
        this.venue_id = new SimpleIntegerProperty(venue_id);
        this.artist_id = new SimpleIntegerProperty(artist_id);
        this.organizer_id = new SimpleIntegerProperty(organizer_id);
        this.event_name = new SimpleStringProperty(event_name);
        this.time = new SimpleObjectProperty<>(time);
        this.date = new SimpleObjectProperty<>(date);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.ticket_price = new SimpleFloatProperty(ticket_price);
        this.status = new SimpleStringProperty(status);
    }

    //Property getters
    public IntegerProperty event_idProperty(){return event_id;}
    public IntegerProperty venue_idProperty(){return venue_id;}
    public IntegerProperty artist_idProperty(){return artist_id;}
    public IntegerProperty organizer_idProperty(){return organizer_id;}
    public StringProperty event_nameProperty(){return event_name;}
    public ObjectProperty<LocalTime> timeProperty(){return time;}
    public ObjectProperty<LocalDate> dateProperty(){return date;}
    public IntegerProperty capacityProperty(){return capacity;}
    public FloatProperty ticket_priceProperty(){return ticket_price;}
    public StringProperty statusProperty(){return status;}

    //Standard getters
    public int getEvent_id(){return event_id.get();}
    public int getVenue_id(){return venue_id.get();}
    public int getArtist_id(){return artist_id.get();}
    public int getOrganizer_id(){return organizer_id.get();}
    public String getEvent_name(){return event_name.get();}
    public LocalTime getTime(){return time.get();}
    public LocalDate getDate(){return date.get();}
    public int getCapacity(){return capacity.get();}
    public float getTicket_price(){return ticket_price.get();}
    public String getStatus(){return status.get();}
}

