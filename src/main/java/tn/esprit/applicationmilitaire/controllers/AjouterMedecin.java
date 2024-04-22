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
    private TextField etatTF;

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
    private TextField interlockTF;

    @FXML
    private Button medecin_ajouterBtn;

    @FXML
    private Button medecin_clearBtn;

    @FXML
    private AnchorPane medecin_form;

    @FXML
    private Button medecin_insérerBtn;
    @FXML
    private TextField genreTF;

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

        //Date date = new Date();
        //java.sql.Date sqlDate =new java.sql.Date(date.getTime());
        //LocalDate sqlDate = LocalDate.now(); // Get the current date

        String sql  =" INSERT INTO  global_user " +
                "( cin,nom,prenom,genre,datenaissance,numtel,email,password,interlock,image,role)"
                +"VALUES( ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

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
                    || roleTF.getText().isEmpty()
                    || getData.path == null || getData.path == "" ){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            }else {

                String check = "SELECT nom FROM global_user WHERE nom = '"
                        +nomTF.getText()+"'";

                statement =connect.createStatement();
                result =statement.executeQuery(check);

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Medecin existe déja!");
                    alert.showAndWait();
                }else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, Integer.parseInt(cinTF.getText()));
                    prepare.setString(2, nomTF.getText());
                    prepare.setString(3, prenomTF.getText());
                    prepare.setInt(4, Integer.parseInt(genreTF.getText()));
                    LocalDate dateNaissance = date_de_naissanceTF.getValue();

// Formater la date de naissance selon le format requis pour la base de données
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String dateNaissanceFormatee = dateNaissance.format(formatter);

// Utiliser la date de naissance formatée dans votre requête préparée
                    prepare.setString(5, dateNaissanceFormatee);
                    prepare.setInt(6, Integer.parseInt(numtelTF.getText()));
                    prepare.setString(7, emailTF.getText());
                    prepare.setString(8, passwordTF.getText());
                    prepare.setInt(9, Integer.parseInt(interlockTF.getText()));
                    prepare.setString(11, roleTF.getText());


                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(10, uri);
                    //prepare.setString(9, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Ajout avec succés!");
                    alert.showAndWait();

                    addMedecinShowList();
                    addMedecinSelect();

                }
            }

        }catch (Exception e){e.printStackTrace();}
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
        roleTF.setText("");
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
        String sql = "SELECT * FROM global_user";
        connect = MyConnection.getInstance().getCnx();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                Medecin medecin = new Medecin(
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
        medecincol_role.setCellValueFactory(new PropertyValueFactory<>("password"));


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
        roleTF.setText(medecin1.getRole());
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

        String uri =getData.path;
        uri = uri.replace("\\","\\\\");

        String sql = "UPDATE global_user SET cin = '"
                +cinTF.getText()+ "', nom = '"
                +nomTF.getText()+ "', prenom = '"
                +prenomTF.getText()+ "', genre = '"
                +genreTF.getText()+ "', datenaissance = '"
                +date_de_naissanceTF.getValue()+ "', numtel = '"
                +numtelTF.getText()+ "', password = '"
                +passwordTF.getText()+ "', interlock = '"
                +interlockTF.getText()+ "', role = '"
                +roleTF.getText()+ "', image = '"
                +uri+  "' WHERE nom ='" +nomTF.getText()+"'" ;

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
                    || roleTF.getText().isEmpty()
                    || getData.path == null || getData.path == "" ){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            }else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous modifier ces informations!");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Modification avec succés !");
                    alert.showAndWait();

                    addMedecinShowList();
                    ajouterMedecinReset();
                }

            }
        } catch (Exception e){e.printStackTrace();}
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
                    || roleTF.getText().isEmpty()
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

