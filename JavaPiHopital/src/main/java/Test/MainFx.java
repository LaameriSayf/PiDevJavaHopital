package Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontEnd.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 550);
        primaryStage.setTitle("Ajouter ordonnance");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
