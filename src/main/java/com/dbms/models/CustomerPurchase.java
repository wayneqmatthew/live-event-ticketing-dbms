package com.dbms.models;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerPurchase {
    private final IntegerProperty customer_id;
    private final StringProperty name;
    private final IntegerProperty tickets_purchased;
    private final FloatProperty total_spent;

    public CustomerPurchase(int customer_id, String name, int tickets_purchased, float total_spent) {
        this.customer_id = new SimpleIntegerProperty(customer_id);
        this.name = new SimpleStringProperty(name);
        this.tickets_purchased = new SimpleIntegerProperty(tickets_purchased);
        this.total_spent = new SimpleFloatProperty(total_spent);
    }

    // Property getters for TableView binding
    public IntegerProperty customer_idProperty() { return customer_id; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty tickets_purchasedProperty() { return tickets_purchased; }
    public FloatProperty total_spentProperty() { return total_spent; }

    // Standard getters
    public int getCustomer_id() { return customer_id.get(); }
    public String getName() { return name.get(); }
    public int getTickets_purchased() { return tickets_purchased.get(); }
    public float getTotal_spent() { return total_spent.get(); }
}