package com.dbms.models;

import javafx.beans.property.*;

public class CancellationReport {
    private final IntegerProperty eventId;
    private final StringProperty eventName;
    private final DoubleProperty totalRefunded; // Only the SUM remains

    public CancellationReport(int eventId, String eventName, double total) {
        this.eventId = new SimpleIntegerProperty(eventId);
        this.eventName = new SimpleStringProperty(eventName);
        this.totalRefunded = new SimpleDoubleProperty(total);
    }

    // Getters for PropertyValueFactory
    public int getEventId() { return eventId.get(); }
    public String getEventName() { return eventName.get(); }
    public double getTotalRefunded() { return totalRefunded.get(); }

    // Property accessors
    public IntegerProperty eventIdProperty() { return eventId; }
    public StringProperty eventNameProperty() { return eventName; }
    public DoubleProperty totalRefundedProperty() { return totalRefunded; }
}