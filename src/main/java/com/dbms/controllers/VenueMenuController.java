package com.dbms.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class VenueMenuController {
    @FXML
    private Button addNewVenueButton;

    @FXML 
    private Button manageExistingVenuesButton;

    @FXML
    private void onAddNewVenueClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/venue-form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Venue");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the add venue form.");
        }
    }

    @FXML
    private void onManageVenuesClick(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("com/dbms/venue-view.fxml"));

            Stage stage = (Stage) manageExistingVenuesButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Venues");

        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the venue manager form.");
        }
    }

    @FXML
    private void onBackClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/admin.fxml"));

            Stage stage = (Stage) manageExistingVenuesButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Venue menu");
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the venue menu.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
