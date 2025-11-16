package com.dbms.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dbms.models.Venue;
import com.dbms.utils.Database;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VenueManagementAddController {
    @FXML
    private TextField nameAddField;

    @FXML
    private TextField capacityAddField;

    @FXML 
    private TextField cityAddField;

    @FXML
    private TextField countryAddField;

    @FXML
    private TextField regionAddField;

    @FXML
    private Button saveAddButton;

    private Venue venueToUpdate = null;
    
    @FXML
    private void onSaveClick(ActionEvent event){
        String name = nameAddField.getText();
        String city = cityAddField.getText();
        String country = countryAddField.getText();
        String region = regionAddField.getText();
        int capacity = 0;
        String status = "Active";

        if (name.isEmpty() || capacityAddField.getText().isEmpty() || city.isEmpty() || country.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all required fields.");
            return;
        }

        try{
            capacity = Integer.parseInt(capacityAddField.getText());
        } catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Capacity must be a valid number.");
            return;
        }

        final int finalCapacity = capacity;
        //final int venue_id = venueToUpdate.getVenue_id();
        
        new Thread(() -> {
            if (venueToUpdate != null){
                final int venue_id = venueToUpdate.getVenue_id();
                updateVenue(venue_id, name, finalCapacity, city, country, region);
            } else {
                addVenue(name, finalCapacity, city, country, region, status);
            }
        }).start();
    }

    private void addVenue(String name, int capacity, String city, String country, String region, String status){
       int venue_id = 0;
        String sql = "INSERT INTO venue (venue_id, name, capacity, city, country, region, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                
                // Generate new venue_id
                String idQuery = "SELECT MAX(venue_id) + 1 AS newID FROM venue";
                try (PreparedStatement pst = conn.prepareStatement(idQuery);
                    ResultSet rst = pst.executeQuery()) {

                    if (rst.next()) {
                        venue_id = rst.getInt("newID");
                    }
                }

                pstmt.setInt(1, venue_id);
                pstmt.setString(2, name);
                pstmt.setInt(3, capacity);
                pstmt.setString(4, city);
                pstmt.setString(5, country);
                pstmt.setString(6, region.isEmpty() ? null : region);
                pstmt.setString(7, status);

                int rowsAffected = pstmt.executeUpdate();

                Platform.runLater(() -> {
                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Venue Successfully Added!");
                        closeWindow();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add venue.");
                    }
                });
                
        } catch(SQLException e){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    private void updateVenue(int venue_id, String name, int capacity, String city, String country, String region){
        String sql = "UPDATE venue SET name = ?, capacity = ?, city = ?, country = ?, region = ? WHERE venue_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, capacity);
                pstmt.setString(3, city);
                pstmt.setString(4, country);
                pstmt.setString(5, region.isEmpty() ? null : region);
                pstmt.setInt(6, venue_id);

                int rowsAffected = pstmt.executeUpdate();
                Platform.runLater(() ->{
                    if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Venue successfully updated!");
                    closeWindow();
                    } else { 
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update venue.");
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

    // @FXML
    // private void onBackClick(ActionEvent event){
    //     try{
    //         Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/venue-menu.fxml"));

    //         Stage stage = (Stage) nameField.getScene().getWindow();
    //         stage.setScene(new Scene(root));
    //         stage.setTitle("Venue Management");
    //     } catch (IOException e){
    //         e.printStackTrace();
    //         showAlert(Alert.AlertType.ERROR, "Error", "Could not load the venue menu.");
    //     }
    // }

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

    public void initData(Venue venue){
        this.venueToUpdate = venue;

        nameAddField.setText(venue.getName());
        capacityAddField.setText(String.valueOf(venue.getCapacity()));
        cityAddField.setText(venue.getCity());
        countryAddField.setText(venue.getCountry());
        regionAddField.setText(venue.getRegion());
    }
}
