package tn.esprit.applicationmilitaire.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Hyperlink MDPoublie;

    @FXML
    private PasswordField passwordTF;
    @FXML
    private CheckBox showpassword;


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
            Connection connect = MyConnection.getInstance().getCnx();

            if (emailTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
                // Champs vides
                showAlert("Erreur", "Champs vides", "Veuillez entrer votre email et votre mot de passe.");
                return;
            }

            String sqlLogin = "SELECT * FROM global_user WHERE email = ?";
            PreparedStatement prepare = connect.prepareStatement(sqlLogin);
            prepare.setString(1, emailTF.getText());
            ResultSet result = prepare.executeQuery();

            if (result.next()) {
                String hashedPasswordFromDB = result.getString("password");
                boolean interlock = result.getBoolean("interlock"); // Vérification de l'interlock

                if (interlock) {
                    showAlert("Compte bloqué", "Votre compte est bloqué", "Vous ne pouvez pas utiliser votre compte actuellement.");
                    return; // Arrête le processus de connexion
                }

                if (BCrypt.checkpw(passwordTF.getText(), hashedPasswordFromDB)) {
                    // Login successful
                    String role = result.getString("role");
                    FXMLLoader loader = new FXMLLoader();
                    Parent root;

                    switch (role) {
                        case "Admin":
                            root = loader.load(getClass().getResource("/Dashboard.fxml").openStream());
                            break;
                        case "Patient":
                            root = loader.load(getClass().getResource("/PatientDashboard.fxml").openStream());
                            break;
                        case "Medecin":
                            root = loader.load(getClass().getResource("/MedecinDashboard.fxml").openStream());
                            break;
                        case "Pharmacien":
                            root = loader.load(getClass().getResource("/PharmacienDashboard.fxml").openStream());
                            break;
                        default:
                            // Role invalide
                            showAlert("Erreur", "Rôle invalide", "Le rôle de cet utilisateur est invalide.");
                            return;
                    }

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // Mauvais mot de passe
                    showAlert("Erreur", "Connexion échouée", "Email ou mot de passe invalide.");
                }
            } else {
                // Utilisateur non trouvé
                showAlert("Erreur", "Connexion échouée", "Utilisateur non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de base de données", "Une erreur s'est produite lors de la connexion à la base de données.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de chargement", "Une erreur s'est produite lors du chargement de l'interface utilisateur.");
        }
    }

    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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
    @FXML
    private TextField loginshowpassword;

    @FXML
    void showPassword(ActionEvent event) {
        if (showpassword.isSelected()) {
            if (loginshowpassword != null && passwordTF != null) {
                loginshowpassword.setText(passwordTF.getText());
                loginshowpassword.setVisible(true);
                passwordTF.setVisible(false);
            }
        } else {
            if (loginshowpassword != null && passwordTF != null) {
                passwordTF.setText(loginshowpassword.getText());
                loginshowpassword.setVisible(false);
                passwordTF.setVisible(true);
            }
        }
    }

    @FXML
    void MDPoubliebtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/forgotPassword.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) MDPoublie.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

