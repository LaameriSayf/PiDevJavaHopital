package Controller;

import Test.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrontEnd implements Initializable {
    @FXML
    private Button btnDossiersMedicaux;

    @FXML
    private Button btnOrdonnances;
    @FXML
    private Button btn1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Définir les actions des boutons
        btnDossiersMedicaux.setOnAction(event -> openAllDossier());
        btnOrdonnances.setOnAction(event -> openAllOrdonnance());
    }

    private void openAllDossier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllDossier.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openAllOrdonnance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllOrdonnance.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private Button logout_btn;
    @FXML
    void logout(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
