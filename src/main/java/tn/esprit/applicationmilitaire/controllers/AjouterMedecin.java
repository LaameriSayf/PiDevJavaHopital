package tn.esprit.applicationmilitaire.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.SimpleObjectProperty;

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
        genreTF.setText("");
        date_de_naissanceTF.setValue(null);
        numtelTF.setText("");
        emailTF.setText("");
        passwordTF.setText("");
        interlockTF.setText("");
        specialiteTF.setText("");
        etatTF.setText("");
        imageTF.setImage(null);
        getData.path = "";
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
    public void addMedecinSelect(){
        Medecin medecin1 =medecincol_tableview.getSelectionModel().getSelectedItem();
        int num = medecincol_tableview.getSelectionModel().getSelectedIndex();

        if((num -1) <-1){return;}

        cinTF.setText(String.valueOf(medecin1.getCin()));
        nomTF.setText(medecin1.getNom());
        prenomTF.setText(medecin1.getPrenom());
        genreTF.setText(String.valueOf(medecin1.getGenre()));
        // Suppose que medecin1.getDateNaissance() retourne une Date
// Convertir Date en LocalDate
        Instant instant = medecin1.getDateNaissance().toInstant();
        LocalDate dateNaissance = instant.atZone(ZoneId.systemDefault()).toLocalDate();

// Définir la date de naissance dans le DatePicker
        date_de_naissanceTF.setValue(dateNaissance);
        numtelTF.setText(String.valueOf(medecin1.getNumtel()));
        emailTF.setText(medecin1.getEmail());
        passwordTF.setText(medecin1.getPassword());
        interlockTF.setText(String.valueOf(medecin1.isInterlock()));
        specialiteTF.setText(medecin1.getSpecialite());
        etatTF.setText(String.valueOf(medecin1.getEtat()));
        getData.path =  medecin1.getImage();
        String uri ="file:" + medecin1.getImage();
        image = new Image(uri,118, 139, false, true);
        imageTF.setImage(image);
    }


    public void initialize() throws SQLException {
        addMedecinList = addMedecinListData(); // Initialize addEventList
        addMedecinShowList(); // Populate TableView with data from addEventList
        addMedecinSelect();
        //setupSearchListener();
    }



    public void modifierMedecinupdate(){


    }

    public void supprimerMedecindelete(){
        String sql = "DELETE FROM global_user WHERE nom = '"
                +nomTF.getText()+"'";

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
                    || etatTF.getText().isEmpty()
                    || specialiteTF.getText().isEmpty()

                    || getData.path == null || getData.path == "" ){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un medecin!");
                alert.showAndWait();
            }else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous supprimer ce médecin !");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Suppression avec succés !");
                    alert.showAndWait();

                    addMedecinShowList();
                    ajouterMedecinReset();
                }

            }
        }catch (Exception e) {e.printStackTrace();}
    }


    public void close(){
        System.exit(0);
    }
    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }


}

