package com.dbms.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.dbms.models.Customer;
import com.dbms.models.CustomerTicket;
import com.dbms.utils.Database;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class AdminCustomerManagementViewController implements Initializable{
    @FXML
    private TableView<CustomerTicket> customerTicketTable;

    @FXML
    private TableColumn<CustomerTicket, Integer> ticketIdColumn;

    @FXML
    private TableColumn<CustomerTicket, String> eventNameColumn;

    @FXML
    private TableColumn<CustomerTicket, LocalDate> purchaseDateColumn;

    @FXML
    private TableColumn<CustomerTicket, Float> priceColumn;

    @FXML
    private TableColumn<CustomerTicket, String> statusColumn;

    private ObservableList<CustomerTicket> customerTicketList = FXCollections.observableArrayList();

    private int selectedCustomerId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticket_id"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadCustomerTickets(){
        customerTicketList.clear();
        String sql = "SELECT t.ticket_id, e.event_name, t.purchase_date, t.price, t.status FROM Ticket t JOIN Event e ON e.event_id = t.event_id WHERE t.customer_id = ?";

        try (Connection conn = Database.connect();PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setInt(1, selectedCustomerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                customerTicketList.add(new CustomerTicket(
                    resultSet.getInt("ticket_id"),
                    resultSet.getString("event_name"),
                    resultSet.getDate("purchase_date").toLocalDate(),
                    resultSet.getFloat("price"),
                    resultSet.getString("status")
                ));
            }

            customerTicketTable.setItems(customerTicketList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onReturnToCustomerManagementClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/AdminCustomerManagementWindow.fxml"));
            Stage stage = (Stage) customerTicketTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to main menu: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initData(Customer customer){
        selectedCustomerId = customer.getCustomer_id();
        loadCustomerTickets();
    }
}
