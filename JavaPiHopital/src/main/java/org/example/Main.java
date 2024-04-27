package org.example;

import Model.CtegorieBlog;
import Service.CategoriBlogService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    //  FXMLLoader loader=new FXMLLoader(getClass().getResource("/GUI/MenuDashboardLayout.fxml"));
       //
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/GUI/FrontOfficeBlog.fxml"));

        try {
            Parent root = loader.load();
            Scene scene =new Scene(root);
            primaryStage.setTitle("Gestion Blog");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
