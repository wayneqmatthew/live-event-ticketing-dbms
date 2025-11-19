package com.dbms.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.dbms.models.ArtistEvent;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ArtistViewEventsController implements Initializable{
    @FXML
    private TableView<ArtistEvent> artistViewEventTable;
    
    @FXML
    private TableColumn<ArtistEvent, Integer> eventIdViewEventColumn;

    @FXML
    private TableColumn<ArtistEvent, String> eventNameViewEventColumn;

    @FXML
    private TableColumn<ArtistEvent, String> venueNameViewEventColumn;

    @FXML
    private TableColumn<ArtistEvent, String> organizerNameViewEventColumn;

    @FXML
    private TableColumn<ArtistEvent, LocalTime> timeViewEventColumn;

    @FXML
    private TableColumn<ArtistEvent, LocalDate> dateViewEventColumn;

    @FXML
    private TableColumn<ArtistEvent, String> statusViewEventColumn;

    private ObservableList<ArtistEvent> artistViewEventList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        eventIdViewEventColumn.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        eventNameViewEventColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        venueNameViewEventColumn.setCellValueFactory(new PropertyValueFactory<>("venue_name"));
        organizerNameViewEventColumn.setCellValueFactory(new PropertyValueFactory<>("organizer_name"));
        timeViewEventColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateViewEventColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusViewEventColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadCustomerTickets();
    }

    private void loadCustomerTickets(){
        artistViewEventList.clear();

        String sql = "SELECT e.event_id, e.event_name, v.name AS venue_name, o.name AS organizer_name, e.time, e.date, e.status " + 
        "FROM Event e " + 
        "JOIN Venue v " + 
        "ON e.venue_id = v.venue_id " + 
        "JOIN Organizer o " + 
        "ON e.organizer_id = o.organizer_id " + 
        "WHERE e.artist_id = ?";

        try (Connection conn = Database.connect();PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setInt(1, LoginController.getIdNumber());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                artistViewEventList.add(new ArtistEvent(
                    resultSet.getInt("event_id"),
                    resultSet.getString("event_name"),
                    resultSet.getString("venue_name"),
                    resultSet.getString("organizer_name"),
                    resultSet.getTime("time").toLocalTime(),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getString("status")
                ));
            }

            artistViewEventTable.setItems(artistViewEventList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onReturnArtistMenuClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/ArtistWindow.fxml"));
            Stage stage = (Stage) artistViewEventTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to artist menu: " + e.getMessage());
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
