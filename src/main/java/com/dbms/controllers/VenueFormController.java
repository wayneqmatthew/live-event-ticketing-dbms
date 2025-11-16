package com.dbms.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dbms.models.Venue;
import com.dbms.utils.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class VenueFormController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField capacityField;

    @FXML 
    private TextField cityField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField regionField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    private Venue venueToUpdate = null;
    
    @FXML
    private void onSaveVenueClick(ActionEvent event){
        String name = nameField.getText();
        String city = cityField.getText();
        String country = countryField.getText();
        String region = regionField.getText();
        int capacity = 0;
        String status = "Active";

        if (name.isEmpty() || capacityField.getText().isEmpty() || city.isEmpty() || country.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all required fields.");
            return;
        }

        try{
            capacity = Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Capacity must be a valid number.");
            return;
        }

        if (venueToUpdate != null){
            updateVenue(name, capacity, city, country, region);
        } else {
            addVenue(name, capacity, city, country, region, status);
        }
    }

    private void addVenue(String name, int capacity, String city, String country, String region, String status){
        String sql = "INSERT INTO Venue (venue_name, venue_capacity, city, country, region, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setString(1, name);
                pstmt.setInt(2, capacity);
                pstmt.setString(3, city);
                pstmt.setString(4, country);
                pstmt.setString(5, region.isEmpty() ? null : region);
                pstmt.setString(6, status);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Venue Successfully Added!");
                    closeWindow();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add venue.");
                }
                
        } catch(SQLException e){
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateVenue(String name, int capacity, String city, String country, String region){
        String sql = "UPDATE venue SET venue_name = ?, venue_capacity = ?, city = ?, country = ?, region = ? WHERE venue_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, capacity);
                pstmt.setString(3, city);
                pstmt.setString(4, country);
                pstmt.setString(5, region.isEmpty() ? null : region);
                pstmt.setInt(6, venueToUpdate.getVenue_id());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Venue successfully updated!");
                    closeWindow();
                } else { 
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update venue.");
                }
             } catch (SQLException e){
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
                e.printStackTrace();
             }
    }

    @FXML
    private void onCancelClick(ActionEvent event){
        closeWindow();
    }

    @FXML
    private void onBackClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/venue-menu.fxml"));

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Venue Management");
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the venue menu.");
        }
    }

    private void closeWindow(){
        Stage stage = (Stage) saveButton.getScene().getWindow();
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

        nameField.setText(venue.getName());
        capacityField.setText(String.valueOf(venue.getCapacity()));
        cityField.setText(venue.getCity());
        countryField.setText(venue.getCountry());
        regionField.setText(venue.getRegion());
    }
}