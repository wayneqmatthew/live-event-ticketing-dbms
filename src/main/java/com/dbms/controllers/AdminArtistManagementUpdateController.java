package com.dbms.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dbms.models.Artist;
import com.dbms.utils.Database;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminArtistManagementUpdateController {

    @FXML
    private TextField nameUpdateField;

    @FXML
    private TextField emailUpdateField;

    @FXML 
    private TextField genreUpdateField;

    @FXML
    private TextField companyUpdateField;

    @FXML
    private Button saveUpdateButton;

    private Artist artistToUpdate = null;
    
    @FXML
    private void onSaveClick(ActionEvent event){
        String name = nameUpdateField.getText();
        String email = emailUpdateField.getText();
        String genre = genreUpdateField.getText();
        String company = companyUpdateField.getText();
        String status = "Active";

        if (name.isEmpty() || email.isEmpty() || genre.isEmpty() || company.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all required fields.");
            return;
        }
        
        new Thread(() -> {
            if (artistToUpdate != null){
                final int artist_id = artistToUpdate.getArtist_id();
                updateArtist(artist_id, name, email, genre, company);
            } else {
                addArtist(name, email, genre, company, status);
            }
        }).start();
    }

    private void addArtist(String name, String email, String genre, String company, String status){
       int artist_id = 0;
        String sql = "INSERT INTO Artist (artist_id, name, email, genre, management_company, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                
                // Generate new artist_id
                String idQuery = "SELECT MAX(artist_id) + 1 AS newID FROM Artist";
                try (PreparedStatement pst = conn.prepareStatement(idQuery);
                    ResultSet rst = pst.executeQuery()) {

                    if (rst.next()) {
                        artist_id = rst.getInt("newID");
                    }
                }

                pstmt.setInt(1, artist_id);
                pstmt.setString(2, name);
                pstmt.setString(3, email);
                pstmt.setString(4, genre);
                pstmt.setString(5, company);
                pstmt.setString(6, status);

                int rowsAffected = pstmt.executeUpdate();

                Platform.runLater(() -> {
                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Artist Successfully Added!");
                        closeWindow();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add artist.");
                    }
                });
                
        } catch(SQLException e){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    private void updateArtist(int artist_id, String name, String email, String genre, String company){
        String sql = "UPDATE Artist SET name = ?, email = ?, genre = ?, management_company = ? WHERE artist_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, genre);
                pstmt.setString(4, company.isEmpty() ? null : company);
                pstmt.setInt(5, artist_id);

                int rowsAffected = pstmt.executeUpdate();
                Platform.runLater(() ->{
                    if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Artist successfully updated!");
                    closeWindow();
                    } else { 
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update artist.");
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

    public void initData(Artist artist){
        this.artistToUpdate = artist;

        nameUpdateField.setText(artist.getName());
        emailUpdateField.setText(artist.getEmail());
        genreUpdateField.setText(artist.getGenre());
        companyUpdateField.setText(artist.getManagement_Company());
    }
}