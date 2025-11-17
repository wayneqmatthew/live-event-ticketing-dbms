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

public class LoginController {
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
                    FXMLLoader loaderAdmin = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminWindow.fxml"));
                    Parent rootAdmin = loaderAdmin.load();
                    Scene sceneAdmin = new Scene(rootAdmin);
                    Stage primaryStageAdmin = (Stage) rootPane.getScene().getWindow();

                    primaryStageAdmin.setScene(sceneAdmin);
                    break;
                
                case organizer:
                    FXMLLoader loaderOrganizer = new FXMLLoader(getClass().getResource("/com/dbms/view/OrganizerWindow.fxml"));
                    Parent rootOrganizer = loaderOrganizer.load();
                    Scene sceneOrganizer = new Scene(rootOrganizer);
                    Stage primaryStageOrganizer = (Stage) rootPane.getScene().getWindow();

                    primaryStageOrganizer.setScene(sceneOrganizer);
                    break;
                
                case artist:
                    FXMLLoader loaderArtist = new FXMLLoader(getClass().getResource("/com/dbms/view/ArtistWindow.fxml"));
                    Parent rootArtist = loaderArtist.load();
                    Scene sceneArtist = new Scene(rootArtist);
                    Stage primaryStageArtist = (Stage) rootPane.getScene().getWindow();

                    primaryStageArtist.setScene(sceneArtist);
                    break;
                
                case customer:
                    FXMLLoader loaderCustomer = new FXMLLoader(getClass().getResource("/com/dbms/view/CustomerWindow.fxml"));
                    Parent rootCustomer = loaderCustomer.load();
                    Scene sceneCustomer = new Scene(rootCustomer);
                    Stage primaryStageCustomer = (Stage) rootPane.getScene().getWindow();

                    primaryStageCustomer.setScene(sceneCustomer);
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
