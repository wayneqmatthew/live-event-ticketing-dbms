package com.dbms.controllers;

public class EventDTO {
    private int event_id;
    private String event_name;
    private String artist_name;

    public EventDTO(int event_id, String event_name, String artist_name){
        this.event_id = event_id;
        this.event_name = event_name;
        this.artist_name = artist_name;
    }

    public int getEvent_id(){return event_id;}
    public String getEvent_name(){return event_name;}
    public String getArtist_name(){return artist_name;}
}
