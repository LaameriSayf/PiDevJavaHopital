package Controllers;

import Models.dossiermedical;
import Services.dossiermedicalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Button btnDashBord;






    private final dossiermedicalService dossierService = new dossiermedicalService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        loadDossiers();
    }

    private void initializeTableView() {
        btnGestionDossiers.setOnAction(event -> ouvrirAjoutDossier());
        btnGestionOrdonnances.setOnAction(event -> ouvrirAjoutOrdonnance());
        btnDashBord.setOnAction(event -> ouvrirFrontEnd());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
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




}
