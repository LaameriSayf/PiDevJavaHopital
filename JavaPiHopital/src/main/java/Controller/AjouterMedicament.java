package Controller;

import Model.Medicament;
import Service.CategorieService;
import Service.MedicamentService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AjouterMedicament implements Initializable {

    MedicamentService ms =new MedicamentService();
    @FXML
    private AnchorPane MedicamentForm;

    @FXML
    private Button categorieBtn;

    @FXML
    private AnchorPane categorieForm;



    @FXML
    private DatePicker dateamm;

    @FXML
    private DatePicker dateexp;

    @FXML
    private TextField desc;



    @FXML
    private TextField etat;

    @FXML
    private Button i;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button medicamentBtn;

    @FXML
    private AnchorPane medicamentForm;


    @FXML
    private TextField nom_med;

    @FXML
    private TextField qte;


    @FXML
    private TextField ref_med;

    @FXML
    private ListView<Medicament> table_med;

    @FXML
    private TextArea textArea;

FileChooser fileChooser=new FileChooser();
    @FXML
    void getText(MouseEvent event) {
File file= fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            while ((scanner.hasNextLine())){
                textArea.appendText(scanner.nextLine()+"\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void switchForm(ActionEvent event) {
        if (event.getSource() == medicamentBtn) {
            categorieForm.setVisible(false);
            medicamentForm.setVisible(true);

        } else if (event.getSource() == categorieBtn) {
            categorieForm.setVisible(true);
            medicamentForm.setVisible(false);


        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
fileChooser.setInitialDirectory(new File("."));

    }

    @FXML
    void AjouterMed(ActionEvent event) {
        LocalDate date = dateamm.getValue();
        LocalDate date1 = dateexp.getValue();
        String description = desc.getText();
        String nom1 = nom_med.getText();
        String ref1 = ref_med.getText();
        String etat1 = etat.getText();
        String image1 = textArea.getText();
        int qte1 = Integer.parseInt(qte.getText());
        ObservableList<Medicament> files = table_med.getItems();

        if (ms != null) {
            for (Medicament fileName : files) {
                Medicament medicament = new Medicament(fileName.getId(), ref1, nom1, date, date1, qte1, description, etat1, image1);
                ms.addMedicament(medicament);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Médicament ajouté avec succès !");
            alert.showAndWait();
        } else {
            System.err.println("Médicament échec!");
        }
    }



    @FXML
    void afficherMed(ActionEvent event) {

    }

    @FXML
    void modifierMed(ActionEvent event) {

    }

    @FXML
    void supprimerMed(ActionEvent event) {

    }




}
