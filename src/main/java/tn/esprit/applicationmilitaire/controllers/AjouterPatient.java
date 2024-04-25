package tn.esprit.applicationmilitaire.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.applicationmilitaire.models.Admin;
import tn.esprit.applicationmilitaire.models.Patient;
import tn.esprit.applicationmilitaire.models.getData;
import tn.esprit.applicationmilitaire.test.HelloApplication;
import tn.esprit.applicationmilitaire.utils.MyConnection;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AjouterPatient {
    @FXML
    private RadioButton InterlockTF;

    @FXML
    private TextField carteTF;

    @FXML
    private TextField cinTF;

    @FXML
    private Button close;

    @FXML
    private DatePicker date_de_naissanceTF;

    @FXML
    private TextField emailTF;

    @FXML
    private RadioButton genreTF;

    @FXML
    private RadioButton genreTF1;

    @FXML
    private RadioButton interlockTF;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimise;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numtelTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TableColumn<?, ?> patientcol_carte;

    @FXML
    private TableColumn<?, ?> patientcol_cin;

    @FXML
    private TableColumn<?, ?> patientcol_datedenaissance;

    @FXML
    private TableColumn<?, ?> patientcol_email;

    @FXML
    private TableColumn<?, ?> patientcol_genre;

    @FXML
    private TableColumn<?, ?> patientcol_id;

    @FXML
    private TableColumn<?, ?> patientcol_image;

    @FXML
    private TableColumn<?, ?> patientcol_interlock;

    @FXML
    private TableColumn<?, ?> patientcol_nom;

    @FXML
    private TableColumn<?, ?> patientcol_numtel;

    @FXML
    private TableColumn<?, ?> patientcol_password;

    @FXML
    private TableColumn<?, ?> patientcol_prenom;

    @FXML
    private TableColumn<?, ?> patientcol_role;

    @FXML
    private TableView<Patient> patientcol_tableview;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField recherche_Patient;

    @FXML
    private RadioButton roleTF;

    @FXML
    private RadioButton roleTF1;

    @FXML
    private RadioButton roleTF2;

    @FXML
    private RadioButton roleTF3;
    @FXML
    private ImageView imageTF;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;

    public void addPatientSelect() {
        Patient patient1 = patientcol_tableview.getSelectionModel().getSelectedItem();
        int num = patientcol_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        carteTF.setText(String.valueOf(patient1.getNumcarte()));
        cinTF.setText(String.valueOf(patient1.getCin()));
        nomTF.setText(patient1.getNom());
        prenomTF.setText(patient1.getPrenom());

        // Décocher toutes les radio box
        genreTF.setSelected(false);
        genreTF1.setSelected(false);

        // Vérifier le genre et sélectionner le radio bouton approprié
        int genre = patient1.getGenre();
        if (genre == 1) { // Suppose que 1 correspond à "Homme"
            genreTF.setSelected(true);
        } else if (genre == 0) { // Suppose que 0 correspond à "Femme"
            genreTF1.setSelected(true);
        }

        // Convertir Date en LocalDate
        Instant instant = patient1.getDateNaissance().toInstant();
        LocalDate dateNaissance = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // Définir la date de naissance dans le DatePicker
        date_de_naissanceTF.setValue(dateNaissance);
        numtelTF.setText(String.valueOf(patient1.getNumtel()));
        emailTF.setText(patient1.getEmail());
        passwordTF.setText(patient1.getPassword());
        interlockTF.setSelected(false);
        InterlockTF.setSelected(false);

        // Vérifier la valeur du champ interlock et sélectionner le radio bouton approprié
        boolean interlock = patient1.isInterlock();
        if (interlock) {
            interlockTF.setSelected(true);
        } else {
            InterlockTF.setSelected(true);
        }



        getData.path = patient1.getImage();
        String uri = "file:" + patient1.getImage();
        image = new Image(uri, 118, 139, false, true);
        imageTF.setImage(image);
    }
    @FXML
    public void ajouterPatientInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        if (file != null){
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(),118, 139, false, true);
            imageTF.setImage(image);
        }

    }


    @FXML
    void ajouterPatientReset() {
        carteTF.setText("");
        cinTF.setText("");
        nomTF.setText("");
        prenomTF.setText("");
        date_de_naissanceTF.setValue(null);
        numtelTF.setText("");
        emailTF.setText("");
        passwordTF.setText("");
        imageTF.setImage(null);
        getData.path = "";
        genreTF.setSelected(false);
        genreTF1.setSelected(false);
        interlockTF.setSelected(false);
        InterlockTF.setSelected(false);

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

    public ObservableList<Patient> addPatientListData() throws SQLException {
        ObservableList<Patient> list = FXCollections.observableArrayList();
        String sql = "SELECT u.id, u.cin, u.nom, u.prenom, u.genre, u.datenaissance, u.numtel, u.role, u.email, u.password, u.interlock, u.image, p.numcarte " +
                "FROM global_user u " +
                "JOIN patient p ON u.id = p.id";

        connect = MyConnection.getInstance().getCnx();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                Patient patient = new Patient(
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
                        result.getTimestamp("datenaissance"),
                        result.getBoolean("interlock"),
                        result.getInt("numcarte")
                );
                list.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    private ObservableList<Patient> addPatientList;
    public void addPatientShowList() throws SQLException {
        addPatientList = addPatientListData();
        patientcol_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientcol_carte.setCellValueFactory(new PropertyValueFactory<>("numcarte"));
        patientcol_cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        patientcol_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        patientcol_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        patientcol_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        patientcol_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        patientcol_numtel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        patientcol_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        patientcol_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        patientcol_interlock.setCellValueFactory(new PropertyValueFactory<>("interlock"));
        patientcol_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        patientcol_image.setCellValueFactory(new PropertyValueFactory<>("image"));



        patientcol_tableview.setItems(addPatientList);
        //addEventSearch();
    }

    public void initialize() throws SQLException {
        addPatientList = addPatientListData(); // Initialize addEventList
        addPatientShowList(); // Populate TableView with data from addEventList
        addPatientSelect();
        //setupSearchListener();
    }


    public void ajouterPatientadd() {
        String sql = "INSERT INTO global_user " +
                "(cin, nom, prenom, genre, datenaissance, numtel, email, password, interlock, image, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlPatient = "INSERT INTO patient (numcarte, id) VALUES (?, LAST_INSERT_ID())";

        connect = MyConnection.getInstance().getCnx();
        try {
            Alert alert;
            if (carteTF.getText().isEmpty() || cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() || genreTF.getText().isEmpty() || genreTF1.getText().isEmpty() || interlockTF.getText().isEmpty() || InterlockTF.getText().isEmpty() ||
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
                    alert.setContentText("Patient existe déjà !");
                    alert.showAndWait();
                } else {
                    // Afficher une alerte de confirmation pour ajouter le patient
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Voulez-vous ajouter ce patient ?");

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
                        prepare.setString(11, "Patient");

                        // Execute the SQL statement to insert into global_user table
                        prepare.executeUpdate();

                        // Now, insert numcarte into patient table
                        prepare = connect.prepareStatement(sqlPatient);
                        prepare.setInt(1, Integer.parseInt(carteTF.getText()));
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Ajout avec succès !");
                        alert.showAndWait();

                        addPatientShowList();
                        addPatientSelect();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void supprimerPatientdelete() {
        String sqlGlobalUser = "DELETE FROM global_user WHERE cin = ?";
        String sqlPatient = "DELETE FROM patient WHERE id = (SELECT id FROM global_user WHERE cin = ?)";

        Connection connect = null;
        PreparedStatement prepareGlobalUser = null;
        PreparedStatement preparePatient = null;

        connect = MyConnection.getInstance().getCnx();

        try {
            Alert alert;
            if (carteTF.getText().isEmpty() ||cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
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
                alert.setContentText("Voulez-vous supprimer ce patient ?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    // Suppression de l'utilisateur de la table global_user
                    prepareGlobalUser = connect.prepareStatement(sqlGlobalUser);
                    prepareGlobalUser.setString(1, cinTF.getText());
                    prepareGlobalUser.executeUpdate();

                    // Suppression de l'utilisateur de la table patient
                    preparePatient = connect.prepareStatement(sqlPatient);
                    preparePatient.setString(1, cinTF.getText());
                    preparePatient.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Suppression avec succès !");
                    alert.showAndWait();

                    addPatientShowList();
                    ajouterPatientReset();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void modifierPatientupdate() {
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sqlGlobalUser = "UPDATE global_user SET cin = ?, nom = ?, prenom = ?, genre = ?, datenaissance = ?, numtel = ?, password = ?, interlock = ?,  image = ? WHERE cin = ?";
        String sqlPatient = "UPDATE patient SET numcarte = ? WHERE id = (SELECT id FROM global_user WHERE cin = ?)";
        Connection connect = null;
        PreparedStatement prepareGlobalUser = null;
        PreparedStatement preparePatient = null;

        try {
            Alert alert;
            if (carteTF.getText().isEmpty() || cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
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

                    // Obtention d'une nouvelle connexion à chaque fois
                    connect = MyConnection.getInstance().getCnx();

                    // Préparation des requêtes avec des paramètres
                    prepareGlobalUser = connect.prepareStatement(sqlGlobalUser);
                    preparePatient = connect.prepareStatement(sqlPatient);

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

                    // Remplir les paramètres pour la requête patient
                    preparePatient.setString(1, carteTF.getText()); // Modification pour inclure numcarte
                    preparePatient.setString(2, cinTF.getText()); // Utilisation du cin comme condition WHERE

                    // Exécuter les requêtes de mise à jour
                    prepareGlobalUser.executeUpdate();
                    preparePatient.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Modification avec succès !");
                    alert.showAndWait();

                    addPatientShowList();
                    ajouterPatientReset();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
