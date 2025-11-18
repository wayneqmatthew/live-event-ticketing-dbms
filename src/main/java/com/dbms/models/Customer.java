package com.dbms.models;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private final IntegerProperty customer_id;
    private final StringProperty last_name;
    private final StringProperty first_name;
    private final StringProperty email;
    private final StringProperty phone_number;
    private final ObjectProperty<LocalDate> registration_date;
    private final StringProperty status;

    public Customer(int customer_id, String last_name, String first_name, String email, String phone_number, LocalDate registration_date, String status){
        this.customer_id = new SimpleIntegerProperty(customer_id);
        this.last_name = new SimpleStringProperty(last_name);
        this.first_name = new SimpleStringProperty(first_name);
        this.email = new SimpleStringProperty(email);
        this.phone_number = new SimpleStringProperty(phone_number);
        this.registration_date = new SimpleObjectProperty<>(registration_date);
        this.status = new SimpleStringProperty(status);
    }

    //Property getters
    public IntegerProperty customer_idProperty(){return customer_id;}
    public StringProperty last_nameProperty(){return last_name;}
    public StringProperty first_nameProperty(){return first_name;}
    public StringProperty emailProperty(){return email;}
    public StringProperty phone_numberProperty(){return phone_number;}
    public ObjectProperty<LocalDate> registration_dateProperty(){return registration_date;}
    public StringProperty statusProperty(){return status;}

    //Standard getters
    public int getCustomer_id(){return customer_id.get();}
    public String getLast_name(){return last_name.get();}
    public String getFirst_name(){return first_name.get();}
    public String getEmail(){return email.get();}
    public String getPhone_number(){return phone_number.get();}
    public LocalDate getRegistration_date(){return registration_date.get();}
    public String getStatus(){return status.get();}
}
