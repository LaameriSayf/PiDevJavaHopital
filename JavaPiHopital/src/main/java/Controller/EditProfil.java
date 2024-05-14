package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    private RadioButton genreTF1;

    @FXML
    private Button imageTF;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimise;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numtelTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private TextField prenomTF;

    private String emailToEdit;

    public void setEmail(String email){
        emailToEdit = email;
        emailTF.setText(email);

    }
    private Integer CinToEdit;
    public void setCin(Integer Cin){
        CinToEdit = Cin;
        cinTF.setText(String.valueOf(Cin));
    }
    private String nomToEdit;

    public void setNom(String nom){
        nomToEdit = nom;
        nomTF.setText(nom);

    }
    private String prenomToEdit;

    public void setPrenom(String prenom){
        prenomToEdit = prenom;
        prenomTF.setText(prenom);
    }
    private Integer numtelToEdit;
    public void setNumtel(Integer numtel){
        numtelToEdit = numtel;
        numtelTF.setText(String.valueOf(numtel));
    }
    private String passwordToEdit;

    public void setPassword(String password){
        passwordToEdit = password;
        passwordTF.setText(password);

    }







    @FXML
    void annuler(ActionEvent event) {

    }

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void editimage(ActionEvent event) {

    }

    @FXML
    void enregistrer(ActionEvent event) {

    }

    @FXML
    void minimise(ActionEvent event) {

    }




}
