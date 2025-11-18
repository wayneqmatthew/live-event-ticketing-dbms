package com.dbms.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import com.dbms.models.Customer;
import com.dbms.utils.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminCustomerManagementController implements Initializable{
    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private TableColumn<Customer, String> firstNameColumn;

    @FXML
    private TableColumn<Customer, String> emailColumn;

    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;

    @FXML
    private TableColumn<Customer, LocalDate> registrationDateColumn;

    @FXML
    private TableColumn<Customer, String> statusColumn;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registration_date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadCustomers();
    }

    private void loadCustomers(){
        customerList.clear();
        String sql = "SELECT customer_id, last_name, first_name, email, phone_number, registration_date, status FROM CUSTOMER";

        try (Connection conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                customerList.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("last_name"),
                    rs.getString("first_name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getDate("registration_date").toLocalDate(),
                    rs.getString("status")
                ));
            }

            customerTable.setItems(customerList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteClick(ActionEvent event){
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a venue to Inactive");
            return;
        }

        if (selectedCustomer.getStatus().equalsIgnoreCase("Inactive")){
            showAlert(Alert.AlertType.INFORMATION, "Already Inactive", "This venue is already 'Inactive'.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this customer?");
        confirmAlert.setContentText(selectedCustomer.getFirst_name() + selectedCustomer.getLast_name());

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            String sql = "UPDATE Customer SET status = 'Inactive' WHERE customer_id = ?";

            try (Connection conn = Database.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)){

                    pstmt.setInt(1, selectedCustomer.getCustomer_id());
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted.");
                        loadCustomers();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer.");
                    }
                 } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
                 }
        }
    }

    @FXML
    private void onUpdateClick(ActionEvent event){
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminCustomerManagementUpdateWindow.fxml"));
            Parent root = loader.load();

            AdminCustomerManagementUpdateController formUpdateController = loader.getController();

            formUpdateController.initData(selectedCustomer);

            Stage stage = new Stage();
            Image logo = new Image("com/dbms/view/assets/logo.png");

            stage.getIcons().add(logo);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadCustomers());
            stage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update data: " + e.getMessage());
        }
    }

    @FXML
    private void onReturnToMainMenuClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/AdminWindow.fxml"));
            Stage stage = (Stage) customerTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to main menu: " + e.getMessage());
        }
    }

    @FXML
    private void onAddClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminCustomerManagementAddWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Image logo = new Image("com/dbms/view/assets/logo.png");

            stage.getIcons().add(logo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(e -> loadCustomers());
            stage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the add customer management: " + e.getMessage());
        }
    }

    @FXML
    private void onViewClick(ActionEvent event){
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to update.");
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminCustomerManagementViewWindow.fxml"));
            Parent root = loader.load();

            AdminCustomerManagementViewController formUpdateController = loader.getController();

            formUpdateController.initData(selectedCustomer);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch(IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the view customer: " + e.getMessage());
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
