package Controller;

import Model.ordonnance;
import Service.ordonnanceService;
import Test.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllOrdonnance implements Initializable {


    @FXML
    private TableView<ordonnance> ordonnanceTableView;

    @FXML
    private TableColumn<ordonnance, Integer> idColumn;

    @FXML
    private TableColumn<ordonnance, String> datePrescriptionColumn;

    @FXML
    private TableColumn<ordonnance, String> renouvellementColumn;

    @FXML
    private TableColumn<ordonnance, String> medecamentPrescritColumn;

    @FXML
    private TableColumn<ordonnance, String> adresseColumn;

    @FXML
    private TableColumn<ordonnance, Void> actionColumn; // Nouvelle colonne pour l'action
    @FXML
    private Button btnGestionDossiers;

    @FXML
    private Button btnGestionOrdonnances;
    @FXML
    private Button btnDashBord;

    @FXML
    private Button pdf;

    @FXML
    private Button excel;



    private final ordonnanceService ordonnanceService = new ordonnanceService();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableView();
        loadOrdonnances();
    }

    private void initializeTableView() {
        btnDashBord.setOnAction(event -> ouvrirFrontEnd());
        btnGestionDossiers.setOnAction(event -> ouvrirAjoutDossier());
        btnGestionOrdonnances.setOnAction(event -> ouvrirAjoutOrdonnance());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        datePrescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dateprescription"));
        renouvellementColumn.setCellValueFactory(new PropertyValueFactory<>("renouvellement"));
        medecamentPrescritColumn.setCellValueFactory(new PropertyValueFactory<>("medecamentprescrit"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        setupActionCellFactory();
    }

    private void loadOrdonnances() {
        try {
            ObservableList<ordonnance> ordonnanceList = FXCollections.observableArrayList(ordonnanceService.getData());
            ordonnanceTableView.setItems(ordonnanceList);
        } catch (SQLException e) {
            afficherAlerteErreur("Erreur lors du chargement des ordonnances : " + e.getMessage());
        }
    }

    private void setupActionCellFactory() {
        Callback<TableColumn<ordonnance, Void>, TableCell<ordonnance, Void>> cellFactory = (param) -> new TableCell<>() {
            private final ImageView modifierIcon = new ImageView(new Image(getClass().getResourceAsStream("/pictures/editing.png")));
            private final ImageView supprimerIcon = new ImageView(new Image(getClass().getResourceAsStream("/pictures/delete.png")));

            {
                modifierIcon.setFitHeight(16); // Ajuster la taille de l'icône
                modifierIcon.setFitWidth(16);
                supprimerIcon.setFitHeight(16);
                supprimerIcon.setFitWidth(16);

                modifierIcon.setOnMouseClicked(event -> {
                    ordonnance ordonnance = getTableView().getItems().get(getIndex());
                    openUpdateOrdonnanceView(ordonnance);
                });

                supprimerIcon.setOnMouseClicked(event -> {
                    ordonnance ordonnance = getTableView().getItems().get(getIndex());
                    try {
                        supprimerOrdonnance(ordonnance.getId());
                    } catch (SQLException e) {
                        afficherAlerteErreur("Erreur lors de la suppression de l'ordonnance : " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Créer un conteneur pour les icônes
                    HBox iconsBox = new HBox(modifierIcon, supprimerIcon);
                    setGraphic(iconsBox);
                }
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void openUpdateOrdonnanceView(ordonnance selectedOrdonnance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOrdonnance.fxml"));
            Parent root = loader.load();

            UpdateOrdonnance controller = loader.getController();
            controller.setOrdonnanceModifier(selectedOrdonnance);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            afficherAlerteErreur("Erreur lors du chargement de la vue de modification : " + e.getMessage());
        }
    }

    private void supprimerOrdonnance(int ordonnanceId) throws SQLException {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette ordonnance ?");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ordonnanceService.supprimer(ordonnanceId);
                    loadOrdonnances(); // Recharger la liste après la suppression
                    afficherAlerteInformation("Ordonnance supprimée avec succès !");
                } catch (SQLException e) {
                    afficherAlerteErreur("Erreur lors de la suppression de l'ordonnance : " + e.getMessage());
                }
            }
        });
    }



    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void afficherAlerteInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void ouvrirAjoutDossier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutDossier.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void ouvrirAjoutOrdonnance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutOrdonnance.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void ouvrirFrontEnd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void PDF(ActionEvent event) {
        // Récupérer le dossier sélectionnée dans la liste
      ordonnance selectedOrdonnance = ordonnanceTableView.getSelectionModel().getSelectedItem();

        if (ordonnanceTableView != null) {
            // Créer un sélecteur de fichiers pour choisir l'emplacement où enregistrer le PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.setInitialFileName("MesInformations.pdf");

            // Afficher la boîte de dialogue pour enregistrer le fichier et obtenir le chemin du fichier choisi
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                // Si l'utilisateur a choisi un emplacement, générez le PDF et enregistrez-le à cet emplacement
                pdf pd = new pdf();
                try {
                    pd.GeneratePdfO(file.getAbsolutePath(), selectedOrdonnance, selectedOrdonnance.getId());
                    System.out.println("PDF saved successfully.");

                } catch (Exception ex) {
                    Logger.getLogger(AllDossier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            showAlert("Please select a Dossier to generate PDF.");
        }


    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(s);
        alert.showAndWait();

    }
    public void generateExcelFile(ActionEvent event) {
        List<ordonnance> ordonnanceList = ordonnanceTableView.getItems();

        if (!ordonnanceList.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.setInitialFileName("Ordonnances.xlsx");

            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                ExcelWriter excelWriter = new ExcelWriter();
                excelWriter.writeOrdonnancesToExcel(ordonnanceList, file.getAbsolutePath());
                showAlert("Excel file saved successfully.");
            }
        } else {
            showAlerte("No data available to generate Excel file.");
        }
    }

    // Méthode pour afficher une boîte de dialogue d'alerte
    private void showAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private Button logout_btn;
    @FXML
    void logout(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
