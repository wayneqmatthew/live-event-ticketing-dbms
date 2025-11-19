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

import com.dbms.models.Artist;
import com.dbms.models.Event;
import com.dbms.models.Venue;
import com.dbms.models.Organizer;
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
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class AdminOrganizerManagementViewController implements Initializable{
    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, Integer> event_idColumn;

    @FXML
    private TableColumn<Event, Integer> venue_idColumn;

    @FXML
    private TableColumn<Event, Integer> organizer_idColumn;

    @FXML
    private TableColumn<Event, String> eventNameColumn;

    @FXML
    private TableColumn<Event, LocalTime> timeColumn;

    @FXML
    private TableColumn<Event, LocalDate> dateColumn;

    @FXML
    private TableColumn<Event, Integer> capacityColumn;

    @FXML
    private TableColumn<Event, String> statusColumn;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();

    private int selectedOrganizerId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        event_idColumn.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        venue_idColumn.setCellValueFactory(new PropertyValueFactory<>("venue_id"));
        organizer_idColumn.setCellValueFactory(new PropertyValueFactory<>("organizer_id"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadEvents(){
        eventList.clear();
        String sql = "SELECT e.event_id, e.venue_id, e.artist_id, e.organizer_id, e.event_name, e.time, e.date, e.capacity, e.ticket_price, e.status FROM Organizer o JOIN Event e ON o.organizer_id = e.organizer_id WHERE o.organizer_id = ?";

        try (Connection conn = Database.connect();PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setInt(1, selectedOrganizerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                eventList.add(new Event(
                    resultSet.getInt("event_id"),
                    resultSet.getInt("venue_id"),
                    resultSet.getInt("artist_id"),
                    resultSet.getInt("organizer_id"),
                    resultSet.getString("event_name"),
                    resultSet.getTime("time").toLocalTime(),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getInt("capacity"),
                    resultSet.getFloat("ticket_price"),
                    resultSet.getString("status")
                ));
            }

            eventTable.setItems(eventList);

        } catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load database: " + e.getMessage());
        }
    }

    @FXML
    private void onReturnOrganizerManagementClick(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/AdminOrganizerManagementWindow.fxml"));
            Stage stage = (Stage) eventTable.getScene().getWindow();
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

    public void initData(Organizer selectedOrganizer){
        selectedOrganizerId = selectedOrganizer.getOrganizer_id();
        loadEvents();
    }
}