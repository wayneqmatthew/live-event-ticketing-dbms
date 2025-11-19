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

import javafx.scene.Node;

import com.dbms.models.Artist;
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

public class AdminArtistManagementController implements Initializable {
    @FXML
    private TableView<Artist> artistTable;

    @FXML
    private TableColumn<Artist, Integer> artistIdColumn;

    @FXML
    private TableColumn<Artist, String> nameColumn;

    @FXML
    private TableColumn<Artist, String> emailColumn;

    @FXML
    private TableColumn<Artist, String> genreColumn;

    @FXML
    private TableColumn<Artist, String> companyColumn;

    @FXML
    private TableColumn<Artist, String> statusColumn;

    private ObservableList<Artist> artistList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        artistIdColumn.setCellValueFactory(new PropertyValueFactory<>("artist_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("management_company"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadArtists();
    }

    private void loadArtists(){
        artistList.clear();
        String sql = "SELECT artist_id, name, email, genre, management_company, status FROM ARTIST";

        try (Connection conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                artistList.add(new Artist(
                    rs.getInt("artist_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("genre"),
                    rs.getString("management_company"),
                    rs.getString("status")
                ));
            }

            artistTable.setItems(artistList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteClick(ActionEvent event){
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();

        if (selectedArtist == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an artist to Inactive");
            return;
        }

        if (selectedArtist.getStatus().equalsIgnoreCase("Inactive")){
            showAlert(Alert.AlertType.INFORMATION, "Already Inactive", "This artist is already 'Inactive'.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this artist?");
        confirmAlert.setContentText(selectedArtist.getName());

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            String sql = "UPDATE Artist SET status = 'Inactive' WHERE artist_id = ?";

            try (Connection conn = Database.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)){

                    pstmt.setInt(1, selectedArtist.getArtist_id());
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0){
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Artist deleted.");
                        loadArtists();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete artist.");
                    }
                 } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
                 }
        }
    }

    @FXML
    private void onUpdateClick(ActionEvent event){
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();

        if (selectedArtist == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an artist to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminArtistManagementUpdateWindow.fxml"));
            Parent root = loader.load();

            AdminArtistManagementUpdateController formUpdateController = loader.getController();

            formUpdateController.initData(selectedArtist);

            Stage stage = new Stage();
            Image logo = new Image("com/dbms/view/assets/logo.png");

            stage.getIcons().add(logo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadArtists());
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
            Stage stage = (Stage) artistTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to main menu: " + e.getMessage());
        }
    }

    @FXML
    private void onAddClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminArtistManagementAddWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Image logo = new Image("com/dbms/view/assets/logo.png");

            stage.getIcons().add(logo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(e -> loadArtists());
            stage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the add artist management: " + e.getMessage());
        }
    }

    @FXML
    private void onViewClick(ActionEvent event){
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();

        if (selectedArtist == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a artist to view.");
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminArtistManagementViewWindow.fxml"));
            Parent root = loader.load();

            AdminArtistManagementViewController formUpdateController = loader.getController();

            formUpdateController.initData(selectedArtist);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch(IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the view artist: " + e.getMessage());
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
