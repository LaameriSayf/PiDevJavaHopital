package Controller;

import Model.dossiermedical;
import Service.dossiermedicalService;
import Test.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllDossier implements Initializable {
    @FXML
    private TableView<dossiermedical> dossierTableView;

    @FXML
    private TableColumn<dossiermedical, Integer> idColumn;
    @FXML
    private TableColumn<dossiermedical, Integer> numdossierColumn;
    @FXML
    private TableColumn<dossiermedical, String> resultatExamenColumn;

    @FXML
    private TableColumn<dossiermedical, String> dateCreationColumn;

    @FXML
    private TableColumn<dossiermedical, String> antecedentsColumn;

    @FXML
    private TableColumn<dossiermedical, String> imageColumn;
    @FXML
    private Button btnGestionDossiers;

    @FXML
    private Button btnGestionOrdonnances;
    @FXML
    private Button OCRR;
    @FXML
    private Button btnDashBord;
    @FXML
    private Button PDF;


    @FXML
    private TextField searchBar;


    private final dossiermedicalService dossierService = new dossiermedicalService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        loadDossiers();
        search();
    }

    private void initializeTableView() {
        btnGestionDossiers.setOnAction(event -> ouvrirAjoutDossier());
        btnGestionOrdonnances.setOnAction(event -> ouvrirAllOrdonnance());
        OCRR.setOnAction(event -> ouvrirOCR());
        btnDashBord.setOnAction(event -> ouvrirFrontEnd());
        numdossierColumn.setCellValueFactory(new PropertyValueFactory<>("numdossier"));
        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        resultatExamenColumn.setCellValueFactory(new PropertyValueFactory<>("resultatexamen"));
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("date_creation"));
        antecedentsColumn.setCellValueFactory(new PropertyValueFactory<>("antecedentspersonelles"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        // Créer une colonne pour les actions avec des ImageView
        TableColumn<dossiermedical, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView modifierIcon = new ImageView(new Image(getClass().getResourceAsStream("/pictures/editing.png")));
            private final ImageView ajouterOrdonnanceIcon = new ImageView(new Image(getClass().getResourceAsStream("/pictures/addfile.png")));
            private final ImageView afficherIcon = new ImageView(new Image(getClass().getResourceAsStream("/pictures/show.png")));

            {
                // Gestion des événements pour chaque icône
                modifierIcon.setOnMouseClicked(event -> {
                    dossiermedical selectedDossier = getTableView().getItems().get(getIndex());
                    if (selectedDossier != null) {
                        openUpdateDossierDialog(selectedDossier);
                    }
                });

                ajouterOrdonnanceIcon.setOnMouseClicked(event -> {
                    dossiermedical selectedDossier = getTableView().getItems().get(getIndex());
                    if (selectedDossier != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutOrdonnance.fxml"));
                            Parent parent = loader.load();
                            AjoutOrdonnance AjoutOrdonnanceController = loader.getController();
                            AjoutOrdonnanceController.setSelectedDossier(selectedDossier);
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();

                        } catch (IOException ex) {
                            Logger.getLogger(AllDossier.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                afficherIcon.setOnMouseClicked(event -> {
                    dossiermedical selectedDossier = getTableView().getItems().get(getIndex());
                    if (selectedDossier != null) {
                        afficherDetailsDossier(selectedDossier.getId());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox icons = new HBox(modifierIcon, ajouterOrdonnanceIcon, afficherIcon);
                    icons.setSpacing(10);
                    setGraphic(icons);
                }
            }
        });

        dossierTableView.getColumns().add(actionColumn);
    }

    private void loadDossiers() {
        try {
            ObservableList<dossiermedical> dossierList = FXCollections.observableArrayList(dossierService.getData());
            dossierTableView.setItems(dossierList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateDossierDialog(dossiermedical selectedDossier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateDossierMedical.fxml"));
            Parent parent = loader.load();

            UpdateDossierMedical controller = loader.getController();
            controller.setDossierModifier(selectedDossier);

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AllDossier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void afficherDetailsDossier(int dossierId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showDetailsDossier.fxml"));
            Parent parent = loader.load();

            showDetailsDossier controller = loader.getController();
            controller.initializeDossier(dossierId);

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AllDossier.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        dossiermedical selectedDossier = dossierTableView.getSelectionModel().getSelectedItem();

        if (selectedDossier != null) {
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
                    pd.GeneratePdf(file.getAbsolutePath(), selectedDossier, selectedDossier.getId());
                    System.out.println("PDF saved successfully.");

                } catch (Exception ex) {
                    Logger.getLogger(AllDossier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            showAlert("Please select a Dossier to generate PDF.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void search() {
        // Création d'une liste filtrée initialisée avec la liste de tous les dossiers
        FilteredList<dossiermedical> filteredData = new FilteredList<>(dossierTableView.getItems(), e -> true);

        // Ajout d'un écouteur sur la propriété text de la barre de recherche pour mettre à jour le prédicat à chaque modification de texte
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(dossier -> {
                // Si le texte de recherche est vide, toutes les dossiers sont affichées
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Convertir le texte de recherche en minuscules
                String lowerCaseFilter = newValue.toLowerCase();

                // Vérifiez si le texte de recherche correspond à une des propriétés du dossier ou au numéro de dossier
                return dossier.getAntecedentspersonelles().toLowerCase().contains(lowerCaseFilter)
                        || dossier.getResultatexamen().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(dossier.getNumdossier()).contains(newValue);
            });
        });

        // Création d'une liste triée à partir de la liste filtrée
        SortedList<dossiermedical> sortedData = new SortedList<>(filteredData);
        // Liaison du comparateur de la liste triée avec le comparateur de la table des dossiers
        sortedData.comparatorProperty().bind(dossierTableView.comparatorProperty());
        // Mise à jour de la table des dossiers avec la liste triée
        dossierTableView.setItems(sortedData);
    }
    private void ouvrirAllOrdonnance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllOrdonnance.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void ouvrirOCR() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OCR.fxml"));
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


