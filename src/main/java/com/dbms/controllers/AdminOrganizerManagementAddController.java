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

public class AdminOrganizerManagementAddController {
    @FXML
    private TextField nameAddField;

    @FXML
    private TextField emailAddField;

    @FXML 
    private TextField cityAddField;

    @FXML
    private TextField countryAddField;

    @FXML
    private TextField regionAddField;

    @FXML
    private Button saveAddButton;

    private Organizer OrganizerToUpdate = null;
    
    @FXML
    private void onSaveClick(ActionEvent event){
        String name = nameAddField.getText();
        String city = cityAddField.getText();
        String email = emailAddField.getText();
        String country = countryAddField.getText();
        String region = regionAddField.getText();
        String status = "Active";

        if (name.isEmpty() || emailAddField.getText().isEmpty() || city.isEmpty() || country.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all required fields.");
            return;
        }

        new Thread(() -> {
            if (OrganizerToUpdate != null){
                final int Organizer_id = OrganizerToUpdate.getOrganizer_id();
                updateOrganizer(Organizer_id, name, email, city, country, region);
            } else {
                addOrganizer(name, email, city, country, region, status);
            }
        }).start();
    }

    private void addOrganizer(String name, String email, String city, String country, String region, String status){
       int Organizer_id = 0;
        String sql = "INSERT INTO Organizer (Organizer_id, name, email, city, country, region, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                
                // Generate new Organizer_id
                String idQuery = "SELECT MAX(Organizer_id) + 1 AS newID FROM Organizer";
                try (PreparedStatement pst = conn.prepareStatement(idQuery);
                    ResultSet rst = pst.executeQuery()) {

                    if (rst.next()) {
                        Organizer_id = rst.getInt("newID");
                    }
                }

                pstmt.setInt(1, Organizer_id);
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
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add Organizer.");
                    }
                });
                
        } catch(SQLException e){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    private void updateOrganizer(int Organizer_id, String name, String email, String city, String country, String region){
        String sql = "UPDATE Organizer SET name = ?, email = ?, city = ?, country = ?, region = ? WHERE Organizer_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, city);
                pstmt.setString(4, country);
                pstmt.setString(5, region.isEmpty() ? null : region);
                pstmt.setInt(6, Organizer_id);

                int rowsAffected = pstmt.executeUpdate();
                Platform.runLater(() ->{
                    if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Organizer successfully updated!");
                    closeWindow();
                    } else { 
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update Organizer.");
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

    public void initData(Organizer Organizer){
        this.OrganizerToUpdate = Organizer;

        nameAddField.setText(Organizer.getName());
        emailAddField.setText(String.valueOf(Organizer.getEmail()));
        cityAddField.setText(Organizer.getCity());
        countryAddField.setText(Organizer.getCountry());
        regionAddField.setText(Organizer.getRegion());
    }
}
