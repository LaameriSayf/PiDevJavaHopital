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
import tn.esprit.applicationmilitaire.models.Medecin;
import tn.esprit.applicationmilitaire.models.getData;
import tn.esprit.applicationmilitaire.test.HelloApplication;
import tn.esprit.applicationmilitaire.utils.MyConnection;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AjouterAdmin {

    @FXML
    private RadioButton InterlockTF;


    @FXML
    private TableColumn<?, ?> admincol_cin;

    @FXML
    private TableColumn<?, ?> admincol_datedenaissance;

    @FXML
    private TableColumn<?, ?> admincol_email;
    @FXML
    private Button gererpatientbtn;

    @FXML
    private TableColumn<?, ?> admincol_genre;

    @FXML
    private TableColumn<?, ?> admincol_id;

    @FXML
    private TableColumn<?, ?> admincol_image;

    @FXML
    private TableColumn<?, ?> admincol_interlock;

    @FXML
    private TableColumn<?, ?> admincol_nom;

    @FXML
    private TableColumn<?, ?> admincol_numtel;

    @FXML
    private TableColumn<?, ?> admincol_password;

    @FXML
    private TableColumn<?, ?> admincol_prenom;

    @FXML
    private TableColumn<?, ?> admincol_role;

    @FXML
    private TableView<Admin> admincol_tableview;

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
    private Button minimise;
    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numtelTF;

    @FXML
    private ImageView imageTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField recherche_Admin;

    @FXML
    private RadioButton interlockTF;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;

    public void addAdminSelect() {
        Admin admin1 = admincol_tableview.getSelectionModel().getSelectedItem();
        int num = admincol_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        cinTF.setText(String.valueOf(admin1.getCin()));
        nomTF.setText(admin1.getNom());
        prenomTF.setText(admin1.getPrenom());

        // Décocher toutes les radio box
        genreTF.setSelected(false);
        genreTF1.setSelected(false);

        // Vérifier le genre et sélectionner le radio bouton approprié
        int genre = admin1.getGenre();
        if (genre == 1) { // Suppose que 1 correspond à "Homme"
            genreTF.setSelected(true);
        } else if (genre == 0) { // Suppose que 0 correspond à "Femme"
            genreTF1.setSelected(true);
        }

        // Convertir Date en LocalDate
        Instant instant = admin1.getDateNaissance().toInstant();
        LocalDate dateNaissance = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // Définir la date de naissance dans le DatePicker
        date_de_naissanceTF.setValue(dateNaissance);
        numtelTF.setText(String.valueOf(admin1.getNumtel()));
        emailTF.setText(admin1.getEmail());
        passwordTF.setText(admin1.getPassword());
        interlockTF.setSelected(false);
        InterlockTF.setSelected(false);

        // Vérifier la valeur du champ interlock et sélectionner le radio bouton approprié
        boolean interlock = admin1.isInterlock();
        if (interlock) {
            interlockTF.setSelected(true);
        } else {
            InterlockTF.setSelected(true);
        }

        getData.path = admin1.getImage();
        String uri = "file:" + admin1.getImage();
        image = new Image(uri, 118, 139, false, true);
        imageTF.setImage(image);
    }




    @FXML
     public void ajouterAdminInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        if (file != null){
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(),118, 139, false, true);
            imageTF.setImage(image);
        }

    }

    @FXML
    void ajouterAdminReset() {
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


    public ObservableList<Admin> addAdminListData() throws SQLException {
        ObservableList<Admin> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM global_user WHERE role = 'Admin'";
        connect = MyConnection.getInstance().getCnx();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                Admin admin = new Admin(
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
                        result.getBoolean("interlock")
                );
                list.add(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    private ObservableList<Admin> addAdminList;
    public void addAdminShowList() throws SQLException {
        addAdminList = addAdminListData();
        admincol_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        admincol_cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        admincol_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        admincol_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        admincol_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        admincol_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        admincol_numtel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        admincol_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        admincol_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        admincol_interlock.setCellValueFactory(new PropertyValueFactory<>("interlock"));
        admincol_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        admincol_image.setCellValueFactory(new PropertyValueFactory<>("image"));



        admincol_tableview.setItems(addAdminList);
        //addEventSearch();
    }


    public void initialize() throws SQLException {
        addAdminList = addAdminListData(); // Initialize addEventList
        addAdminShowList(); // Populate TableView with data from addEventList
        addAdminSelect();
        //setupSearchListener();
    }




        @FXML
     public void supprimerAdmindelete() {
        String sql = "DELETE FROM global_user WHERE cin = '"
                +cinTF.getText()+"'";

        connect = MyConnection.getInstance().getCnx();

        try {
            Alert alert;
            if(cinTF.getText().isEmpty()
                    || nomTF.getText().isEmpty()
                    || prenomTF.getText().isEmpty()
                    || genreTF.getText().isEmpty()
                    || date_de_naissanceTF.getValue() == null
                    || numtelTF.getText().isEmpty()
                    || emailTF.getText().isEmpty()
                    || passwordTF.getText().isEmpty()
                    || interlockTF.getText().isEmpty()
                    || getData.path == null || getData.path == "" ){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un administrateur!");
                alert.showAndWait();
            }else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous supprimer cet administrateur !");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Suppression avec succés !");
                    alert.showAndWait();

                    addAdminShowList();
                    ajouterAdminReset();
                }

            }
        }catch (Exception e) {e.printStackTrace();}
    }



    @FXML
    public void ajouterAdminadd() {
        String sqlGlobalUser = "INSERT INTO global_user " +
                "(cin, nom, prenom, genre, datenaissance, numtel, email, password, interlock, image, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAdmin = "INSERT INTO admin (id) VALUES (?)";

        Connection connect = null;
        PreparedStatement prepareGlobalUser = null;
        PreparedStatement prepareAdmin = null;
        ResultSet result = null;

        connect = MyConnection.getInstance().getCnx();
        try {
            Alert alert;
            if (cinTF.getText().isEmpty() || !isValidCIN(cinTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir un CIN valide (8 chiffres).");
                alert.showAndWait();
                return;
            } else if (nomTF.getText().isEmpty() || !isValidName(nomTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir un nom valide (caractères alphabétiques uniquement).");
                alert.showAndWait();
                return;
            } else if (prenomTF.getText().isEmpty() || !isValidName(prenomTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir un prénom valide (caractères alphabétiques uniquement).");
                alert.showAndWait();
                return;
            } else if (date_de_naissanceTF.getValue() == null || !isValidDateOfBirth(date_de_naissanceTF.getValue())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("L'âge minimum doit être de 24 ans.");
                alert.showAndWait();
                return;
            } else if (numtelTF.getText().isEmpty() || !isValidPhoneNumber(numtelTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir un numéro de téléphone valide (8 chiffres).");
                alert.showAndWait();
                return;
            } else if (emailTF.getText().isEmpty() || !isValidEmail(emailTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir une adresse email valide.");
                alert.showAndWait();
                return;
            } else if (passwordTF.getText().isEmpty() || !isValidPassword(passwordTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir un mot de passe valide (8 caractères alphanumériques au minimum).");
                alert.showAndWait();
                return;
            } else if (getData.path == null || getData.path.equals("")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une image.");
                alert.showAndWait();
                return;
            }

            String check = "SELECT cin FROM global_user WHERE cin = '" + cinTF.getText() + "'";
            Statement statement = connect.createStatement();
            result = statement.executeQuery(check);
            if (result.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Admin existe déjà !");
                alert.showAndWait();
                return;
            }

            // Afficher une alerte de confirmation pour ajouter l'administrateur
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Voulez-vous ajouter cet administrateur ?");

            // Ajouter des boutons de confirmation et d'annulation
            ButtonType confirmButton = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            // Ajouter les boutons à l'alerte
            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            // Attendre la réponse de l'utilisateur
            Optional<ButtonType> userChoice = confirmationAlert.showAndWait();

            // Si l'utilisateur confirme, procéder à l'ajout de l'administrateur
            if (userChoice.isPresent() && userChoice.get() == confirmButton) {
                // Préparer la requête pour l'insertion dans la table global_user
                prepareGlobalUser = connect.prepareStatement(sqlGlobalUser, Statement.RETURN_GENERATED_KEYS);
                prepareGlobalUser.setInt(1, Integer.parseInt(cinTF.getText()));
                prepareGlobalUser.setString(2, nomTF.getText());
                prepareGlobalUser.setString(3, prenomTF.getText());

                // Determine the gender based on which CheckBox is selected
                if (genreTF.isSelected() && genreTF1.isSelected()) {
                    // Afficher un message d'erreur indiquant que seul un genre doit être sélectionné
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Veuillez sélectionner uniquement un genre.");
                    errorAlert.showAndWait();
                    return; // Sortir de la méthode sans exécuter la requête SQL
                } else if (genreTF.isSelected()) {
                    prepareGlobalUser.setBoolean(4, true); // Assuming genreTF represents "Homme"
                } else if (genreTF1.isSelected()) {
                    prepareGlobalUser.setBoolean(4, false); // Assuming genreTF1 represents "Femme"
                } else {
                    // Aucune case n'est sélectionnée, afficher un message d'erreur
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Veuillez sélectionner un genre.");
                    errorAlert.showAndWait();
                    return; // Sortir de la méthode sans exécuter la requête SQL
                }

                LocalDate dateNaissance = date_de_naissanceTF.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dateNaissanceFormatee = dateNaissance.format(formatter);
                prepareGlobalUser.setString(5, dateNaissanceFormatee);
                prepareGlobalUser.setInt(6, Integer.parseInt(numtelTF.getText()));
                prepareGlobalUser.setString(7, emailTF.getText());
                prepareGlobalUser.setString(8, passwordTF.getText());
                prepareGlobalUser.setString(10, getData.path.replace("\\", "\\\\"));

                // Determine the interlock value based on which CheckBox is selected
                if (interlockTF.isSelected() && InterlockTF.isSelected()) {
                    // Afficher un message d'erreur indiquant que seul un choix doit être sélectionné
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Veuillez sélectionner uniquement un choix pour l'interlock.");
                    errorAlert.showAndWait();
                    return; // Sortir de la méthode sans exécuter la requête SQL
                } else if (interlockTF.isSelected()) {
                    prepareGlobalUser.setInt(9, 1); // Assuming interlockTF represents "Oui", so set 1
                } else if (InterlockTF.isSelected()) {
                    prepareGlobalUser.setInt(9, 0); // Assuming InterlockTF represents "Non", so set 0
                } else {
                    // Aucune case n'est sélectionnée, afficher un message d'erreur
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Veuillez sélectionner un choix pour l'interlock.");
                    errorAlert.showAndWait();
                    return; // Sortir de la méthode sans exécuter la requête SQL
                }

                // Le rôle est toujours "Admin"
                prepareGlobalUser.setString(11, "Admin");

                // Exécution de la requête d'insertion dans la table global_user
                prepareGlobalUser.executeUpdate();

                // Récupération de l'ID généré pour l'utilisateur nouvellement ajouté
                result = prepareGlobalUser.getGeneratedKeys();
                int id = -1;
                if (result.next()) {
                    id = result.getInt(1);
                }

                // Insertion de l'ID dans la table des administrateurs
                prepareAdmin = connect.prepareStatement(sqlAdmin);
                prepareAdmin.setInt(1, id);
                prepareAdmin.executeUpdate();

                // Affichage d'une alerte de succès
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Ajout avec succès !");
                alert.showAndWait();

                // Rafraîchissement de la liste des administrateurs et de l'affichage des détails
                addAdminShowList();
                addAdminSelect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}

    public void modifierAdminupdate() {
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sql = "UPDATE global_user SET cin = ?, nom = ?, prenom = ?, genre = ?, datenaissance = ?, " +
                "numtel = ?, password = ?, interlock = ?, image = ? WHERE cin = ?";

        Connection connect = null;
        PreparedStatement prepare = null;

        try {
            Alert alert;
            if (cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
                    date_de_naissanceTF.getValue() == null || numtelTF.getText().isEmpty() ||
                    emailTF.getText().isEmpty() || passwordTF.getText().isEmpty() || (!genreTF.isSelected() && !genreTF.isSelected()) ||
                    (!interlockTF.isSelected() && !InterlockTF.isSelected()) ||
                    uri == null || uri.equals("")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            } else if (!isValidCIN(cinTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("CIN invalide ! Veuillez saisir un CIN composé de 8 chiffres.");
                alert.showAndWait();
            } else if (!isValidName(nomTF.getText()) || !isValidName(prenomTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Nom ou prénom invalide ! Veuillez saisir uniquement des caractères alphabétiques.");
                alert.showAndWait();
            } else if (!isValidDateOfBirth(date_de_naissanceTF.getValue())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Date de naissance invalide ! L'utilisateur doit avoir plus de 23 ans.");
                alert.showAndWait();
            } else if (!isValidPhoneNumber(numtelTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Numéro de téléphone invalide ! Veuillez saisir exactement 8 chiffres.");
                alert.showAndWait();
            } else if (!isValidEmail(emailTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Email invalide !");
                alert.showAndWait();
            } else if (!isValidPassword(passwordTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Mot de passe invalide ! Veuillez saisir au moins 8 caractères alphanumériques.");
                alert.showAndWait();
            } else {
                // Vérifier si une seule radio box est sélectionnée pour le genre
                if (!(genreTF.isSelected() ^ genreTF1.isSelected())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner un seul genre !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }

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

                    // Préparation de la requête avec des paramètres
                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, Integer.parseInt(cinTF.getText()));
                    prepare.setString(2, nomTF.getText());
                    prepare.setString(3, prenomTF.getText());
                    prepare.setInt(4, genreSelectionne); // Utilisation du genre sélectionné
                    prepare.setObject(5, date_de_naissanceTF.getValue());
                    prepare.setInt(6, Integer.parseInt(numtelTF.getText()));
                    prepare.setString(7, passwordTF.getText());

                    // Convertir la valeur de l'interlock en entier (0 ou 1)
                    int interlockValue = interlockTF.isSelected() ? 1 : 0;
                    prepare.setInt(8, interlockValue);

                    //prepare.setString(9, roleTF.getText());
                    prepare.setString(9, uri);
                    prepare.setString(10, cinTF.getText()); // Utilisation du cin comme condition WHERE

                    // Exécuter la requête de mise à jour
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Modification avec succès !");
                    alert.showAndWait();

                    addAdminShowList();
                    ajouterAdminReset();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidCIN(String cin) {
        // Vérifie si le CIN est composé de 8 chiffres uniquement
        return Pattern.matches("\\d{8}", cin);
    }

    private boolean isValidName(String name) {
        // Vérifie si le nom ou le prénom ne contient que des caractères alphabétiques
        return Pattern.matches("[a-zA-Z]+", name);
    }

    private boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        // Vérifie si l'utilisateur a plus de 23 ans
        LocalDate now = LocalDate.now();
        Period period = Period.between(dateOfBirth, now);
        return period.getYears() > 23;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Vérifie si le numéro de téléphone contient exactement 8 chiffres
        return Pattern.matches("\\d{8}", phoneNumber);
    }

    private boolean isValidEmail(String email) {
        // Vérifie si l'email est valide en utilisant une expression régulière simple
        return Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email);
    }

    private boolean isValidPassword(String password) {
        // Vérifie si le mot de passe a au moins 8 caractères alphanumériques
        return Pattern.matches("[a-zA-Z0-9]{8,}", password);
    }
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
    private Button gerermedecin;



    @FXML
    private Button gererpharmacienbtn;

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

            Stage stage = (Stage) gerermedecin.getScene().getWindow();
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
}

