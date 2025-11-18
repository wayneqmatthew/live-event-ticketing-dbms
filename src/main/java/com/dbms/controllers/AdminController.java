package com.dbms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AdminController {
    @FXML
    private Pane rootPane;


    @FXML
    private void onArtistManagementClick(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminArtistManagementWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }

        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load Artist Management: " + e.getMessage());
        }
    }

    @FXML
    private void onCustomerManagementClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminCustomerManagementWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }

        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load Customer Management: " + e.getMessage());
        }
    }

    @FXML
    private void onVenueManagementClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminVenueManagementWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }

        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load Venue Management: " + e.getMessage());
        }
    }

    @FXML
    private void onOrganizerManagementClick(){

    }

    @FXML
    private void onLogOutClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/LoginWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }

        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load log out: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
