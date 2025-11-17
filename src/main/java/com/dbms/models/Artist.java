package main.java.com.dbms.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Artist {
    private final IntegerProperty artist_id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty genre;
    private final StringProperty management_company;
    private final StringProperty status;

    public Artist(int artist_id, String name, String email, String genre, String management_company, String status){
        this.artist_id = new SimpleIntegerProperty(artist_id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.genre = new SimpleStringProperty(genre);
        this.management_company = new SimpleStringProperty(management_company);
        this.status = new SimpleStringProperty(status);
    }

    //Property getters
    public IntegerProperty artist_idProperty(){return artist_id;}
    public StringProperty nameProperty(){return name;}
    public StringProperty emailProperty(){return email;}
    public StringProperty genreProperty(){return genre;}
    public StringProperty management_companyProperty(){return management_company;}
    public StringProperty statusProperty(){return status;}

    //Standard getters
    public int getArtist_id(){return artist_id.get();}
    public String getName(){return name.get();}
    public String getEmail(){return email.get();}
    public String getGenre(){return genre.get();}
    public String getManagement_Company(){return management_company.get();}
    public String getStatus(){return status.get();}
}

