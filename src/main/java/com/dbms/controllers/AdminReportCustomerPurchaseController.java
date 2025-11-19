package com.dbms.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.dbms.models.CustomerPurchase;
import com.dbms.utils.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;    
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminReportCustomerPurchaseController implements Initializable{
    @FXML
    private TableView<CustomerPurchase> customerPurchaseTable;
    
    @FXML
    private TableColumn<CustomerPurchase, Integer> customerIDColumn;

    @FXML
    private TableColumn<CustomerPurchase, String> nameColumn;

    @FXML
    private TableColumn<CustomerPurchase, Integer> ticketsPurchasedColumn;

    @FXML
    private TableColumn<CustomerPurchase, Float> totalColumn;

    private ObservableList<CustomerPurchase> customerPurchaseList = FXCollections.observableArrayList();

    @FXML
    private DatePicker dateCustomerPurchase;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ticketsPurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("tickets_purchased"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total_spent"));
    }


    private void loadCustomerPurchase(){
        customerPurchaseList.clear();

        LocalDate date = dateCustomerPurchase.getValue();

        if (date == null){
            showAlert(AlertType.ERROR, "Error", "Pick a date first.");
            return;
        }

        int year = date.getYear();
        int month = date.getMonthValue();

        String sql = "SELECT c.customer_id, CONCAT(c.last_name, ', ', c.first_name) AS name, COUNT(t.ticket_id) AS tickets_purchased, " + "SUM(e.ticket_price) AS total_spent " +
                     "FROM Ticket t " +
                     "JOIN Customer c ON t.customer_id = c.customer_id " +
                     "JOIN Event e ON t.event_id = e.event_id " +
                     "WHERE YEAR(t.purchase_date) = ? AND MONTH(t.purchase_date) = ? " +
                     "GROUP BY c.customer_id, c.first_name, c.last_name " +
                     "ORDER BY total_spent DESC";



        try (Connection conn = Database.connect();PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                customerPurchaseList.add(new CustomerPurchase(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getInt("tickets_purchased"),
                    resultSet.getFloat("total_spent")
                ));
            }

            customerPurchaseTable.setItems(customerPurchaseList);

            if (customerPurchaseList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No customer purchases have been made in this month (" + month + "/" + year + ").");
            }

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onCustomerPurchaseDate(ActionEvent event){
        loadCustomerPurchase();
    }

    @FXML
    private void onReturnToReportMenuClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/AdminReportWindow.fxml"));
            Stage stage = (Stage) customerPurchaseTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to report menu: " + e.getMessage());
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
