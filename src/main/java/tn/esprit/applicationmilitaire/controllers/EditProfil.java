package tn.esprit.applicationmilitaire.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.applicationmilitaire.models.Global_user;
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
    private Button imageTF;

    @FXML
    private Button minimise;

    @FXML
    private TextField motdepasseTF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numtelTF;

    @FXML
    private TextField prenomTF;
    private Global_user currentUser;


    private void retrieveUserDataFromDatabase(String email) {
        try {
            Connection connect = MyConnection.getInstance().getCnx();

            String sqlQuery = "SELECT * FROM global_user WHERE email = ?";
            PreparedStatement prepare = connect.prepareStatement(sqlQuery);
            prepare.setString(1, email);
            ResultSet result = prepare.executeQuery();

            if (result.next()) {
                currentUser = new Global_user(
                        result.getInt("id"),
                        result.getInt("cin"),
                        result.getInt("numtel"),
                        result.getInt("genre"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("image"),
                        result.getString("role"),
                        result.getTimestamp("dateNaissance"),
                        result.getBoolean("interlock")
                );

                fillFormFields();
            } else {
                showAlert("Erreur", "Utilisateur non trouvé", "Aucun utilisateur trouvé avec cet email.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de base de données", "Une erreur s'est produite lors de la récupération des données de l'utilisateur.");
        }
    }

    private void fillFormFields() {
        cinTF.setText(String.valueOf(currentUser.getCin()));
        numtelTF.setText(String.valueOf(currentUser.getNumtel()));
        nomTF.setText(currentUser.getNom());
        prenomTF.setText(currentUser.getPrenom());
        // Remplissez les autres champs du formulaire
    }

    @FXML
    public void enregistrer() {
        // Mettez à jour les données de l'utilisateur dans la base de données en utilisant currentUser
        // Vous devrez implémenter cette méthode
        updateUserDataInDatabase();

        // Affichez un message de confirmation ou effectuez d'autres actions si nécessaire
    }

    private void updateUserDataInDatabase() {
        try {
            Connection connect = MyConnection.getInstance().getCnx();

            String sqlUpdate = "UPDATE global_user SET cin = ?, numtel = ?, nom = ?, prenom = ?, dateNaissance = ? WHERE id = ?";
            PreparedStatement prepare = connect.prepareStatement(sqlUpdate);

            // Remplacez les "?" par les valeurs actuelles des champs du formulaire
            prepare.setInt(1, Integer.parseInt(cinTF.getText()));
            prepare.setInt(2, Integer.parseInt(numtelTF.getText()));
            prepare.setString(3, nomTF.getText());
            prepare.setString(4, prenomTF.getText());
            prepare.setTimestamp(5, Timestamp.valueOf(datedenaissanceTF.getValue().atStartOfDay()));

            // Utilisez l'identifiant de l'utilisateur pour la clause WHERE
            prepare.setInt(6, currentUser.getId());

            // Exécutez la mise à jour
            int rowsAffected = prepare.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Succès", "Données mises à jour", "Les données de l'utilisateur ont été mises à jour avec succès.");
            } else {
                showAlert("Erreur", "Échec de la mise à jour", "La mise à jour des données de l'utilisateur a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de base de données", "Une erreur s'est produite lors de la mise à jour des données de l'utilisateur.");
        }
    }
    private void showAlert (String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
