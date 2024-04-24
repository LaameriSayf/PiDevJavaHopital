package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuDashboardLayout implements Initializable {
    @FXML
    private AnchorPane sidebar;



    public void DarkMode() {
        // Ajoutez ou supprimez des classes CSS pour basculer entre le mode sombre et le mode clair
            sidebar.getStyleClass().add("dark-mode");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}