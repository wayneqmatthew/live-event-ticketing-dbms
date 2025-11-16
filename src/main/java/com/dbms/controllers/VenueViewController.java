package com.dbms.controllers;

import com.dbms.models.Venue;
import com.dbms.utils.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory; 
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class VenueViewController implements Initializable{

    @FXML
    private TableView<Venue> venueTable;

    @FXML
    private TableColumn<Venue, String> nameColumn;

    @FXML
    private TableColumn<Venue, Integer> capacityColumn;

    @FXML
    private TableColumn<Venue, String> cityColumn;

    @FXML
    private TableColumn<Venue, String> countryColumn;

    @FXML
    private TableColumn<Venue, String> regionColumn;

    @FXML
    private TableColumn<Venue, String> statusColumn;

    @FXML
    private Button updateButton;

    @FXML 
    private Button deleteButton;

    @FXML 
    private Button backButton;

    private ObservableList<Venue> venueList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadVenues();
    }

    private void loadVenues(){
        venueList.clear();
        String sql = "SELECT venue_id, name, capacity, city, country, region, status FROM VENUE";

        try (Connection conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                venueList.add(new Venue(
                    rs.getInt("venue_id"),
                    rs.getString("name"),
                    rs.getInt("capacity"),
                    rs.getString("city"),
                    rs.getString("country"),
                    rs.getString("region"),
                    rs.getString("status")
                ));
            }

            venueTable.setItems(venueList);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onSetInactiveClick(ActionEvent event){
        Venue selectedVenue = venueTable.getSelectionModel().getSelectedItem();

        if (selectedVenue == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a venue to delete");
            return;
        }

        if (selectedVenue.getStatus().equalsIgnoreCase("Inactive")){
            showAlert(Alert.AlertType.INFORMATION, "Already Inactive", "This venue is already 'Inactive'.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this venue?");
        confirmAlert.setContentText(selectedVenue.getName());

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            String sql = "UPDATE Venue SET status = 'Inactive' WHERE venue_id = ?";

            try (Connection conn = Database.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)){

                    pstmt.setInt(1, selectedVenue.getVenue_id());
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Venue deleted.");
                        loadVenues();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete venue");
                    }
                 } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Error " + e.getMessage());
                    e.printStackTrace();
                 }
        }
    }

    @FXML
    private void onUpdateVenueClick(ActionEvent event){
        Venue selectedVenue = venueTable.getSelectionModel().getSelectedItem();

        if (selectedVenue == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a venue to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/venue-form.fxml"));
            Parent root = loader.load();

            VenueFormController formController = loader.getController();

            formController.initData(selectedVenue);

            Stage stage = new Stage();
            stage.setTitle("Update Venue");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the update form.");
        }
    }

    @FXML
    private void onBackClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/venue-menu.fxml"));

            Stage stage = (Stage) venueTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Venue Management");
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
