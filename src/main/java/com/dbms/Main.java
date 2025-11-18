package com.dbms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class Main extends Application{
    public static void main(String[] args){
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Font.loadFont(getClass().getResourceAsStream("/com/dbms/view/assets/Inter_18pt-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/dbms/view/assets/Inter_18pt-Black.ttf"), 14);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbms/view/OrganizerWindow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Image logo = new Image("com/dbms/view/assets/logo.png");

        primaryStage.getIcons().add(logo);
        primaryStage.setTitle("Live Ticketing DBMS");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}