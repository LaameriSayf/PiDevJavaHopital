package tn.esprit.applicationmilitaire.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.applicationmilitaire.models.Global_user;
import tn.esprit.applicationmilitaire.services.Global_userService;
import tn.esprit.applicationmilitaire.utils.EmailUser;

import java.sql.SQLException;

public class ForgotPassword {

    @FXML
    private TextField emailTF;

    @FXML
    private Button envoyerbtn;

    public void  resetPassword() throws SQLException {
        String email = emailTF.getText();
        if (email.isEmpty()) {
            showAlert("Veuillez entrer votre adresse e-mail.", Alert.AlertType.WARNING);
            return;
        }

        // Générer un mot de passe par défaut
        String defaultPassword = generateDefaultPassword();
        String hashedPassword = BCrypt.hashpw(defaultPassword, BCrypt.gensalt());
        Global_userService us = new Global_userService();
        Global_user user = us.getEventByEmail(email); // Assurez-vous d'avoir cette méthode
        if (user != null) {
            user.setPassword(hashedPassword); // Mettre à jour le mot de passe de l'utilisateur
            us.updateGlobal_user(user); // Mettre à jour l'utilisateur dans la base de données
        } else {
            showAlert("L'utilisateur avec l'adresse e-mail " + email + " n'a pas été trouvé.", Alert.AlertType.ERROR);
        }


        // Envoyer un e-mail avec le mot de passe par défaut
        String subject = "Réinitialisation de votre mot de passe";
        String message = "Votre mot de passe a été réinitialisé. Voici votre nouveau mot de passe : " + defaultPassword;
        EmailUser.sendEmailU(email, subject, message);

        showAlert("Un e-mail avec votre nouveau mot de passe a été envoyé à " + email + ".", Alert.AlertType.INFORMATION);

        // Après l'envoi de l'e-mail

    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String generateDefaultPassword() {
// Générer un mot de passe aléatoire
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int passwordLength = 10; // Longueur du mot de passe
        StringBuilder password = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int index = (int) (alphabet.length() * Math.random());
            password.append(alphabet.charAt(index));
        }
        return password.toString();
    }
}
