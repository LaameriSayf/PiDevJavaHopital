package tn.esprit.applicationmilitaire.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.applicationmilitaire.models.Medecin;
import tn.esprit.applicationmilitaire.models.getData;
import tn.esprit.applicationmilitaire.test.HelloApplication;
import tn.esprit.applicationmilitaire.utils.MyConnection;


import java.io.File;
import java.sql.*;

public class AjouterMedecin {

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField cinTF;

    @FXML
    private Button close;

    @FXML
    private DatePicker date_de_naissanceTF;

    @FXML
    private TextField emailTF;




    @FXML
    private RadioButton etatTF;

    @FXML
    private RadioButton etatTF1;
    @FXML
    private RadioButton genreTF;

    @FXML
    private RadioButton genreTF1;


    @FXML
    private Button gereradmin;

    @FXML
    private Button gererblog;

    @FXML
    private Button gerercategorie;

    @FXML
    private Button gererdossiersmedicale;

    @FXML
    private Button gerermedecin;

    @FXML
    private Button gerermedicament;

    @FXML
    private Button gererordonnance;

    @FXML
    private Button gererpatient;

    @FXML
    private Button gererpharmacien;

    @FXML
    private Button gererrdv;

    @FXML
    private ImageView imageTF;

    @FXML
    private RadioButton InterlockTF;
    @FXML
    private RadioButton interlockTF;

    @FXML
    private Button medecin_ajouterBtn;

    @FXML
    private Button medecin_clearBtn;

    @FXML
    private AnchorPane medecin_form;

    @FXML
    private Button medecin_insérerBtn;

    @FXML
    private Button medecin_modifierBtn;

    @FXML
    private Button medecin_supprimerBtn;

    @FXML
    private TableColumn<?, ?> medecincol_cin;

    @FXML
    private TableColumn<?, ?> medecincol_datedenaissance;

    @FXML
    private TableColumn<?, ?> medecincol_email;

    @FXML
    private TableColumn<?, ?> medecincol_etat;

    @FXML
    private TableColumn<?, ?> medecincol_genre;

    @FXML
    private TableColumn<?, ?> medecincol_id;
    @FXML
    private TableColumn<?, ?> medecincol_password;

    @FXML
    private TableColumn<?, ?> medecincol_image;

    @FXML
    private TableColumn<?, ?> medecincol_interlock;

    @FXML
    private TableColumn<?, ?> medecincol_nom;

    @FXML
    private TableColumn<?, ?> medecincol_numtel;

    @FXML
    private TableColumn<?, ?> medecincol_prenom;

    @FXML
    private TableColumn<?, ?> medecincol_role;

    @FXML
    private TableColumn<?, ?> medecincol_specialite;

    @FXML
    private TableView<Medecin> medecincol_tableview;

    @FXML
    private Button minimise;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numtelTF;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField recherche_Medecin;

    @FXML
    private TextField roleTF;
    @FXML
    private TextField passwordTF;


    @FXML
    private Button sedeconnecter;

    @FXML
    private TextField specialiteTF;

    @FXML
    private Button tableaudeboard;


    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;


