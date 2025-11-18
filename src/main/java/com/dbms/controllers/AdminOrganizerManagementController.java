package com.dbms.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import com.dbms.models.Organizer;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminOrganizerManagementController implements Initializable {
    @FXML
    private TableView<Organizer> organizerTable;

    @FXML
    private TableColumn<Organizer, Integer> organizerIdColumn;

    @FXML
    private TableColumn<Organizer, String> nameColumn;

    @FXML
    private TableColumn<Organizer, Integer> emailColumn;

    @FXML
    private TableColumn<Organizer, String> cityColumn;

    @FXML
    private TableColumn<Organizer, String> countryColumn;

    @FXML
    private TableColumn<Organizer, String> regionColumn;

    @FXML
    private TableColumn<Organizer, String> statusColumn;

    private ObservableList<Organizer> organizerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        organizerIdColumn.setCellValueFactory(new PropertyValueFactory<>("organizer_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadOrganizers();
    }

    private void loadOrganizers(){
        organizerList.clear();
        String sql = "SELECT organizer_id, name, email, city, country, region, status FROM ORGANIZER";

        try (Connection conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                organizerList.add(new Organizer(
                    rs.getInt("organizer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("country"),
                    rs.getString("region"),
                    rs.getString("status")
                ));
            }

            organizerTable.setItems(organizerList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteClick(ActionEvent event){
        Organizer selectedOrganizer = organizerTable.getSelectionModel().getSelectedItem();

        if (selectedOrganizer == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a organizer to Inactive");
            return;
        }

        if (selectedOrganizer.getStatus().equalsIgnoreCase("Inactive")){
            showAlert(Alert.AlertType.INFORMATION, "Already Inactive", "This organizer is already 'Inactive'.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this organizer?");
        confirmAlert.setContentText(selectedOrganizer.getName());

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            String sql = "UPDATE Organizer SET status = 'Inactive' WHERE organizer_id = ?";

            try (Connection conn = Database.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)){

                    pstmt.setInt(1, selectedOrganizer.getOrganizer_id());
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Organizer deleted.");
                        loadOrganizers();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete organizer.");
                    }
                 } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
                 }
        }
    }

    @FXML
    private void onUpdateClick(ActionEvent event){
        Organizer selectedOrganizer = organizerTable.getSelectionModel().getSelectedItem();

        if (selectedOrganizer == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a organizer to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminOrganizerManagementUpdateWindow.fxml"));
            Parent root = loader.load();

            AdminOrganizerManagementUpdateController formUpdateController = loader.getController();

            formUpdateController.initData(selectedOrganizer);

            Stage stage = new Stage();
            Image logo = new Image("com/dbms/view/assets/logo.png");

            stage.getIcons().add(logo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadOrganizers());
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
            Stage stage = (Stage) organizerTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to main menu: " + e.getMessage());
        }
    }

    @FXML
    private void onAddClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminOrganizerManagementAddWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Image logo = new Image("com/dbms/view/assets/logo.png");

            stage.getIcons().add(logo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(e -> loadOrganizers());
            stage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the add organizer management: " + e.getMessage());
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
