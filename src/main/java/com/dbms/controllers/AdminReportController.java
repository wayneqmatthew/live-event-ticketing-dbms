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

public class AdminReportController {
    @FXML
    private Pane rootPane;

    @FXML
    private void onTicketSalesReportClick(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminReportTicketSalesWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }
        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load Ticket Sales Report: " + e.getMessage());
        }
    }

    @FXML
    private void onCustomerPurchaseReportClick(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminReportCustomerPurchaseWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }
        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load Ticket Sales Report: " + e.getMessage());
        }
    }

    @FXML
    private void onTicketRefundReportClick(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminReportTicketRefundWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }
        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load Ticket Refund Report: " + e.getMessage());
        }
    }

    @FXML
    private void onArtistPaymentReportClick(ActionEvent event){
        
    }

    @FXML
    private void onReturnToAdminMenuClick(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();

            primaryStage.setScene(scene);
        }
        catch(Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to return to Admin Menu: " + e.getMessage());
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
