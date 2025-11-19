package com.dbms.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.dbms.models.CustomerEvent;
import com.dbms.utils.Database;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class CustomerTicketPurchaseController implements Initializable{
    @FXML
    private TableView<CustomerEvent> availableEventsTable;

    @FXML
    private TableColumn<CustomerEvent, Integer> eventIdColumn;

    @FXML
    private TableColumn<CustomerEvent, String> eventNameColumn;

    @FXML
    private TableColumn<CustomerEvent, LocalTime> timeColumn;

    @FXML
    private TableColumn<CustomerEvent, LocalDate> dateColumn;

    @FXML
    private TableColumn<CustomerEvent, Integer> availableCapacityColumn;

    @FXML
    private TableColumn<CustomerEvent, Float> ticketPriceColumn;

    private ObservableList<CustomerEvent> availableEventsList = FXCollections.observableArrayList();

    private int customerIdNumber = LoginController.getIdNumber();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        availableCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("available_capacity"));
        ticketPriceColumn.setCellValueFactory(new PropertyValueFactory<>("ticket_price"));

        loadAvailableEvents();
    }

    private void loadAvailableEvents(){
        availableEventsList.clear();
        String sql = "SELECT e.event_id, e.event_name, e.time, e.date, " +
                "(e.capacity - COUNT(t.ticket_id)) AS available_capacity, t.price AS ticket_price " +
                "FROM Event e " +
                "LEFT JOIN Ticket t ON t.event_id = e.event_id AND t.status = 'Active' " +
                "WHERE e.status = 'Upcoming' " +
                "GROUP BY e.event_id, e.event_name, e.time, e.date, t.price, e.capacity";
        try (Connection conn = Database.connect();PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                availableEventsList.add(new CustomerEvent(
                    resultSet.getInt("event_id"),
                    resultSet.getString("event_name"),
                    resultSet.getTime("time").toLocalTime(),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getInt("available_capacity"),
                    resultSet.getFloat("ticket_price")
                ));
            }

            availableEventsTable.setItems(availableEventsList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onReturnToCustomerMenuClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/CustomerWindow.fxml"));
            Stage stage = (Stage) availableEventsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to customer menu: " + e.getMessage());
        }
    }

    @FXML
    private void onPurchaseClick(ActionEvent event){
        CustomerEvent selectedEvent = availableEventsTable.getSelectionModel().getSelectedItem();

        String sql = "INSERT INTO Ticket (event_id, customer_id, purchase_date, status, price) " +
            "VALUES(?, ?, ?, ?, ?)";

        if (selectedEvent == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an event to purchase.");
            return;
        }

        try(Connection conn = Database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            
            preparedStatement.setInt(1, selectedEvent.getEvent_id());
            preparedStatement.setInt(2, customerIdNumber);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setString(4, "Active");
            preparedStatement.setDouble(5, selectedEvent.getTicket_price());

            int rowsAffected = preparedStatement.executeUpdate();
            Platform.runLater(() ->{
                if (rowsAffected > 0){
                loadAvailableEvents();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket successfully purchased!");
                } else { 
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to purchase ticket.");
                }
            });
        }
        catch(SQLException e){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            });
            e.printStackTrace();
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
