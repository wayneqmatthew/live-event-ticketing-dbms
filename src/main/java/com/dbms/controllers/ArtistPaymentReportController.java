package com.dbms.controllers;

import com.dbms.controllers.ArtistPaymentReportDTO;
import com.dbms.models.CommissionPayout;
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


public class ArtistPaymentReportController implements Initializable {

    // --- FXML Components (MUST match Step 2 FXML IDs) ---
    @FXML private TableView<ArtistPaymentReportDTO> artistPaymentReportTable;
    @FXML private TableColumn<ArtistPaymentReportDTO, Integer> artistIdColumn;
    @FXML private TableColumn<ArtistPaymentReportDTO, String> artistNameColumn;
    @FXML private TableColumn<ArtistPaymentReportDTO, Float> totalPaymentColumn;
    @FXML private DatePicker datePicker;
    @FXML private Button backButton;


    private ObservableList<ArtistPaymentReportDTO> reportList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Setup Columns (Bind to Model)
        artistIdColumn.setCellValueFactory(new PropertyValueFactory<>("artist_id"));
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artist_name"));
        totalPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("total_payment"));
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
        String sql = "SELECT a.artist_id, a.name AS artist_name, " +
                    " COALESCE(SUM(e.ticket_price * (cp.commission_percentage / 100)), 0) AS total_payment " +
                    " FROM artist a " +
                    " LEFT JOIN commissionPayout cp ON a.artist_id = cp.artist_id" +
                    " LEFT JOIN event e ON e.event_id = cp.event_id " +
                    " LEFT JOIN ticket t ON t.event_id = e.event_id AND t.status = 'Active' " +
                    " WHERE YEAR(cp.payout_date) = ? AND MONTH(cp.payout_date) = ? " +
                    " GROUP BY a.artist_id, a.name " +
                    " ORDER BY total_payment DESC;";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            pstmt.setInt(2, month);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reportList.add(new ArtistPaymentReportDTO(
                    rs.getInt("artist_id"),
                    rs.getString("artist_name"),
                    rs.getFloat("total_payment")
                ));
            }

            artistPaymentReportTable.setItems(reportList);

            if (reportList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No total payments amounts found in the selected month (" + month + "/" + year + ").");
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/AdminReportWindow.fxml")); 
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