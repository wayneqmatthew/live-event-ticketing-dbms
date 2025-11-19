package com.dbms.models;

import javafx.beans.property.*;

public class CancellationReport {
    private final IntegerProperty eventId;
    private final StringProperty eventName;
    private final IntegerProperty ticketsRefunded;

    public CancellationReport(int eventId, String eventName, int ticketsRefunded) {
        this.eventId = new SimpleIntegerProperty(eventId);
        this.eventName = new SimpleStringProperty(eventName);
        this.ticketsRefunded = new SimpleIntegerProperty(ticketsRefunded); 
    }

    // Getters for PropertyValueFactory
    public int getEventId() { return eventId.get(); }
    public String getEventName() { return eventName.get(); }
    public int getTotalTicketsRefunded() {return ticketsRefunded.get(); }

    // Property accessors
    public IntegerProperty eventIdProperty() { return eventId; }
    public StringProperty eventNameProperty() { return eventName; }
    public IntegerProperty ticketsRefundedProperty(){ return ticketsRefunded; }
}