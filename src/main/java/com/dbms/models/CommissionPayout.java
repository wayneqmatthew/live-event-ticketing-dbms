package com.dbms.models;

import java.sql.Date;
import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CommissionPayout {
    private final IntegerProperty payoutId;
    private final IntegerProperty artistId;
    private final IntegerProperty organizerId;
    private final IntegerProperty eventId;
    private final DoubleProperty commissionPercentage; 
    private final StringProperty payoutDate;
    
    public CommissionPayout(int payoutId, int artistId, int organizerId, int eventId, double commissionPercentage, LocalDate payoutDate) {
        this.payoutId = new SimpleIntegerProperty(payoutId);
        this.artistId = new SimpleIntegerProperty(artistId);
        this.organizerId = new SimpleIntegerProperty(organizerId);
        this.eventId = new SimpleIntegerProperty(eventId);
        this.commissionPercentage = new SimpleDoubleProperty(commissionPercentage);
        this.payoutDate = new SimpleStringProperty(payoutDate.toString());
    }

    public IntegerProperty payoutIdProperty() { return payoutId; }
    public IntegerProperty artistIdProperty() { return artistId; }
    public IntegerProperty organizerIdProperty() { return organizerId; }
    public IntegerProperty eventIdProperty() { return eventId; }
    public DoubleProperty commissionPercentageProperty() { return commissionPercentage; }
    public StringProperty payoutDateProperty() { return payoutDate; }

    public int getPayoutId() { return payoutId.get(); }
    public int getArtistId() { return artistId.get(); }
    public int getOrganizerId() { return organizerId.get(); }
    public int getEventId() { return eventId.get(); }
    public double getCommissionPercentage() { return commissionPercentage.get(); }
    public String getPayoutDate() { return payoutDate.get(); }

}