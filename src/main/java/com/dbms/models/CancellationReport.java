package com.dbms.models;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Date;

public class CancellationReport {
    private final IntegerProperty cancellationId;
    private final IntegerProperty ticketId;
    private final StringProperty eventName;
    private final DoubleProperty refundAmount;
    private final StringProperty cancellationDate;

    public CancellationReport(int cancellationId, int ticketId, String eventName, double refundAmount, Date cancellationDate){
        this.cancellationId = new SimpleIntegerProperty(cancellationId);
        this.ticketId = new SimpleIntegerProperty(ticketId);
        this.eventName = new SimpleStringProperty(eventName);
        this.refundAmount = new SimpleDoubleProperty(refundAmount);
        this.cancellationDate = new SimpleStringProperty(cancellationDate.toString());
    }

    public IntegerProperty cancellationIdProperty(){ return cancellationId;}
    public IntegerProperty ticketIdProperty(){ return ticketId;}
    public StringProperty eventNameProperty(){ return eventName;}
    public DoubleProperty refuundAmountProperty(){ return refundAmount;}
    public StringProperty cancellationDateProperty(){ return cancellationDate;}
}
