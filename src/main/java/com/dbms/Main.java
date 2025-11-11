package com.dbms;

import javafx.application.Application;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DMBS");

        Text text = new Text();
        text.setText("Main Page");
        text.setX(50);
        text.setY(50);

        root.getChildren().add(text);

        primaryStage.show();
    }
}