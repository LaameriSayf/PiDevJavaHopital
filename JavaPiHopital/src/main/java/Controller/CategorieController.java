package Controller;

import Interface.ICategorie;
import Model.Categorie;

import Model.Medicament;
import Service.CategorieService;

import Service.MedicamentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.collections.FXCollections.observableArrayList;


public class CategorieController implements Initializable {
    CategorieService cs =new CategorieService();
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
    private Button medicamentBtn;

    @FXML
    private AnchorPane medicamentForm;
    @FXML
    private ResourceBundle resources;
    @FXML
    private TableView<Categorie> table;

    @FXML
    private TableColumn<Categorie, String> colDescription;

    @FXML
    private TableColumn<Categorie, String> colName;

    @FXML
    private TableColumn<Categorie, String> colType;
    @FXML
    private TextField description_cat;

    @FXML
    private TextField nom_cat;

    @FXML
    private TextField type_cat;
    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField nom_med;

    @FXML
    private TextField qte;


    @FXML
    private TextField ref_med;

    @FXML
    private ListView<String> table_med;

    @FXML
    private TextArea textArea;


    @FXML
    private TextField recherche;
    @FXML
    private Button minimize;
    @FXML
    private Button close;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    FileChooser fileChooser=new FileChooser();

    @FXML
    void importerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Définir les filtres pour n'afficher que les fichiers image
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.gif")
        );

        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Charger l'image à partir du fichier sélectionné
            Image image = new Image(selectedFile.toURI().toString());

            // Afficher l'image dans un composant ImageView ou utiliser l'image comme bon vous semble
            // Par exemple, vous pouvez définir l'image dans un objet ImageView
            // ImageView imageView = new ImageView(image);
            // imageView.setFitWidth(200); // Définir la largeur souhaitée
            // imageView.setFitHeight(200); // Définir la hauteur souhaitée
            // Ajouter imageView à votre interface utilisateur

            // Mettre à jour le contenu du TextArea avec le chemin de l'image sélectionnée
            textArea.setText(selectedFile.getPath());
        }
    }



    public CategorieController() {
    }

    public void close(ActionEvent event) {
        // Fermer l'application en fermant la fenêtre principale
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void minimize(ActionEvent event) {
        // Réduire la fenêtre principale
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == categorieBtn) {
            categorieForm.setVisible(true);
            medicamentForm.setVisible(false);

        } else if (event.getSource() == medicamentBtn) {
            categorieForm.setVisible(false);
            medicamentForm.setVisible(true);


        }

    }



    @FXML
    void ajouterCategorie(ActionEvent event) {
        String nom_cat1 = nom_cat.getText();
        String description1_cat = description_cat.getText();
        String type_cat1 = type_cat.getText();

        if (nom_cat1.isEmpty() || description1_cat.isEmpty() || type_cat1.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return; // Sortie de la méthode si un champ est vide
        }

        Categorie c = new Categorie(1, nom_cat1, description1_cat, type_cat1);
        cs.addCategorie(c);

        // Affichage d'une alerte de succès
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Catégorie ajoutée avec succès!");
        successAlert.showAndWait();

        // Effacement des champs de saisie
        nom_cat.clear();
        description_cat.clear();
        type_cat.clear();

        // Rafraîchissement automatique de la table
        refreshTable();
    }

    @FXML
    void AfficherCategorie(ActionEvent event) {
        refreshTable();
    }

    @FXML
    void SupprimerCategorie(ActionEvent event) {
        Categorie categorieSelectionnee = table.getSelectionModel().getSelectedItem();
        if (categorieSelectionnee != null) {
            cs.deleteCategorie(categorieSelectionnee);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Categorie removed successfully!");
            alert.show();
            refreshTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select a category to delete.");
            alert.show();
        }
    }



    void refreshTable() {
        table.getItems().clear();
        table.getItems().addAll(cs.getData());
        table_med.getItems().clear();
        ObservableList<String> medStringList = FXCollections.observableArrayList();
        for (Medicament med : ms.getData()) {
            String item = String.format("ID: %d, Reference: %s, nom medicament: %s," +
                            " Description: %s, Date expiration: %s, date amm: %s," +
                            " etat: %s, Quantite: %s, image: %s",
                    med.getId(), med.getRef_med(), med.getNom_med(),
                    med.getDescription(), med.getDate_expiration(),
                    med.getDate_amm(), med.getEtat(), med.getQte(), med.getImage());
            medStringList.add(item);
        }
        table_med.setItems(medStringList);
    }




    @FXML
    void modifierCategorie(ActionEvent event) {
        Categorie categorieSelectionnee = table.getSelectionModel().getSelectedItem();

        if (categorieSelectionnee != null) {
            String nomcat = nom_cat.getText();
            String typecat = type_cat.getText();
            String description1 = description_cat.getText();

            try {
                if (nomcat.isEmpty() || typecat.isEmpty() || description1.isEmpty()) {
                    throw new Exception("Veuillez remplir tous les champs !");
                }

                // Modifier la catégorie avec les nouvelles valeurs
                categorieSelectionnee.setNom_cat(nomcat);
                categorieSelectionnee.setType_cat(typecat);
                categorieSelectionnee.setDescription_cat(description1);

                // Appeler la méthode pour modifier la catégorie dans le service
                cs.updateCategorie(categorieSelectionnee);

                // Afficher une alerte de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Catégorie modifiée avec succès !");
                alert.showAndWait();

                // Rafraîchir la table
                refreshTable();
            } catch (Exception e) {
                // Afficher une alerte en cas d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Afficher une alerte si aucune catégorie n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une catégorie à modifier.");
            alert.showAndWait();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("."));
        loadAndDisplayData();

        try {
            // Récupération des données de catégorie depuis le service
            List<Categorie> categories = cs.getData();

            // Conversion de la liste de catégories en une liste observable pour liaison avec la table
            ObservableList<Categorie> data = observableArrayList(categories);

            // Vérifie si la référence à la table n'est pas nulle avant d'appeler setItems
            if (table != null) {
                // Liaison des propriétés des catégories aux colonnes de la table
                colName.setCellValueFactory(new PropertyValueFactory<>("nom_cat"));
                colType.setCellValueFactory(new PropertyValueFactory<>("type_cat"));
                colDescription.setCellValueFactory(new PropertyValueFactory<>("description_cat"));

                // Affichage des données dans la table
                table.setItems(data);
            } else {
                System.err.println("La référence à la table est nulle. Vérifiez votre configuration FXML.");
            }
        } catch (Exception e) {
            // Gestion des exceptions: Affichage de l'erreur et capture de l'exception pour éviter les interruptions
            System.err.println("Une exception s'est produite lors de l'initialisation de l'interface utilisateur : " + e.getMessage());
            e.printStackTrace(); // Affichage de la trace de la pile pour le débogage
        }
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

        // Conversion de la quantité en entier
        int qte1 = 0;
        try {
            qte1 = Integer.parseInt(qte.getText());
        } catch (NumberFormatException e) {
            // Gestion de l'erreur de conversion
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("La quantité doit être un entier valide.");
            alert.showAndWait();
            return; // Sortie de la méthode en cas d'erreur
        }

        // Appel de la méthode addMedicament avec les données récupérées
        if (ms != null) {
            Medicament medicament = new Medicament(1, ref1, nom1, date, date1, qte1, description, etat1, image1);
            ms.addMedicament(medicament);

            // Affichage de l'alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Médicament ajouté avec succès !");
            alert.showAndWait();
        } else {
            System.err.println("Échec de l'ajout du médicament!");
        }
        // Effacement des champs de saisie
        nom_med.clear();
        ref_med.clear();
        dateamm.setValue(null);
        dateexp.setValue(null);
        qte.clear();
        desc.clear();
        etat.clear();
        textArea.clear();


        // Rafraîchissement automatique de la table
        refreshTable();
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
    public int get_List_medicament(String chaine){
        String selectedItem =chaine;
        System.out.println("Selected item: " + selectedItem);
        Pattern pattern = Pattern.compile("ID: (\\d+)");
        Matcher matcher = pattern.matcher(selectedItem);

        // Check if the pattern matches and extract the ID
        if (matcher.find()) {
            return  Integer.parseInt(matcher.group(1));

        } else {
            System.out.println("ID not found");
        }return 0;
    }
    private void loadAndDisplayData() {
        ObservableList<String> items = observableArrayList();
        System.out.println("loading");
        // Retrieve data from the database

        List<Medicament> medicamentList = ms.getData();

        // Convert the data into strings and add them to the list
        for (Medicament med : medicamentList) {

            String item = String.format("ID: %d, Reference: %s, nom medicament: %s," +
                            " Description: %s, Date expiration: %s, date amm: %s," +
                            " etat: %s, Quantite: %s, image: %s",
                    med.getId(), med.getRef_med(), med.getNom_med(),
                    med.getDescription(), med.getDate_expiration(),
                    med.getDate_amm(), med.getEtat(), med.getQte(), med.getImage());
            items.add(item);
        }

        // Set the items to the ListView
        table_med.setItems(items);
    }


}
