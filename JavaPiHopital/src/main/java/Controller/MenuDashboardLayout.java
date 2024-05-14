package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

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