package com.dbms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginManagementController {
    @FXML
    private Pane rootPane;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    private String email;
    private String password;

    private final String adminEmail = "admin@gmail.com";
    private final String adminPassword = "admin";

    private final String organizerEmail = "organizer@gmail.com";
    private final String organizerPassword = "admin";

    private final String artistEmail = "artist@gmail.com";
    private final String artistPassword = "admin";

    private final String customerEmail = "customer@gmail.com";
    private final String customerPassword = "admin";

    private final String admin = adminEmail + ":" + adminPassword;
    private final String organizer = organizerEmail + ":" + organizerPassword;
    private final String artist = artistEmail + ":" + artistPassword;
    private final String customer = customerEmail + ":" + customerPassword;

    @FXML
    private void onSubmitClick(ActionEvent event){
        try{
            email = emailField.getText();
            password = passwordField.getText();

            switch(email + ":" + password){
                case admin:
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/venue-menu.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage primaryStage = (Stage) rootPane.getScene().getWindow();

                    primaryStage.setScene(scene);
                    break;
                
                case organizer:
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/venue-menu.fxml"));
                    break;
                
                case artist:
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/venue-menu.fxml"));
                    break;
                
                case customer:
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/venue-menu.fxml"));
                    break;
                
                default:
                    showAlert(AlertType.ERROR, "Error", "Invalid email or password");
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to submit email or password: " + e.getMessage());
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
