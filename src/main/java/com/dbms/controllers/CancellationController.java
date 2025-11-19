package com.dbms.controllers;

import com.dbms.models.CustomerTicket;
import com.dbms.utils.Database;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CancellationController implements Initializable {

    @FXML private TableView<CustomerTicket> ticketsTable;
    @FXML private TableColumn<CustomerTicket, Integer> ticketIdColumn;
    @FXML private TableColumn<CustomerTicket, String> eventNameColumn;
    @FXML private TableColumn<CustomerTicket, LocalDate> purchaseDateColumn; 
    @FXML private TableColumn<CustomerTicket, Float> priceColumn;
    @FXML private TableColumn<CustomerTicket, String> statusColumn;
    
    @FXML private Button backButton; 

    private ObservableList<CustomerTicket> ticketList = FXCollections.observableArrayList();
    private int customerIdNumber = LoginController.getIdNumber();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticket_id"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("ticket_price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadMyTickets();
    }

    private void loadMyTickets() {
        ticketList.clear();
        
        // We are aliasing the columns to match the standard getters (ticket_id, price, status, etc.)
        String sql = "SELECT t.ticket_id AS ticket_id, " + 
                    "e.event_name AS event_name, " +   
                    "t.purchase_date AS purchase_date, " +
                    "t.price AS ticket_price, " +
                    "t.status AS status " + 
                    "FROM Ticket t " +
                    "JOIN Event e ON t.event_id = e.event_id " +
                    "WHERE t.customer_id = ? " +
                    "AND t.status = 'Active' " +
                    "AND e.status = 'Upcoming'";

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerIdNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ticketList.add(new CustomerTicket(
                    // Ensure retrieval matches the data type and the column alias
                    rs.getInt("ticket_id"),
                    rs.getString("event_name"),
                    rs.getDate("purchase_date").toLocalDate(),
                    rs.getFloat("ticket_price"),
                    rs.getString("status") 
                ));
            }
            ticketsTable.setItems(ticketList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load tickets: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelTicket(ActionEvent event) {
        CustomerTicket selectedTicket = ticketsTable.getSelectionModel().getSelectedItem();

        if (selectedTicket == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a ticket to cancel.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancellation");
        confirmAlert.setHeaderText("Cancel Ticket #" + selectedTicket.getTicket_id() + "?");
        confirmAlert.setContentText("Event: " + selectedTicket.getEvent_name() + 
                                    "\nRefund Amount: " + selectedTicket.getTicket_price());

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            performCancellation(selectedTicket);
        }
    }

    private void performCancellation(CustomerTicket ticket) {
        String updateTicketSql = "UPDATE Ticket SET status = 'Cancelled' WHERE ticket_id = ?";
        String insertCancelSql = "INSERT INTO Cancellation (ticket_id, refund_amount, cancellation_date) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = Database.connect();
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmt1 = conn.prepareStatement(updateTicketSql)) {
                pstmt1.setInt(1, ticket.getTicket_id());
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(insertCancelSql)) {
                pstmt2.setInt(1, ticket.getTicket_id());
                pstmt2.setDouble(2, ticket.getTicket_price());
                pstmt2.setDate(3, Date.valueOf(LocalDate.now()));
                pstmt2.executeUpdate();
            }

            conn.commit(); 
            
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket cancelled successfully.");
                loadMyTickets(); 
            });

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Transaction Failed", "Error cancelling ticket: " + e.getMessage());
            });
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/CustomerWindow.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Menu");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the menu.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}