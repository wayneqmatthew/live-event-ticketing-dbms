package com.dbms.models;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ArtistEvent {
    private final IntegerProperty event_id;
    private final StringProperty event_name;
    private final StringProperty venue_name;
    private final StringProperty organizer_name;
    private final ObjectProperty<LocalTime> time;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty status;

    public ArtistEvent(int event_id, String event_name, String venue_name, String organizer_name, LocalTime time, LocalDate date, String status){
        this.event_id = new SimpleIntegerProperty(event_id);
        this.event_name = new SimpleStringProperty(event_name);
        this.venue_name = new SimpleStringProperty(venue_name);
        this.organizer_name = new SimpleStringProperty(organizer_name);
        this.time = new SimpleObjectProperty<>(time);
        this.date = new SimpleObjectProperty<>(date);
        this.status = new SimpleStringProperty(status);
    }

    //Property getters
    public IntegerProperty event_idProperty(){return event_id;}
    public StringProperty event_nameProperty(){return event_name;}
    public StringProperty venue_nameProperty(){return venue_name;}
    public StringProperty organizer_nameProperty(){return organizer_name;}
    public ObjectProperty<LocalTime> timeProperty(){return time;}
    public ObjectProperty<LocalDate> dateProperty(){return date;}
    public StringProperty statusProperty(){return status;}

    //Standard getters
    public int getEvent_id(){return event_id.get();}
    public String getEvent_name(){return event_name.get();}
    public String getVenue_name(){return venue_name.get();}
    public String getOrganizer_name(){return organizer_name.get();}
    public LocalTime getTime(){return time.get();}
    public LocalDate getDate(){return date.get();}
    public String getStatus(){return status.get();}
}
