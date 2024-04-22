package tn.esprit.applicationmilitaire.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.applicationmilitaire.utils.MyConnection;




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
    public void login(ActionEvent event) throws SQLException {
        // Restoring connection if necessary
        if (connect == null || connect.isClosed()) {
            connect = MyConnection.getInstance().getCnx();
        }

        // Checking for empty fields
        if (emailTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs vides");
            alert.setContentText("Veuillez entrer votre email et votre mot de passe.");
            alert.showAndWait();
            return;
        }

        // SQL query to retrieve user with matching email
        String sqlLogin = "SELECT * FROM global_user WHERE email = ?";

        try {
            prepare = connect.prepareStatement(sqlLogin);
            prepare.setString(1, emailTF.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                // Retrieving hashed password from the database
                String hashedPasswordFromDB = result.getString("password");

                // Comparing hashed password from DB with entered password
                if (BCrypt.checkpw(passwordTF.getText(), hashedPasswordFromDB)) {
                    // Login successful
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Connexion réussie");
                    alert.setContentText("Bienvenue!");
                    alert.showAndWait();

                    // Implement logic to handle successful login
                } else {
                    // Login failed - incorrect password
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("La connexion a échoué");
                    alert.setContentText("Email ou mot de passe invalide.");
                    alert.showAndWait();
                }
            } else {
                // Login failed - user not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("La connexion a échoué");
                alert.setContentText("Utilisateur non trouvé");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Erreur de la base de données");
            alert.setContentText("Une erreur s'est produite lors de la connexion à la base de données.");
            alert.showAndWait();
        } finally {
            // Closing resources
            if (result != null) {
                result.close();
            }
            if (prepare != null) {
                prepare.close();
            }
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

