package com.dbms.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OrganizerController {
    @FXML
    private Pane rootPane;

    @FXML
    private void onViewClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/OrganizerViewEventWindow.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }

        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to view event window: " + e.getMessage());
        }
    }

    @FXML
    private void onPayoutClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/CommissionPayoutWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }

        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load payout: " + e.getMessage());
        }
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
            showAlert(AlertType.ERROR, "Error", "Failed to log out: " + e.getMessage());
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
