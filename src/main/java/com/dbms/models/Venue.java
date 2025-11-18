package com.dbms.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Venue {
    private final IntegerProperty venue_id;
    private final StringProperty name;
    private final IntegerProperty capacity;
    private final StringProperty city;
    private final StringProperty country;
    private final StringProperty region;
    private final StringProperty status;

    public Venue(int venue_id, String name, int capacity, String city, String country, String region, String status){
        this.venue_id = new SimpleIntegerProperty(venue_id);
        this.name = new SimpleStringProperty(name);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.city = new SimpleStringProperty(city);
        this.country = new SimpleStringProperty(country);
        this.region = new SimpleStringProperty(region);
        this.status = new SimpleStringProperty(status);
    }

    

    //Property getters
    public IntegerProperty venue_idProperty(){return venue_id;}
    public StringProperty nameProperty(){return name;}
    public IntegerProperty capacityProperty(){return capacity;}
    public StringProperty cityProperty(){return city;}
    public StringProperty countryProperty(){return country;}
    public StringProperty regionProperty(){return region;}
    public StringProperty statusProperty(){return status;}

    //Standard getters
    public int getVenue_id(){return venue_id.get();}
    public String getName(){return name.get();}
    public int getCapacity(){return capacity.get();}
    public String getCity(){return city.get();}
    public String getCountry(){return country.get();}
    public String getRegion(){return region.get();}
    public String getStatus(){return status.get();}
}
