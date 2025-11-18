package com.dbms.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.dbms.utils.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;

public class LoginController implements Initializable{
    @FXML
    private Pane rootPane;

    @FXML
    private TextField identifierField;

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<String> userTypeChoiceBox;

    private String identifier;
    private String password;
    

    private final String adminUsername = "admin";
    private final String adminPassword = "admin";

    private final String[] userTypeCollection = {"CUSTOMER", "ARTIST", "ORGANIZER", "ADMIN"};
    private String userType;

    private int organizerId;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        userTypeChoiceBox.getItems().addAll(userTypeCollection);
        userTypeChoiceBox.setOnAction(this::userTypeChoice);
    }

    public void userTypeChoice(ActionEvent event){
        userType = userTypeChoiceBox.getValue();
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }


    @FXML
    private void onSubmitClick(ActionEvent event){
        try{

            identifier = identifierField.getText();
            password = passwordField.getText();

            switch(userType){
                case "ADMIN":
                    if(identifier.equals(adminUsername) && password.equals(adminPassword)){
                        FXMLLoader loaderAdmin = new FXMLLoader(getClass().getResource("/com/dbms/view/AdminWindow.fxml"));
                        Parent rootAdmin = loaderAdmin.load();
                        Scene sceneAdmin = new Scene(rootAdmin);
                        Stage primaryStageAdmin = (Stage) rootPane.getScene().getWindow();

                        primaryStageAdmin.setScene(sceneAdmin);
                    }
                    else{
                        showAlert(AlertType.ERROR, "Error", "Failed to authenticate.");
                        return;
                    }
                    break;
                
                case "ORGANIZER":
                    if (authenticateOrganizer(identifier) && password.equals(adminPassword)) {
                        FXMLLoader loaderOrganizer = new FXMLLoader(getClass().getResource("/com/dbms/view/OrganizerWindow.fxml"));
                        Parent rootOrganizer = loaderOrganizer.load();

                        OrganizerController controller = loaderOrganizer.getController();
                        controller.setOrganizerId(Integer.parseInt(identifier));

                        Scene sceneOrganizer = new Scene(rootOrganizer);
                        Stage primaryStageOrganizer = (Stage) rootPane.getScene().getWindow();
                        primaryStageOrganizer.setScene(sceneOrganizer);
                    } else {
                        showAlert(AlertType.ERROR, "Error", "Failed to authenticate.");
                        return;
                    }
                    break;
                
                case "ARTIST":
                    if(authenticateArtist(identifier) && password.equals(adminPassword)){
                        FXMLLoader loaderArtist = new FXMLLoader(getClass().getResource("/com/dbms/view/ArtistWindow.fxml"));
                        Parent rootArtist = loaderArtist.load();
                        Scene sceneArtist = new Scene(rootArtist);
                        Stage primaryStageArtist = (Stage) rootPane.getScene().getWindow();

                        primaryStageArtist.setScene(sceneArtist);
                    }
                    else{
                        showAlert(AlertType.ERROR, "Error", "Failed to authenticate.");
                        return;
                    }
                    break;
                
                case "CUSTOMER":
                    if(authenticateCustomer(identifier) && password.equals(adminPassword)){
                        FXMLLoader loaderCustomer = new FXMLLoader(getClass().getResource("/com/dbms/view/CustomerWindow.fxml"));
                        Parent rootCustomer = loaderCustomer.load();
                        Scene sceneCustomer = new Scene(rootCustomer);
                        Stage primaryStageCustomer = (Stage) rootPane.getScene().getWindow();

                        primaryStageCustomer.setScene(sceneCustomer);
                    }
                    else{
                        showAlert(AlertType.ERROR, "Error", "Failed to authenticate.");
                        return;
                    }
                    break;
                
                default:
                    showAlert(AlertType.ERROR, "Error", "Invalid user type.");
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to log in: " + e.getMessage());
        }
    }

    private boolean authenticateOrganizer(String identifierOrganizer){
        String organizerSql = "SELECT * FROM Organizer WHERE organizer_id = ?";

        try (Connection conn = Database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(organizerSql)){
            
            preparedStatement.setString(1, identifierOrganizer);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean authenticateArtist(String identifierArtist){
        String artistSql = "SELECT * FROM Artist WHERE artist_id = ?";

        try (Connection conn = Database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(artistSql)){
            
            preparedStatement.setString(1, identifierArtist);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean authenticateCustomer(String identifierCustomer){
        String customerSql = "SELECT * FROM Customer WHERE customer_id = ?";

        try (Connection conn = Database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(customerSql)){
            
            preparedStatement.setString(1, identifierCustomer);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getIdentifier(){
        return identifier;
    }
}
