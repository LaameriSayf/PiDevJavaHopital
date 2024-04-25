package tn.esprit.applicationmilitaire.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.applicationmilitaire.test.HelloApplication;
import tn.esprit.applicationmilitaire.utils.MyConnection;

public class Dashboard {
    @FXML
    private AnchorPane hometotaladmin;

    @FXML
    private AnchorPane hometotalmedecin;

    @FXML
    private AnchorPane hometotalpatient;

    @FXML
    private AnchorPane hometotalpharmacien;

    @FXML
    private Label totalMedecinLabel;

    @FXML
    private Label totalPatientLabel;

    @FXML
    private Label totalPharmacienLabel;
    @FXML
    private Label totalAdminLabel;
    private Connection connect;

    @FXML
    public void initialize() {
        // Connexion à la base de données
        Connection connect = MyConnection.getInstance().getCnx();
        String sqlAdmin = "SELECT COUNT(*) AS total FROM admin";
        String sqlMedecin = "SELECT COUNT(*) AS total FROM medecin";
        String sqlPatient = "SELECT COUNT(*) AS total FROM patient";
        String sqlPharmacien = "SELECT COUNT(*) AS total FROM pharmacien";

        try {
            // Compter le nombre total des admins
            PreparedStatement statementAdmin = connect.prepareStatement(sqlAdmin);
            ResultSet resultSetAdmin = statementAdmin.executeQuery();
            if (resultSetAdmin.next()) {
                int totalAdmins = resultSetAdmin.getInt("total");
                totalAdminLabel.setText("Total des admins : " + totalAdmins);
            }

            // Compter le nombre total des médecins
            PreparedStatement statementMedecin = connect.prepareStatement(sqlMedecin);
            ResultSet resultSetMedecin = statementMedecin.executeQuery();
            if (resultSetMedecin.next()) {
                int totalMedecins = resultSetMedecin.getInt("total");
                totalMedecinLabel.setText("Total des médecins : " + totalMedecins);
            }
            // Compter le nombre total des patients
            PreparedStatement statementPatient = connect.prepareStatement(sqlPatient);
            ResultSet resultSetPatient = statementPatient.executeQuery();
            if (resultSetPatient.next()) {
                int totalPatients = resultSetMedecin.getInt("total");
                totalPatientLabel.setText("Total des patients : " + totalPatients);
            }

            // Compter le nombre total des patients
            PreparedStatement statementPharmacien = connect.prepareStatement(sqlPharmacien);
            ResultSet resultSetPharmacien= statementPharmacien.executeQuery();
            if (resultSetPharmacien.next()) {
                int totalPharmaciens = resultSetMedecin.getInt("total");
                totalPharmacienLabel.setText("Total des Pharmaciens : " + totalPharmaciens);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur de manière appropriée
        }
    }



    @FXML
    private Button gererpatientbtn;

    @FXML
    public void gererpatientbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterPatient.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererpatientbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button gereradministrateurbtn;

    @FXML
    private Button gerermedecinbtn;

    @FXML
    private Button gererpharmacienbtn;

    @FXML
    private Button tableauboardbtn;

    @FXML
    public void tableauboardbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Dashboard.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) tableauboardbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void gereradministrateurbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterAdmin.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gereradministrateurbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void gerermedecin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterMedecin.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gerermedecinbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void gererpharmacienbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterPharmacien.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererpharmacienbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void logout(ActionEvent actionEvent) {

    }
}
