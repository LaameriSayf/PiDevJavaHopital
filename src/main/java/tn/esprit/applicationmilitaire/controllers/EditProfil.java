package tn.esprit.applicationmilitaire.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.applicationmilitaire.models.Global_user;
import java.sql.Timestamp;
import java.time.LocalDate;
import tn.esprit.applicationmilitaire.utils.MyConnection;

import java.sql.*;

public class EditProfil {



    @FXML
    private Button annulerbtn;

    @FXML
    private TextField cinTF;

    @FXML
    private Button close;

    @FXML
    private DatePicker datedenaissanceTF;

    @FXML
    private TextField emailTF;

    @FXML
    private Button enregistrerbtn;

    @FXML
    private RadioButton genreTF;

    @FXML
    private RadioButton genreTF1;

    @FXML
    private Button imageTF;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimise;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numtelTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private TextField prenomTF;
    private Global_user currentUser;

    @FXML
    void annuler(ActionEvent event) {

    }




    @FXML
    void editimage(ActionEvent event) {

    }

    @FXML
    void enregistrer(ActionEvent event) {

    }


    public void close(){
        System.exit(0);
    }
    public void minimise(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }




}
