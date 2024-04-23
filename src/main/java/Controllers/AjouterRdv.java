package Controllers;

import entities.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import services.RdvService;
import services.RendezVousService;
import utils.MyDataBase;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterRdv {

    @FXML
    private Button ajouterButton;
    @FXML
    private  Button supprimerRDB;

    @FXML
    private DatePicker dateLabel;

    @FXML
    private TextField descriptionLabel;

    @FXML
    private TextField heureLabel;

    @FXML
    private Button importFile;

    @FXML
    private ListView<String> fileList;

    @FXML
    private ListView<String> listeRdv;

    private RdvService rdvService;

    @FXML
    private void initialize() {

        // Get the database connection from the MyDataBase class


        final Connection connection = MyDataBase.getInstance().getConnection();
        rdvService = new RendezVousService(connection);
        loadAndDisplayData();

    }
    @FXML
    void supprimerRDV(){
        String selectedItem = listeRdv.getSelectionModel().getSelectedItem();
        System.out.println("Selected item: " + selectedItem);
        Pattern pattern = Pattern.compile("ID: (\\d+)");
        Matcher matcher = pattern.matcher(selectedItem);

        // Check if the pattern matches and extract the ID
        if (matcher.find()) {
            System.out.println(matcher.group(1));  // Extract the ID from the first capturing group
            try {
                rdvService.supprimer(Integer.parseInt(matcher.group(1)));
                if (listeRdv.getItems().isEmpty()==false) {
                    listeRdv.getItems().removeAll();
                    loadAndDisplayData();
//                    System.out.println(listeRdv.getItems());
                }else
                    loadAndDisplayData();

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
                    } else {
            System.out.println("ID not found");
        }
    }
    @FXML
    void ajouterRDV(ActionEvent event) {
        LocalDate date = dateLabel.getValue();
        String heure = heureLabel.getText();
        String description = descriptionLabel.getText();

        if (date == null || heure.isEmpty() || description.isEmpty() || fileList.getItems().isEmpty()) {
            // Check which field is empty and display an alert accordingly
            if (date == null) {
                showAlert("Ajouter la date");
            } else if (heure.isEmpty()) {
                showAlert("Ajouter Heure");
            } else if (description.isEmpty()) {
                showAlert("Ajouter Description");
            } else if (fileList.getItems().isEmpty()) {
                showAlert("Ajouter fichier(s) de liaison");
            }
            return;
        }

        List<String> files = fileList.getItems(); // Get the list of selected file names

        if (rdvService != null) {
            try {
                // Iterate over the list of selected file names and add them to the database
                for (String fileName : files) {
                    RendezVous rendezVous = new RendezVous(description, fileName, date, heure);
                    rdvService.ajouterRDV(rendezVous);
                }
                showAlert("RendezVous ajouté avec succès!");
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
            if (listeRdv.getItems().isEmpty()==false) {
                listeRdv.getItems().removeAll();
                loadAndDisplayData();
            }else
                loadAndDisplayData();
        } else {
            System.err.println("RdvService échec!");
        }
    }

    @FXML
    void importFile (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(importFile.getScene().getWindow());

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                fileList.getItems().add(file.getName());
            }
        }
    }
    private void loadAndDisplayData() {
        ObservableList<String> items = FXCollections.observableArrayList();
        System.out.println("loading");
        try {
            // Retrieve data from the database
            List<RendezVous> rendezVousList = rdvService.recuperer();

            // Convert the data into strings and add them to the list
            for (RendezVous rendezVous : rendezVousList) {
                String item = String.format("ID: %d, Description: %s, File: %s, Date: %s, Heure: %s",
                        rendezVous.getId(), rendezVous.getDescription(), rendezVous.getFile(),
                        rendezVous.getDaterdv(), rendezVous.getHeurerdv());
                items.add(item);
            }

            // Set the items to the ListView
            listeRdv.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ Vide");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
