package tn.esprit.applicationmilitaire.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.applicationmilitaire.models.getData;
import org.mindrot.jbcrypt.BCrypt;


import javafx.scene.image.ImageView;
import tn.esprit.applicationmilitaire.utils.MyConnection;

import java.io.File;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Inscription {
    @FXML
    private TextField cinTF;

    @FXML
    private Button close;

    @FXML
    private DatePicker date_de_naissanceTF;

    @FXML
    private TextField emailTF;
    @FXML
    private TextField interlockTF;
    @FXML
    private PasswordField passwordTF;


    @FXML
    private CheckBox genreTF;

    @FXML
    private CheckBox genreTF1;
    @FXML
    private ImageView imageTF;

    @FXML
    private Button inscription_insérerBtn;

    @FXML
    private Button minimise;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numcarteTF;

    @FXML
    private TextField numtelTF;


    @FXML
    private TextField prenomTF;

    @FXML
    private TextField roleTF;
    @FXML
    private AnchorPane main_form;



    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;



     public void inscriptionInsertImage() {

            FileChooser open = new FileChooser();
            File file = open.showOpenDialog(main_form.getScene().getWindow());
            if (file != null){
                getData.path = file.getAbsolutePath();

                image = new Image(file.toURI().toString(),118, 139, false, true);
                imageTF.setImage(image);
            }

    }


    public void inscription_patient() {
        String cinRegex = "\\d{8}"; // Expression régulière pour un CIN de 8 chiffres
        String numcarteRegex = "\\d{5}";
        String nomPrenomRegex = "[a-zA-Z]+"; // Expression régulière pour les noms et prénoms contenant uniquement des lettres
        String telRegex = "\\d{8}"; // Expression régulière pour un numéro de téléphone de 8 chiffres
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // Expression régulière pour l'email
        String passwordRegex = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,}"; // Expression régulière pour un mot de passe contenant au moins une lettre, un chiffre et de longueur minimale 8

        String sqlGlobalUser = "INSERT INTO global_user " +
                "(cin, nom, prenom, genre, datenaissance, numtel, email, password, interlock, image, role)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlPatient = "INSERT INTO patient (id, numcarte) VALUES (?, ?)";

        connect = MyConnection.getInstance().getCnx();
        try {
            Alert alert;
            // Vérification si tous les champs sont remplis
            if (cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty()
                    || (!genreTF.isSelected() && !genreTF1.isSelected()) || date_de_naissanceTF.getValue() == null
                    || numtelTF.getText().isEmpty() || emailTF.getText().isEmpty()
                    || passwordTF.getText().isEmpty() || interlockTF.getText().isEmpty()
                    || roleTF.getText().isEmpty() || getData.path == null || getData.path.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs !");
                alert.showAndWait();
            } else if (!cinTF.getText().matches(cinRegex)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Le CIN doit être composé exactement de 8 chiffres !");
                alert.showAndWait();
            } else if (!nomTF.getText().matches(nomPrenomRegex)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Le nom ne doit contenir que des lettres !");
                alert.showAndWait();
            } else if (!prenomTF.getText().matches(nomPrenomRegex)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Le prénom ne doit contenir que des lettres !");
                alert.showAndWait();
            } else if (!numtelTF.getText().matches(telRegex)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Le numéro de téléphone doit contenir exactement 8 chiffres !");
                alert.showAndWait();
            } else if (date_de_naissanceTF.getValue().isAfter(LocalDate.now())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("La date de naissance ne peut pas être postérieure à la date actuelle !");
                alert.showAndWait();
            } else if (!emailTF.getText().matches(emailRegex)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir une adresse email valide !");
                alert.showAndWait();
            } else if (!numcarteTF.getText().matches(numcarteRegex)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Le numéro de la carte jaune doit contenir exactement 5 chiffres !");
                alert.showAndWait();
            } else {
                if (!passwordTF.getText().matches(passwordRegex)) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Le mot de passe doit contenir au moins une lettre, un chiffre et être d'une longueur minimale de 8 caractères !");
                } else {
                    // Vérification d'unicité pour le numéro de carte jaune
                    String checkNumCarte = "SELECT numcarte FROM patient WHERE numcarte = ?";
                    PreparedStatement prepareCheckNumCarte = connect.prepareStatement(checkNumCarte);
                    prepareCheckNumCarte.setString(1, numcarteTF.getText());
                    ResultSet resultNumCarte = prepareCheckNumCarte.executeQuery();
                    if (resultNumCarte.next()) {
                        // Afficher un message d'erreur car le numéro de carte jaune est déjà utilisé
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Le numéro de carte jaune est déjà utilisé !");
                        alert.showAndWait();
                        return; // Sortir de la méthode car l'inscription ne peut pas être poursuivie
                    }
    
                    // Vérification d'unicité pour le numéro de CIN
                    String checkCin = "SELECT cin FROM global_user WHERE cin = ?";
                    PreparedStatement prepareCheckCin = connect.prepareStatement(checkCin);
                    prepareCheckCin.setInt(1, Integer.parseInt(cinTF.getText()));
                    ResultSet resultCin = prepareCheckCin.executeQuery();
                    if (resultCin.next()) {
                        // Afficher un message d'erreur car le CIN est déjà utilisé
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Le numéro de CIN est déjà utilisé !");
                        alert.showAndWait();
                        return; // Sortir de la méthode car l'inscription ne peut pas être poursuivie
                    }
    
                    // Cryptage du mot de passe
                    String hashedPassword = BCrypt.hashpw(passwordTF.getText(), BCrypt.gensalt());
    
                    // Récupération du genre
                    int genre =genreTF.isSelected() ? 0 : 1;
    
                    // Insertion dans global_user
                    prepare = connect.prepareStatement(sqlGlobalUser, Statement.RETURN_GENERATED_KEYS);
                    prepare.setInt(1, Integer.parseInt(cinTF.getText()));
                    prepare.setString(2, nomTF.getText());
                    prepare.setString(3, prenomTF.getText());
                    prepare.setInt(4, genre);
                    LocalDate dateNaissance = date_de_naissanceTF.getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String dateNaissanceFormatee = dateNaissance.format(formatter);
                    prepare.setString(5, dateNaissanceFormatee);
                    prepare.setInt(6, Integer.parseInt(numtelTF.getText()));
                    prepare.setString(7, emailTF.getText());
                    prepare.setString(8, hashedPassword); // Mot de passe crypté
                    prepare.setInt(9, Integer.parseInt(interlockTF.getText()));
                    prepare.setString(11, roleTF.getText());
                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    prepare.setString(10, uri);
                    prepare.executeUpdate();
    
                    // Récupération de l'ID de l'utilisateur nouvellement inséré
                    ResultSet generatedKeys = prepare.getGeneratedKeys();
                    int userId = 0;
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    }
    
                    // Insertion dans la table patient
                    prepare = connect.prepareStatement(sqlPatient);
                    prepare.setInt(1, userId);
                    prepare.setString(2, numcarteTF.getText()); // Supposons que numcarteTF contient le numéro de carte
                    prepare.executeUpdate();
    
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Compte créé avec succès !");
                }
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void close(){

         System.exit(0);
    }
    public void minimise(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }


    }





