package com.dbms.controllers;

import com.dbms.models.CancellationReport;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CancellationReportController implements Initializable{
    @FXML
    private TableView<CancellationReport> reportTable;

    @FXML
    private TableColumn<CancellationReport, Integer> cancellationIdColumn;

    @FXML
    private TableColumn<CancellationReport, Integer> ticketIdColumn;

    @FXML
    private TableColumn<CancellationReport, String> eventNameColumn;

    @FXML
    private TableColumn<CancellationReport, Double> refundAmountColumn;

    @FXML
    private TableColumn<CancellationReport, String> cancellationDateColumn;

    @FXML
    private Button backButton;

    private ObservableList<CancellationReport> reportList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        cancellationIdColumn.setCellValueFactory(new PropertyValueFactory<>("cancellationId"));
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        refundAmountColumn.setCellValueFactory(new PropertyValueFactory<>("refundAmount"));
        cancellationDateColumn.setCellValueFactory(new PropertyValueFactory<>("cancellationDate"));

        loadReport();
    }

    private void loadReport(){
        reportList.clear();

        String sql = "SELECT c.cancellation_ref_id, c.ticket_id, c.refund_amount, c.cancellation_date, e.event_name " +
                     "FROM Cancellation c " + 
                     "JOIN Ticket t ON c.ticket_id = t.ticket_id " +
                     "JOIN Event e ON t.event_id = e.event_id " +
                     "ORDER BY c.cancellation_date DESC";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                reportList.add(new CancellationReport(
                    rs.getInt("cancellation_ref_id"),
                    rs.getInt("ticket_id"),
                    rs.getString("event_name"),
                    rs.getDouble("refund_amount"),
                    rs.getDate("cancellation_date")
                ));
            }

            reportTable.setItems(reportList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", " Error Loading report" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onReturnToMenuClick(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/CustomerWindow.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
        } catch (IOException e){
            e.printStackTrace();
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
