package com.dbms.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import com.dbms.utils.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage; 

public class CancellationController {

    @FXML
    private TextField ticketIdField;

    @FXML
    private Button findTicketButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextArea detailsLabel;

    @FXML
    private Button backButton;

    private int foundTicketId;
    private double foundTicketPrice;

    @FXML
    public void initialize(){
        detailsLabel.setVisible(false);
        detailsLabel.setEditable(false);
        confirmButton.setDisable(true);
    }

    @FXML
    private void onFindTicketClick(ActionEvent event){
        String ticketIdText = ticketIdField.getText();
        int ticketId = 0;

        detailsLabel.setVisible(false);
        confirmButton.setDisable(true);
        detailsLabel.clear();

        try {
            ticketId = Integer.parseInt(ticketIdText);
        } catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ticket ID must be a number");
            return;
        }

        String sql = "SELECT t.ticket_id, t.price, t.purchase_date, t.status, e.event_name, e.status AS event_status " +
                     "FROM Ticket t " +
                     "JOIN Event e ON t.event_id = e.event_id " +
                     "WHERE t.ticket_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1, ticketId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String status = rs.getString("status");
                    String eventStatus = rs.getString("event_status");
                    double price = rs.getDouble("price");
                    String eventName = rs.getString("event_name");
                    String purchaseDate = rs.getString("purchase_date");

                    if (eventStatus.equalsIgnoreCase("Done")){
                        showAlert(Alert.AlertType.ERROR, "Event Over", "This ticket is for an event that has already finished. It is not eligible for a refund.");
                    }
                    else if (status.equalsIgnoreCase("Cancelled")) {
                        showAlert(Alert.AlertType.INFORMATION, "Already Cancelled", "This ticket (ID: " + ticketId + ") has already been cancelled.");
                    }
                    else {
                        String details = String.format (
                            "Event: %s\n" +
                            "Ticket ID: %d\n" +
                            "Price: %.2f\n" +
                            "Purchased: %s",
                            eventName, ticketId, price, purchaseDate
                        );

                        detailsLabel.setText(details);
                        detailsLabel.setVisible(true);
                        confirmButton.setDisable(false);

                        this.foundTicketId = ticketId;
                        this.foundTicketPrice = price;
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Not Found", "No ticket found with ID: " + ticketId);
                }
                
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    @FXML
    private void onConfirmCancellationClick(ActionEvent event){
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancellation");
        confirmAlert.setHeaderText("Are you sure you want to cancel this ticket?");
        confirmAlert.setContentText(String.format("Ticket ID: %d\nRefund Amount: %.2f", this.foundTicketId, this.foundTicketPrice)); 

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String updateTicketSql = "UPDATE Ticket SET status = 'Cancelled' WHERE ticket_id = ?";
            String insertCancelSql = "INSERT INTO Cancellation (ticket_id, refund_amount, cancellation_date) VALUES (?, ?, ?)";

            Connection conn = null;
            try {
                conn = Database.connect();
                conn.setAutoCommit(false);

                try (PreparedStatement pstmtTicket = conn.prepareStatement(updateTicketSql)) {
                    pstmtTicket.setInt(1, this.foundTicketId);
                    pstmtTicket.executeUpdate();
                }

                try (PreparedStatement pstmtCancel = conn.prepareStatement(insertCancelSql)) {
                    pstmtCancel.setInt(1, this.foundTicketId);
                    pstmtCancel.setDouble(2, this.foundTicketPrice);
                    pstmtCancel.setDate(3, Date.valueOf(LocalDate.now()));
                    pstmtCancel.executeUpdate();
                }

                conn.commit();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket " + this.foundTicketId + " has been cancelled");
                resetUI();

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to cancel ticket. Error " + e.getMessage());
                e.printStackTrace();

                if (conn != null){
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    private void onBackClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/CustomerWindow.fxml"));

            Stage stage = (Stage) ticketIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Menu");
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the venue menu.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetUI(){
        ticketIdField.clear();
        detailsLabel.clear();
        detailsLabel.setVisible(false);
        confirmButton.setDisable(true);

        this.foundTicketId = 0;
        this.foundTicketPrice = 0.0;
    }
    
}
