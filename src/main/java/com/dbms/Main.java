package com.dbms;

import com.dbms.models.Organizer;

import javafx.application.Application;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class Main extends Application{
    public static void main(String[] args){
        
        // organizer testing for connection to db
        // adds new organizer
        Organizer org = new Organizer("Grand Corp.", "grand_corp@gmail.com", "London", "United Kingdom", "Europe", "Active");
        org.add_organizer();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/dbms/views/commission_payout.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("DMBS");
        stage.show();
    }
}