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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

// Note: Ensure your FXML points to this controller name!
public class CancellationSummaryReportController implements Initializable {

    // --- FXML Components (MUST match Step 2 FXML IDs) ---
    @FXML private TableView<CancellationReport> reportTable;
    @FXML private TableColumn<CancellationReport, Integer> eventIdColumn;
    @FXML private TableColumn<CancellationReport, String> eventNameColumn;
    @FXML private TableColumn<CancellationReport, Double> totalRefundedColumn;
    @FXML private DatePicker datePicker;
    @FXML private Button backButton;
    // NOTE: ticketsRefundedColumn is REMOVED

    private ObservableList<CancellationReport> reportList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Setup Columns (Bind to Model)
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        totalRefundedColumn.setCellValueFactory(new PropertyValueFactory<>("totalRefunded"));
    }

    @FXML
    private void onGenerateReportClick(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Date", "Please select a month and year to generate the report.");
            return;
        }

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();

        loadSummaryReport(year, month);
    }

    private void loadSummaryReport(int year, int month) {
        reportList.clear();

        // --- SQL AGGREGATION LOGIC (Simplified to only SUM) ---
        String sql = "SELECT e.event_id, e.event_name, " +
                     "SUM(c.refund_amount) AS total_refund " + // Only SUM remains
                     "FROM Cancellation c " +
                     "JOIN Ticket t ON c.ticket_id = t.ticket_id " +
                     "JOIN Event e ON t.event_id = e.event_id " +
                     "WHERE YEAR(c.cancellation_date) = ? AND MONTH(c.cancellation_date) = ? " +
                     "GROUP BY e.event_id, e.event_name " +
                     "ORDER BY total_refund DESC";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            pstmt.setInt(2, month);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reportList.add(new CancellationReport(
                    rs.getInt("event_id"),
                    rs.getString("event_name"),
                    rs.getDouble("total_refund") // Only two data points from the SQL
                ));
            }

            reportTable.setItems(reportList);

            if (reportList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No total refund amounts found in the selected month (" + month + "/" + year + ").");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load report: " + e.getMessage());
        }
    }

    // --- Navigation ---
    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/AdminReportMenu.fxml")); // Assuming this is your admin menu
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
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