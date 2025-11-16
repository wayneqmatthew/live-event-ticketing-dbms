package com.dbms;

import com.dbms.controllers.VenueMenuController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application{
    public static void main(String[] args){
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/venue-menu.fxml"));

        VenueMenuController venueMenuController = new VenueMenuController();

        loader.setController(venueMenuController);
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DMBS");
        primaryStage.show();
    }
}