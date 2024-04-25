package tn.esprit.applicationmilitaire.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.applicationmilitaire.test.HelloApplication;
import tn.esprit.applicationmilitaire.utils.MyConnection;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class Login {


    @FXML
    private Button close;

    @FXML
    private TextField emailTF;

    @FXML
    private Button minimise;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Button login;
    @FXML
    private AnchorPane main_form;


    // Maintenir la connexion ouverte (Option 1)
    private Connection connect = MyConnection.getInstance().getCnx();

    // Option 2:
    // private Connection connect;

    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    public void login(ActionEvent event) {
        try {
            if (connect == null || connect.isClosed()) {
                connect = MyConnection.getInstance().getCnx();
            }

            if (emailTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
                // Champs vides
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Champs vides");
                alert.setContentText("Veuillez entrer votre email et votre mot de passe.");
                alert.showAndWait();
                return;
            }

            String sqlLogin = "SELECT * FROM global_user WHERE email = ?";
            prepare = connect.prepareStatement(sqlLogin);
            prepare.setString(1, emailTF.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                String hashedPasswordFromDB = result.getString("password");
                if (BCrypt.checkpw(passwordTF.getText(), hashedPasswordFromDB)) {
                    // Login successful
                    String role = result.getString("role");
                    FXMLLoader loader = new FXMLLoader();

                    switch (role) {
                        case "Admin":
                            loader.setLocation(getClass().getResource("/Dashboard.fxml"));
                            break;
                        case "patient":
                            loader.setLocation(getClass().getResource("PatientDashboard.fxml"));
                            break;
                        case "medecin":
                            loader.setLocation(getClass().getResource("MedecinDashboard.fxml"));
                            break;
                        case "pharmacien":
                            loader.setLocation(getClass().getResource("PharmacienDashboard.fxml"));
                            break;
                        default:
                            // Role invalide
                            Alert invalidRoleAlert = new Alert(Alert.AlertType.ERROR);
                            invalidRoleAlert.setTitle("Erreur");
                            invalidRoleAlert.setHeaderText("Rôle invalide");
                            invalidRoleAlert.setContentText("Le rôle de cet utilisateur est invalide.");
                            invalidRoleAlert.showAndWait();
                            return;
                    }

                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // Mauvais mot de passe
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Connexion échouée");
                    alert.setContentText("Email ou mot de passe invalide.");
                    alert.showAndWait();
                }
            } else {
                // Utilisateur non trouvé
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Connexion échouée");
                alert.setContentText("Utilisateur non trouvé.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Erreur de la base de données ou de chargement de l'interface
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de base de données ou de chargement de l'interface");
            alert.setContentText("Une erreur s'est produite lors de la connexion ou du chargement de l'interface.");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Fermer la connexion, le PreparedStatement et le ResultSet
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private Button inscri_btn;
    @FXML
    void inscription_btn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/inscription.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) inscri_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }






    @FXML
    public void close() {
        System.exit(0);

    }

    @FXML
    public void minimise() {
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);

    }
    public void initialize() throws SQLException {

    }
}

