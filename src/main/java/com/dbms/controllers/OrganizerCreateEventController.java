package com.dbms.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.dbms.models.Event;
import com.dbms.utils.Database;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OrganizerCreateEventController {
    @FXML
    private TextField venueIDAddField;

    @FXML
    private TextField artistIDAddField;

    @FXML
    private TextField eventNameAddField;

    @FXML
    private TextField timeAddField;

    @FXML
    private TextField dateAddField;

    @FXML
    private TextField capacityAddField;

    @FXML
    private Button saveAddButton;

    private Event eventToUpdate = null;

    private final int organizer_id = 1;
    
    @FXML
    private void onSaveClick(ActionEvent event){
        String venue = venueIDAddField.getText();
        String artist = artistIDAddField.getText();
        String name = eventNameAddField.getText();
        String timeStr = timeAddField.getText();
        String dateStr = dateAddField.getText();
        String status = "Active";

        if (venue.isEmpty() || artist.isEmpty() || name.isEmpty() || timeStr.isEmpty() || dateStr.isEmpty() || capacityAddField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all required fields.");
            return;
        }

        int venue_id, artist_id, capacity;
        LocalDate date;
        LocalTime time;
        
        try{
            venue_id = Integer.parseInt(venue);
            artist_id = Integer.parseInt(artist);
            capacity = Integer.parseInt(capacityAddField.getText());
        } catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Numeric fields must be in a valid format.");
            return;
        }
        
        try {
            date = LocalDate.parse(dateStr);
            time = LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Invalid date or time format. Use yyyy-mm-dd and hh:mm:ss");
        return;
        }


        new Thread(() -> {
            if (eventToUpdate != null){
                final int event_id = eventToUpdate.getEvent_id();
                updateEvent(event_id, venue_id, artist_id, organizer_id, name, time, date, capacity);
            } else {
                addEvent(venue_id, artist_id, organizer_id, name, time, date, capacity, status);
            }
        }).start();
    }

    private void addEvent(int venue_id, int artist_id, int organizer_id, String name, LocalTime time, LocalDate date, int capacity, String status){
        int event_id = 0;
        organizer_id = LoginController.getIdNumber();
        String sql = "INSERT INTO Event (event_id, venue_id, artist_id, organizer_id, event_name, time, date, capacity, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                
                // Generate new venue_id
                String idQuery = "SELECT MAX(event_id) + 1 AS newID FROM Event";
                try (PreparedStatement pst = conn.prepareStatement(idQuery);
                    ResultSet rst = pst.executeQuery()) {

                    if (rst.next()) {
                        event_id = rst.getInt("newID");
                    }
                }

                pstmt.setInt(1, event_id);
                pstmt.setInt(2, venue_id);
                pstmt.setInt(3, artist_id);
                pstmt.setInt(4, organizer_id);
                pstmt.setString(5, name);
                pstmt.setTime(6, java.sql.Time.valueOf(time));
                pstmt.setDate(7, java.sql.Date.valueOf(date));
                pstmt.setInt(8, capacity);
                pstmt.setString(9, status);

                int rowsAffected = pstmt.executeUpdate();

                Platform.runLater(() -> {
                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Event Successfully Added!");
                        closeWindow();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add event.");
                    }
                });
                
        } catch(SQLException e){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    private void updateEvent(int event_id, int venue_id, int artist_id, int organizer_id, String name, LocalTime time, LocalDate date, int capacity){
        String sql = "UPDATE Event SET venue_id = ?, artist_id = ?, organizer_id = ?, name = ?, time = ?, date = ?, capacity = ? WHERE event_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, venue_id);
                pstmt.setInt(2, artist_id);
                pstmt.setInt(3, organizer_id);
                pstmt.setString(4, name);
                pstmt.setTime(5, java.sql.Time.valueOf(time));
                pstmt.setDate(6, java.sql.Date.valueOf(date));
                pstmt.setInt(7, capacity);
                pstmt.setInt(8, event_id);

                int rowsAffected = pstmt.executeUpdate();
                Platform.runLater(() ->{
                    if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Event successfully updated!");
                    closeWindow();
                    } else { 
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update event.");
                    }
                });
                
             } catch (SQLException e){
                Platform.runLater(() ->{
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
                });
                
                e.printStackTrace();
             }
    }

    @FXML
    private void onCancelClick(ActionEvent event){
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) saveAddButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initData(Event event){
        this.eventToUpdate = event;

        venueIDAddField.setText(String.valueOf(event.getVenue_id()));
        artistIDAddField.setText(String.valueOf(event.getArtist_id()));
        eventNameAddField.setText(event.getEvent_name());
        timeAddField.setText(event.getTime().toString());
        dateAddField.setText(event.getDate().toString());
        capacityAddField.setText(String.valueOf(event.getCapacity()));

    }
}