    public void ajouterMedecinadd(){

        String sql = "INSERT INTO global_user " +
                "(cin, nom, prenom, genre, datenaissance, numtel, email, password, interlock, image, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlMedecin = "INSERT INTO medecin (specialite, etat,id) VALUES (?,?,LAST_INSERT_ID())";

        connect = MyConnection.getInstance().getCnx();
        try {
            Alert alert;
            if (etatTF.getText().isEmpty() ||specialiteTF.getText().isEmpty() || cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() || genreTF.getText().isEmpty()  || interlockTF.getText().isEmpty() ||
                    date_de_naissanceTF.getValue() == null || numtelTF.getText().isEmpty() || emailTF.getText().isEmpty() || passwordTF.getText().isEmpty() ||
                    getData.path == null || getData.path.equals("")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            } else {
                String check = "SELECT cin FROM global_user WHERE cin = '" + cinTF.getText() + "'";
                Statement statement = connect.createStatement();
                result = statement.executeQuery(check);
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Medecin existe déjà !");
                    alert.showAndWait();
                } else {
                    // Afficher une alerte de confirmation pour ajouter le patient
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Voulez-vous ajouter ce Medecin ?");

                    // Ajouter des boutons de confirmation et d'annulation
                    ButtonType confirmButton = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

                    // Ajouter les boutons à l'alerte
                    confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

                    // Attendre la réponse de l'utilisateur
                    Optional<ButtonType> userChoice = confirmationAlert.showAndWait();

                    // Si l'utilisateur confirme, procéder à l'ajout du patient
                    if (userChoice.isPresent() && userChoice.get() == confirmButton) {
                        prepare = connect.prepareStatement(sql);
                        prepare.setInt(1, Integer.parseInt(cinTF.getText()));
                        prepare.setString(2, nomTF.getText());
                        prepare.setString(3, prenomTF.getText());

                        // Determine the gender based on which CheckBox is selected
                        if (genreTF.isSelected()) {
                            prepare.setBoolean(4, true); // Assuming genreTF represents "Homme"
                        } else {
                            prepare.setBoolean(4, false); // Assuming genreTF1 represents "Femme"
                        }

                        LocalDate dateNaissance = date_de_naissanceTF.getValue();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String dateNaissanceFormatee = dateNaissance.format(formatter);
                        prepare.setString(5, dateNaissanceFormatee);
                        prepare.setInt(6, Integer.parseInt(numtelTF.getText()));
                        prepare.setString(7, emailTF.getText());
                        prepare.setString(8, passwordTF.getText());
                        prepare.setString(10, getData.path.replace("\\", "\\\\"));

                        // Determine the interlock value based on which CheckBox is selected
                        if (interlockTF.isSelected()) {
                            prepare.setInt(9, 1); // Assuming interlockTF represents "Oui", so set 1
                        } else {
                            prepare.setInt(9, 0); // Assuming InterlockTF represents "Non", so set 0
                        }

                        // Set the role to "Patient" by default
                        prepare.setString(11, "Medecin");

                        // Execute the SQL statement to insert into global_user table
                        prepare.executeUpdate();

                        // Now, insert numcarte into patient table
                        prepare = connect.prepareStatement(sqlMedecin);
                        prepare.setString(1, specialiteTF.getText());
                        if (etatTF.isSelected()) {
                            prepare.setBoolean(2, true); // Assuming genreTF represents "Homme"
                        } else {
                            prepare.setBoolean(2, false); // Assuming genreTF1 represents "Femme"
                        }
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Ajout avec succès !");
                        alert.showAndWait();

                        addMedecinShowList();
                        addMedecinSelect();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void ajouterMedecinReset(){
        cinTF.setText("");
        nomTF.setText("");
        prenomTF.setText("");
        date_de_naissanceTF.setValue(null);
        numtelTF.setText("");
        specialiteTF.setText("");
        emailTF.setText("");
        passwordTF.setText("");
        imageTF.setImage(null);
        getData.path = "";
        genreTF.setSelected(false);
        genreTF1.setSelected(false);
        interlockTF.setSelected(false);
        InterlockTF.setSelected(false);
        etatTF.setSelected(false);
        etatTF1.setSelected(false);
    }
    public void ajouterMedecinInsertImage(){
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        if (file != null){
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(),118, 139, false, true);
            imageTF.setImage(image);
        }

    }
    public ObservableList<Medecin> addMedecinListData() throws SQLException {
        ObservableList<Medecin> list = FXCollections.observableArrayList();
        String sql = "SELECT u.id, u.cin, u.nom, u.prenom, u.genre, u.datenaissance, u.numtel, u.role, u.email, u.password, u.interlock, u.image, m.specialite , m.etat " +
                "FROM global_user u " +
                "JOIN medecin m ON u.id = m.id";
        connect = MyConnection.getInstance().getCnx();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                Medecin medecin = new Medecin(
                        result.getInt("id"),
                        result.getInt("cin"),
                        result.getInt("numtel"),
                        result.getInt("etat"),
                        result.getInt("genre"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("specialite"),
                        result.getString("image"),
                        result.getString("role"),
                        result.getTimestamp("datenaissance"),
                        result.getBoolean("interlock")
                );
                list.add(medecin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    private ObservableList<Medecin> addMedecinList;
    public void addMedecinShowList() throws SQLException {
        addMedecinList = addMedecinListData();
        medecincol_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        medecincol_cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        medecincol_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        medecincol_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        medecincol_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        medecincol_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        medecincol_numtel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        medecincol_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        medecincol_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        medecincol_interlock.setCellValueFactory(new PropertyValueFactory<>("interlock"));
        medecincol_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        medecincol_specialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        medecincol_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        medecincol_image.setCellValueFactory(new PropertyValueFactory<>("image"));


        medecincol_tableview.setItems(addMedecinList);
        //addEventSearch();
    }
    public void addMedecinSelect() {
        // Sélectionner le médecin dans la table
        Medecin medecin1 = medecincol_tableview.getSelectionModel().getSelectedItem();
        // Récupérer l'index du médecin sélectionné
        int num = medecincol_tableview.getSelectionModel().getSelectedIndex();

        // Vérifier si aucun médecin n'est sélectionné ou si l'index est incorrect
        if ((num - 1) < -1) {
            return;
        }

        // Remplir les champs avec les informations du médecin sélectionné
        cinTF.setText(String.valueOf(medecin1.getCin()));
        nomTF.setText(medecin1.getNom());
        prenomTF.setText(medecin1.getPrenom());

        // Décocher toutes les radio box de genre
        genreTF.setSelected(false);
        genreTF1.setSelected(false);

        // Vérifier le genre et sélectionner le radio bouton approprié
        int genre = medecin1.getGenre();
        if (genre == 1) { // Suppose que 1 correspond à "Homme"
            genreTF.setSelected(true);
        } else if (genre == 0) { // Suppose que 0 correspond à "Femme"
            genreTF1.setSelected(true);
        }

        // Convertir la Date de naissance en LocalDate
        Instant instant = medecin1.getDateNaissance().toInstant();
        LocalDate dateNaissance = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // Définir la date de naissance dans le DatePicker
        date_de_naissanceTF.setValue(dateNaissance);

        numtelTF.setText(String.valueOf(medecin1.getNumtel()));
        emailTF.setText(medecin1.getEmail());
        passwordTF.setText(medecin1.getPassword());

        // Décocher toutes les radio box d'interlock
        interlockTF.setSelected(false);
        InterlockTF.setSelected(false);

        // Vérifier la valeur du champ interlock et sélectionner le radio bouton approprié
        boolean interlock = medecin1.isInterlock();
        if (interlock) {
            interlockTF.setSelected(true);
        } else {
            InterlockTF.setSelected(true);
        }

        // Remplir le champ de spécialité
        specialiteTF.setText(medecin1.getSpecialite());

        // Décocher toutes les radio box d'état
        etatTF.setSelected(false);
        etatTF1.setSelected(false);

        // Vérifier la valeur de l'état et sélectionner le radio bouton approprié
        int etat = medecin1.getEtat();
        if (etat == 1) { // Suppose que 1 correspond à "disponible"
            etatTF.setSelected(true);
        } else if (etat == 0) { // Suppose que 0 correspond à "non disponible "
            etatTF1.setSelected(true);
        }

        // Récupérer le chemin de l'image et l'afficher dans ImageView
        getData.path = medecin1.getImage();
        String uri = "file:" + medecin1.getImage();
        image = new Image(uri, 118, 139, false, true);
        imageTF.setImage(image);
    }


    public void initialize() throws SQLException {
        addMedecinList = addMedecinListData(); // Initialize addMedecinList
        addMedecinShowList(); // Populate TableView with data from addMedecinList
        addMedecinSelect();
        //setupSearchListener();
    }



    public void modifierMedecinupdate() {
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sqlGlobalUser = "UPDATE global_user SET cin = ?, nom = ?, prenom = ?, genre = ?, datenaissance = ?, numtel = ?, password = ?, interlock = ?,  image = ? WHERE cin = ?";
        String sqlMedecin = "UPDATE medecin SET specialite = ?, etat = ? WHERE id = (SELECT id FROM global_user WHERE cin = ?)";
        Connection connect = null;
        PreparedStatement prepareGlobalUser = null;
        PreparedStatement prepareMedecin = null;

        try {
            Alert alert;
            if (etatTF.getText().isEmpty() || specialiteTF.getText().isEmpty() || cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
                    date_de_naissanceTF.getValue() == null || numtelTF.getText().isEmpty() ||
                    emailTF.getText().isEmpty() || passwordTF.getText().isEmpty() ||
                    (!interlockTF.isSelected() && !InterlockTF.isSelected()) ||
                    uri == null || uri.equals("")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous modifier ces informations!");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    // Récupérer le genre sélectionné
                    int genreSelectionne = genreTF.isSelected() ? 1 : 0; // 1 pour Homme, 0 pour Femme

                    // Récupérer l'état sélectionné
                    int etatSelectionne = etatTF.isSelected() ? 1 : 0; // 1 pour actif, 0 pour inactif

                    // Obtention d'une nouvelle connexion à chaque fois
                    connect = MyConnection.getInstance().getCnx();

                    // Préparation des requêtes avec des paramètres
                    prepareGlobalUser = connect.prepareStatement(sqlGlobalUser);
                    prepareMedecin = connect.prepareStatement(sqlMedecin);

                    // Remplir les paramètres pour la requête global_user
                    prepareGlobalUser.setInt(1, Integer.parseInt(cinTF.getText()));
                    prepareGlobalUser.setString(2, nomTF.getText());
                    prepareGlobalUser.setString(3, prenomTF.getText());
                    prepareGlobalUser.setInt(4, genreSelectionne); // Utilisation du genre sélectionné
                    prepareGlobalUser.setObject(5, date_de_naissanceTF.getValue());
                    prepareGlobalUser.setInt(6, Integer.parseInt(numtelTF.getText()));
                    prepareGlobalUser.setString(7, passwordTF.getText());

                    // Convertir la valeur de l'interlock en entier (0 ou 1)
                    int interlockValue = interlockTF.isSelected() ? 1 : 0;
                    prepareGlobalUser.setInt(8, interlockValue);

                    prepareGlobalUser.setString(9, uri);
                    prepareGlobalUser.setString(10, cinTF.getText()); // Utilisation du cin comme condition WHERE

                    // Remplir les paramètres pour la requête medecin
                    prepareMedecin.setString(1, specialiteTF.getText()); // Modification pour inclure specialite
                    prepareMedecin.setInt(2, etatSelectionne); // Utilisation de l'état sélectionné
                    prepareMedecin.setString(3, cinTF.getText()); // Utilisation du cin comme condition WHERE

                    // Exécuter les requêtes de mise à jour
                    prepareGlobalUser.executeUpdate();
                    prepareMedecin.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Modification avec succès !");
                    alert.showAndWait();

                    addMedecinShowList();
                    ajouterMedecinReset();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void supprimerMedecindelete() {
        String sqlGlobalUser = "DELETE FROM global_user WHERE cin = ?";
        String sqlMedecin = "DELETE FROM medecin WHERE id = (SELECT id FROM global_user WHERE cin = ?)";

        Connection connect = null;
        PreparedStatement prepareGlobalUser = null;
        PreparedStatement preparePatient = null;

        connect = MyConnection.getInstance().getCnx();

        try {
            Alert alert;
            if (etatTF.getText().isEmpty() || specialiteTF.getText().isEmpty() ||cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
                    genreTF.getText().isEmpty() || date_de_naissanceTF.getValue() == null || numtelTF.getText().isEmpty() ||
                    emailTF.getText().isEmpty() || passwordTF.getText().isEmpty() || interlockTF.getText().isEmpty() ||
                    getData.path == null || getData.path.equals("")) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous supprimer ce medecin ?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    // Suppression de l'utilisateur de la table global_user
                    prepareGlobalUser = connect.prepareStatement(sqlGlobalUser);
                    prepareGlobalUser.setString(1, cinTF.getText());
                    prepareGlobalUser.executeUpdate();

                    // Suppression de l'utilisateur de la table patient
                    preparePatient = connect.prepareStatement(sqlMedecin);
                    preparePatient.setString(1, cinTF.getText());
                    preparePatient.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Suppression avec succès !");
                    alert.showAndWait();

                    addMedecinShowList();
                    ajouterMedecinReset();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        System.exit(0);
    }
    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
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
    private Button tableaudeboardbtn;

    @FXML
    public void tableaudeboardbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Dashboard.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) tableaudeboardbtn.getScene().getWindow();
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

