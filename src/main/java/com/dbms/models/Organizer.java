package com.dbms.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Organizer {
    private final IntegerProperty organizer_id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty city;
    private final StringProperty country;
    private final StringProperty region;
    private final StringProperty status;

    public Organizer(int organizer_id, String name, String email, String city, String country, String region, String status){
        this.organizer_id = new SimpleIntegerProperty(organizer_id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.city = new SimpleStringProperty(city);
        this.country = new SimpleStringProperty(country);
        this.region = new SimpleStringProperty(region);
        this.status = new SimpleStringProperty(status);
    }

    //Property getters
    public IntegerProperty organizer_idProperty(){return organizer_id;}
    public StringProperty nameProperty(){return name;}
    public StringProperty emailProperty(){return email;}
    public StringProperty cityProperty(){return city;}
    public StringProperty countryProperty(){return country;}
    public StringProperty regionProperty(){return region;}
    public StringProperty statusProperty(){return status;}

    //Standard getters
    public int getOrganizer_id(){return organizer_id.get();}
    public String getName(){return name.get();}
    public String getEmail(){return email.get();}
    public String getCity(){return city.get();}
    public String getCountry(){return country.get();}
    public String getRegion(){return region.get();}
    public String getStatus(){return status.get();}
}
