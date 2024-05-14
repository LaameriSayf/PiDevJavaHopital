package Controller;

import Test.HelloApplication;
import Util.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private String emailToEdit;

    public void setEmail(String email){
        emailToEdit = email;
    }
    private String nomToEdit;

    public void setNom(String nom){
        nomToEdit = nom;
    }

    private Integer CinToEdit;
    public void setCin(Integer Cin){
        CinToEdit = Cin;
    }


    @FXML
    public void initialize() {
        // Connexion à la base de données
        Connection connect = DataBase.getInstance().getCnx();
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
                int totalPatients = resultSetPatient.getInt("total");
                totalPatientLabel.setText("Total des patients : " + totalPatients);
            }

            // Compter le nombre total des patients
            PreparedStatement statementPharmacien = connect.prepareStatement(sqlPharmacien);
            ResultSet resultSetPharmacien= statementPharmacien.executeQuery();
            if (resultSetPharmacien.next()) {
                int totalPharmaciens = resultSetPharmacien.getInt("total");
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
            AjouterAdmin ajouterAdminController  = fxmlLoader.getController();
            ajouterAdminController.setEmail(emailToEdit);
            ajouterAdminController.setCin(CinToEdit);
            ajouterAdminController.setNom(nomToEdit);

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
    @FXML
    private Button editprofil;
    @FXML
    public void editprofil(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/EditProfil.fxml"));
            Parent root = fxmlLoader.load();
            EditProfil editController = fxmlLoader.getController();
            editController.setEmail(emailToEdit);
            editController.setCin(CinToEdit);
            editController.setNom(nomToEdit);
            Stage stage = (Stage) editprofil.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private Button gererRdv;
    @FXML
    public void gererrendezvous(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterRdv.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererRdv.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button gererMedic;

    @FXML
    public void gererMedic(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterCategorie.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererMedic.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private Button gererEmploi;

    @FXML
    public void gererEmploi(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/GestionEmploi.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererEmploi.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button gererordonnance;

    @FXML
    public void gererordonnance(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AllOrdonnance.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererordonnance.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button gererdossier;

    @FXML
    public void gererdossier(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AllDossier.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererdossier.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private Button gererblog_btn;

    @FXML
    void gererblogbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/GUI/MenuDashboardLayout.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererblog_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }












}
