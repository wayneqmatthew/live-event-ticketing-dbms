package com.dbms.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dbms.models.Organizer;
import com.dbms.utils.Database;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminOrganizerManagementUpdateController {

    @FXML
    private TextField nameUpdateField;

    @FXML
    private TextField emailUpdateField;

    @FXML 
    private TextField cityUpdateField;

    @FXML
    private TextField countryUpdateField;

    @FXML
    private TextField regionUpdateField;

    @FXML
    private Button saveUpdateButton;

    private Organizer organizerToUpdate = null;
    
    @FXML
    private void onSaveClick(ActionEvent event){
        String name = nameUpdateField.getText();
        String email = emailUpdateField.getText();
        String city = cityUpdateField.getText();
        String country = countryUpdateField.getText();
        String region = regionUpdateField.getText();
        String status = "Active";

        if (name.isEmpty() || emailUpdateField.getText().isEmpty() || city.isEmpty() || country.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all required fields.");
            return;
        }

        
        new Thread(() -> {
            if (organizerToUpdate != null){
                final int organizer_id = organizerToUpdate.getOrganizer_id();
                updateOrganizer(organizer_id, name, email, city, country, region);
            } else {
                addOrganizer(name, email, city, country, region, status);
            }
        }).start();

    }

    private void addOrganizer(String name, String email, String city, String country, String region, String status){
       int organizer_id = 0;
        String sql = "INSERT INTO organizer (organizer_id, name, email, city, country, region, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                
                // Generate new organizer_id
                String idQuery = "SELECT MAX(organizer_id) + 1 AS newID FROM organizer";
                try (PreparedStatement pst = conn.prepareStatement(idQuery);
                    ResultSet rst = pst.executeQuery()) {

                    if (rst.next()) {
                        organizer_id = rst.getInt("newID");
                    }
                }

                pstmt.setInt(1, organizer_id);
                pstmt.setString(2, name);
                pstmt.setString(3, email);
                pstmt.setString(4, city);
                pstmt.setString(5, country);
                pstmt.setString(6, region.isEmpty() ? null : region);
                pstmt.setString(7, status);

                int rowsAffected = pstmt.executeUpdate();

                Platform.runLater(() -> {
                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Organizer Successfully Added!");
                        closeWindow();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add organizer.");
                    }
                });
                
        } catch(SQLException e){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    private void updateOrganizer(int organizer_id, String name, String email, String city, String country, String region){
        String sql = "UPDATE organizer SET name = ?, email = ?, city = ?, country = ?, region = ? WHERE organizer_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, city);
                pstmt.setString(4, country);
                pstmt.setString(5, region.isEmpty() ? null : region);
                pstmt.setInt(6, organizer_id);

                int rowsAffected = pstmt.executeUpdate();
                Platform.runLater(() ->{
                    if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Organizer successfully updated!");
                    closeWindow();
                    } else { 
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update organizer.");
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
        Stage stage = (Stage) saveUpdateButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initData(Organizer organizer){
        this.organizerToUpdate = organizer;

        nameUpdateField.setText(organizer.getName());
        emailUpdateField.setText(organizer.getEmail());
        cityUpdateField.setText(organizer.getCity());
        countryUpdateField.setText(organizer.getCountry());
        regionUpdateField.setText(organizer.getRegion());
    }
}