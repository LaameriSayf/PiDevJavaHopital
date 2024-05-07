package org.example.client;

import javafx.application.Application;
import  javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        Parent root =FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage.setTitle("server");
        stage.setScene(new Scene(root, 478 , 396));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
