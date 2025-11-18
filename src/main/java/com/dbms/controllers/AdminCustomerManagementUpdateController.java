package com.dbms.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.dbms.models.Customer;
import com.dbms.utils.Database;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminCustomerManagementUpdateController {
    @FXML
    private TextField lastNameUpdateField;

    @FXML
    private TextField firstNameUpdateField;

    @FXML 
    private TextField emailUpdateField;

    @FXML
    private TextField phoneNumberUpdateField;

    @FXML
    private DatePicker dateUpdateField;

    @FXML
    private Button saveUpdateButton;

    private Customer customerToUpdate = null;
    
    @FXML
    private void onSaveClick(ActionEvent event){
        String lastName = lastNameUpdateField.getText();
        String firstName = firstNameUpdateField.getText();
        String email = emailUpdateField.getText();
        String phoneNumber = phoneNumberUpdateField.getText();
        LocalDate date = dateUpdateField.getValue();
        String status = "Active";

        if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || date == null){
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return;
        }
        
        new Thread(() -> {
            if (customerToUpdate != null){
                final int customer_id = customerToUpdate.getCustomer_id();
                updateCustomer(customer_id, lastName, firstName, email, phoneNumber, date);
            } else {
                addCustomer(lastName, firstName, email, phoneNumber, date, status);
            }
        }).start();
    }

    private void addCustomer(String lastName, String firstName, String email, String phoneNumber, LocalDate date, String status){
       int customer_id = 0;
        String sql = "INSERT INTO customer (customer_id, last_name, first_name, email, phone_number, registration_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                
                // Generate new venue_id
                String idQuery = "SELECT MAX(customer_id) + 1 AS newID FROM customer";
                try (PreparedStatement pst = conn.prepareStatement(idQuery);
                    ResultSet rst = pst.executeQuery()) {

                    if (rst.next()) {
                        customer_id = rst.getInt("newID");
                    }
                }

                pstmt.setInt(1, customer_id);
                pstmt.setString(2, lastName);
                pstmt.setString(3, firstName);
                pstmt.setString(4, email);
                pstmt.setString(5, phoneNumber);
                pstmt.setDate(6, Date.valueOf(date));
                pstmt.setString(7, status);

                int rowsAffected = pstmt.executeUpdate();

                Platform.runLater(() -> {
                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Successfully Added!");
                        closeWindow();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add customer.");
                    }
                });
                
        } catch(SQLException e){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    private void updateCustomer(int customer_id, String lastName, String firstName, String email, String phoneNumber, LocalDate date){
        String sql = "UPDATE customer SET last_name = ?, first_name = ?, email = ?, phone_number = ?, registration_date = ? WHERE customer_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, lastName);
                pstmt.setString(2, firstName);
                pstmt.setString(3, email);
                pstmt.setString(4, phoneNumber);
                pstmt.setDate(5, Date.valueOf(date));
                pstmt.setInt(6, customer_id);

                int rowsAffected = pstmt.executeUpdate();
                Platform.runLater(() ->{
                    if (rowsAffected > 0){
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Customer successfully updated!");
                    closeWindow();
                    } else { 
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update customer.");
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

    public void initData(Customer customer){
        this.customerToUpdate = customer;

        lastNameUpdateField.setText(customer.getLast_name());
        firstNameUpdateField.setText(customer.getFirst_name());
        emailUpdateField.setText(customer.getEmail());
        phoneNumberUpdateField.setText(customer.getPhone_number());
        dateUpdateField.setValue(customer.getRegistration_date());
    }
}
