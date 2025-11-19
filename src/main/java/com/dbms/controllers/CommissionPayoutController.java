package com.dbms.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.net.URL;
import java.util.ResourceBundle;

import com.dbms.utils.Database;

import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;




// retrive event id and name
// have it be able to do like the select first
// payout connect

public class CommissionPayoutController implements Initializable{
    
    @FXML
    private TableView<EventDTO> eventDTOTable;

    @FXML
    private TableColumn<EventDTO, Integer> eventIdColumn;

    @FXML
    private TableColumn<EventDTO, String> eventNameColumn;

    @FXML
    private TableColumn<EventDTO, String> artistNameColumn;

    @FXML
    private TextField commissionField;

    @FXML
    private Button payoutButton;

    @FXML
    private Button backButton;

    private ObservableList<EventDTO> eventDTOList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("event_id"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artist_name"));

        loadevents();
    }


    private void loadevents(){
        int currentId = LoginController.getIdNumber();
        eventDTOList.clear();
        String sql = "SELECT e.event_id, e.event_name, a.name " + 
                 " FROM event e " +
                 " JOIN artist a ON e.artist_id = a.artist_id " +
                 " LEFT JOIN CommissionPayout cp ON e.event_id = cp.event_id " +
                 " WHERE cp.payout_id IS NULL AND e.organizer_id = ?" +
                 " ORDER BY e.event_name;";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1, currentId);

                try(ResultSet rs = pstmt.executeQuery()){
                    while(rs.next()){
                        eventDTOList.add(new EventDTO(rs.getInt("event_id"), rs.getString("event_name"), rs.getString("name")));
                    }
                    eventDTOTable.setItems(eventDTOList);
                }
            } catch (SQLException e){
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error Loading report" + e.getMessage());
                e.printStackTrace();
            }
    }

    @FXML
    private void onPayoutClick(ActionEvent event){
        EventDTO selectedeventDTO = eventDTOTable.getSelectionModel().getSelectedItem();
        
        double commissionRate;
        String rateText = commissionField.getText().trim();
        
        if (rateText.isEmpty()) {
            commissionRate = 15.0; // SET DEFAULT
        } else {
            try {
                commissionRate = Double.parseDouble(rateText);
                if (commissionRate <= 0 || commissionRate > 100.0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Must be (0-100)");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Should be a number");
                return;
            }
        }

        if(selectedeventDTO == null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a event to update.");
            return;
        }
        new Thread(() -> {
            int art = getArtist_id(selectedeventDTO.getEvent_id());
            int org = getOrganizer_id(selectedeventDTO.getEvent_id());
            if(art != -1 && org != -1){
                addPayoutCommission(art, org, selectedeventDTO.getEvent_id(), commissionRate, LocalDate.now());
            }
        }).start();
        
    }

   private int getOrganizer_id(int event_id){
        
        String sql = "SELECT organizer_id " +
                      "FROM event " +
                      "WHERE event_id = ?;";
        try(Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, event_id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("organizer_id");
                }
            };
            
        }catch (SQLException e){
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error Loading report" + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    private int getArtist_id(int event_id){
        
        String sql = "SELECT artist_id " +
                      "FROM event " +
                      "WHERE event_id = ?;";
        try(Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, event_id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("artist_id");
                }
            };
            
        }catch (SQLException e){
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error Loading report" + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }


    private void addPayoutCommission(int artist_id, int organizer_id, int event_id, double commission_percentage, LocalDate payout_date){
        String sql = "INSERT INTO commissionpayout (artist_id, organizer_id, event_id, commission_percentage, payout_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, artist_id);
                pstmt.setInt(2, organizer_id);
                pstmt.setInt(3, event_id);
                pstmt.setDouble(4, commission_percentage);
                pstmt.setString(5, payout_date.toString());

                int rowsAffected = pstmt.executeUpdate();
                Platform.runLater(() ->{
                    if (rowsAffected > 0){
                        loadevents();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Commission Payout successfully updated!");
                    
                    // closeWindow();
                    } else { 
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Comission Payout to update organizer.");
                    }
                });
                
             } catch (SQLException e){
                Platform.runLater(() ->{
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
                });
                
                e.printStackTrace();
             }
    }

    @FXML
    private void onReturnToMenuClick(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/view/OrganizerWindow.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
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
