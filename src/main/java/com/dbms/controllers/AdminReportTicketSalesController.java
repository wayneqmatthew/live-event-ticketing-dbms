package com.dbms.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.dbms.models.TicketSales;
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

public class AdminReportTicketSalesController implements Initializable{
    @FXML
    private TableView<TicketSales> ticketSalesTable;
    
    @FXML
    private TableColumn<TicketSales, Integer> eventIdTicketSalesColumn;

    @FXML
    private TableColumn<TicketSales, String> eventNameTicketSalesColumn;

    @FXML
    private TableColumn<TicketSales, Integer> ticketsSoldTicketSalesColumn;

    @FXML
    private TableColumn<TicketSales, Float> totalRevenueTicketSalesColumn;

    private ObservableList<TicketSales> ticketSalesList = FXCollections.observableArrayList();

    @FXML
    private DatePicker dateTicketSales;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        eventIdTicketSalesColumn.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        eventNameTicketSalesColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        ticketsSoldTicketSalesColumn.setCellValueFactory(new PropertyValueFactory<>("tickets_sold"));
        totalRevenueTicketSalesColumn.setCellValueFactory(new PropertyValueFactory<>("total_revenue"));
    }

    private void loadTicketSales(){
        ticketSalesList.clear();

        LocalDate date = dateTicketSales.getValue();

        if (date == null){
            showAlert(AlertType.ERROR, "Error", "Pick a date first.");
            return;
        }

        int year = date.getYear();
        int month = date.getMonthValue();

        String sql = "SELECT e.event_id, e.event_name, COUNT(t.ticket_id) AS tickets_sold, (COUNT(t.ticket_id) * e.ticket_price) AS total_revenue " + 
            "FROM Event e " + 
            "JOIN Ticket t " + 
            "ON t.event_id = e.event_id " + 
            "WHERE YEAR(t.purchase_date) = ? AND MONTH(t.purchase_date) = ? " + 
            "GROUP BY e.event_id, e.event_name, e.ticket_price";



        try (Connection conn = Database.connect();PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                ticketSalesList.add(new TicketSales(
                    resultSet.getInt("event_id"),
                    resultSet.getString("event_name"),
                    resultSet.getInt("tickets_sold"),
                    resultSet.getFloat("total_revenue")
                ));
            }

            if (ticketSalesList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No ticket sales have been made in this month (" + month + "/" + year + ").");
            }

            ticketSalesTable.setItems(ticketSalesList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onTicketSalesDate(ActionEvent event){
        loadTicketSales();
    }

    @FXML
    private void onReturnToReportMenuClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/AdminReportWindow.fxml"));
            Stage stage = (Stage) ticketSalesTable.getScene().getWindow();
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
