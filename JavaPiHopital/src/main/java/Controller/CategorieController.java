package Controller;

import Interface.ICategorie;
import Model.Categorie;

import Model.Medicament;
import Service.CategorieService;

import Service.MedicamentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;


public class CategorieController implements Initializable {
    CategorieService cs =new CategorieService();
    MedicamentService ms =new MedicamentService();
    @FXML
    private AnchorPane MedicamentForm;

    @FXML
    private ComboBox<String> etat;

    @FXML
    private Button categorieBtn;

    @FXML
    private AnchorPane categorieForm;

    @FXML
    private TextField recherche;

    @FXML
    private DatePicker dateamm;

    @FXML
    private DatePicker dateexp;

    @FXML
    private TextField desc;


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

            File destinationDirectory = new File("C:\\Users\\ASUS\\Desktop\\PiDevJava\\PiDevJavaHopital\\JavaPiHopital\\src\\main\\resources\\uploads");

            Path destinationPath = null;
            try {
                // Copier le fichier sélectionné vers le répertoire de destination
                Path sourcePath = selectedFile.toPath();
                destinationPath = new File(destinationDirectory, selectedFile.getName()).toPath();
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // Afficher un message de succès
                showAlert("Image uploaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                // Afficher un message d'erreur si le téléchargement échoue
                showAlert("Failed to upload image.");
            }

            // Mettre à jour le contenu du TextArea avec le chemin de l'image sélectionnée
            textArea.setText(destinationPath.toString());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

        // Vérifier si les champs sont vides
        if (nom_cat1.isEmpty() || description1_cat.isEmpty() || type_cat1.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.").showAndWait();
            return; // Sortir de la méthode si un champ est vide
        }

        // Vérifier si le nom de la catégorie contient uniquement des lettres
        if (!isValidName(nom_cat1)) {
            new Alert(Alert.AlertType.WARNING, "Le nom de la catégorie ne doit contenir que des lettres.").showAndWait();
            return;
        }

        // Vérifier si le type de la catégorie contient uniquement des lettres
        if (!isValidName(type_cat1)) {
            new Alert(Alert.AlertType.WARNING, "Le type de la catégorie ne doit contenir que des lettres.").showAndWait();
            return;
        }
        // Vérifier si la description contient au moins 3 caractères
        if (description1_cat.length() < 3) {
            new Alert(Alert.AlertType.WARNING, "La description de la catégorie doit contenir au moins 3 caractères.").showAndWait();
            return; // Sortir de la méthode si la description est trop courte
        }

        // Ajouter la nouvelle catégorie
        Categorie c = new Categorie(1, nom_cat1, description1_cat, type_cat1);
        cs.addCategorie(c);

        // Affichage d'une alerte de succès
        new Alert(Alert.AlertType.INFORMATION, "Catégorie ajoutée avec succès!").showAndWait();

        // Effacement des champs de saisie
        nom_cat.clear();
        description_cat.clear();
        type_cat.clear();

        // Rafraîchissement automatique de la table
        refreshTable();
    }

    private boolean isValidName(String name) {
        // Vérifier si le nom contient uniquement des lettres
        return name.matches("[a-zA-Z]+");
    }

    @FXML
    void AfficherCategorie(ActionEvent event) {

        nom_med.clear();
        ref_med.clear();
        dateamm.setValue(null);
        dateexp.setValue(null);
        qte.clear();
        desc.clear();
        etat.getSelectionModel().clearSelection();
        textArea.clear();
        refreshTable();    }

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


    @FXML
    void rechercher(ActionEvent event) {
        table.getItems().clear();
        table.getItems().addAll(searchList(recherche.getText(), cs.getData()));
    }

    private List<Categorie> searchList(String searchWords, List<Categorie> data) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return data.stream().filter(input -> {
            boolean matchNom = false;
            boolean matchType = false;
            boolean matchDescription = false;

            // Vérifie si le nom, le type ou la description contient l'un des mots de recherche
            for (String word : searchWordsArray) {
                if (input.getNom_cat().toLowerCase().contains(word.toLowerCase())) {
                    matchNom = true;
                }
                if (input.getType_cat().toLowerCase().contains(word.toLowerCase())) {
                    matchType = true;
                }
                if (input.getDescription_cat().toLowerCase().contains(word.toLowerCase())) {
                    matchDescription = true;
                }
            }

            // Renvoie vrai si le nom, le type ou la description correspond à l'un des mots de recherche
            return matchNom || matchType || matchDescription;
        }).collect(Collectors.toList());
    }

    private List<String> searchList1(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
etat.setItems(FXCollections.observableArrayList("En Stock","Non disponible"));
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
        table.setOnMouseClicked(event -> {
            // Vérifier si un clic de souris a été effectué avec le bouton gauche
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                // Récupérer la ligne sélectionnée
                Categorie categorieSelectionnee = table.getSelectionModel().getSelectedItem();

                // Vérifier si la ligne sélectionnée n'est pas nulle
                if (categorieSelectionnee != null) {
                    // Afficher les valeurs de la ligne sélectionnée dans les zones de texte
                    nom_cat.setText(categorieSelectionnee.getNom_cat());
                    description_cat.setText(categorieSelectionnee.getDescription_cat());
                    type_cat.setText(categorieSelectionnee.getType_cat());
                }
            }
        });
        table_med.setOnMouseClicked(this::handle);


    }
    // Méthode pour extraire l'ID à partir de la ligne de médicament sélectionnée
    private int getIdFromLine(String line) {
        Pattern pattern = Pattern.compile("ID: (\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private String getRefFromLine(String line) {
        String[] parts = line.split(", Reference: ");
        if (parts.length > 1) {
            return parts[1].split(",")[0].trim();
        }
        return "";
    }

    // Méthode pour extraire le nom à partir de la ligne de médicament sélectionnée
    private String getNomFromLine(String line) {
        String[] parts = line.split("nom medicament: ");
        if (parts.length > 1) {
            return parts[1].split(",")[0].trim();
        }
        return "";
    }

    // Méthode pour extraire la description à partir de la ligne de médicament sélectionnée
    private String getDescriptionFromLine(String line) {
        String[] parts = line.split("Description: ");
        if (parts.length > 1) {
            return parts[1].split(",")[0].trim();
        }
        return "";
    }
    private LocalDate getDateAmmFromLine(String line) {
        Pattern pattern = Pattern.compile("date amm: (\\d{4}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return LocalDate.parse(matcher.group(1));
        } else {
            return null; // Gérer le cas où la date d'AMM n'est pas trouvée
        }
    }

    // Méthode pour extraire la date d'expiration à partir de la ligne de médicament sélectionnée
    private LocalDate getDateExpFromLine(String line) {
        Pattern pattern = Pattern.compile("Date expiration: (\\d{4}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return LocalDate.parse(matcher.group(1));
        } else {
            return null; // Gérer le cas où la date d'expiration n'est pas trouvée
        }
    }

    // Méthode pour extraire la quantité à partir de la ligne de médicament sélectionnée
    private int getQteFromLine(String line) {
        Pattern pattern = Pattern.compile("Quantite: (\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return 0; // Gérer le cas où la quantité n'est pas trouvée
        }
    }

    // Méthode pour extraire l'état à partir de la ligne de médicament sélectionnée
    private String getEtatFromLine(String line) {
        Pattern pattern = Pattern.compile("etat: (.*?),");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return ""; // Gérer le cas où l'état n'est pas trouvé
        }
    }

    // Méthode pour extraire le chemin de l'image à partir de la ligne de médicament sélectionnée
    private String getImageFromLine(String line) {
        Pattern pattern = Pattern.compile("image: (.+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return ""; // Gérer le cas où le chemin de l'image n'est pas trouvé
        }
    }
    @FXML
    void AjouterMed(ActionEvent event) {
        LocalDate date = dateamm.getValue();
        LocalDate date1 = dateexp.getValue();
        String description = desc.getText();
        String nom1 = nom_med.getText();
        String ref1 = ref_med.getText();
        String etat1 = etat.getSelectionModel().getSelectedItem();
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
        etat.getSelectionModel().clearSelection();
        textArea.clear();


        // Rafraîchissement automatique de la table
        refreshTable();
    }




    @FXML
    void afficherMed(ActionEvent event) {

    }

    @FXML
    void modifierMed(ActionEvent event) {
        int selectedIndex = table_med.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            // Récupérer le médicament sélectionné
            Medicament medSelected = ms.getData().get(selectedIndex);

            String nomMed = nom_med.getText();
            String refMed = ref_med.getText();
            LocalDate dateAmm = dateamm.getValue();
            LocalDate dateExp = dateexp.getValue();
            String description = desc.getText();
            String etatMed = etat.getSelectionModel().getSelectedItem();
            String image = textArea.getText();
            int qteMed = Integer.parseInt(qte.getText());

            try {
                if (nomMed.isEmpty() || refMed.isEmpty() || dateAmm == null || dateExp == null ||
                        description.isEmpty() || etatMed.isEmpty() || image.isEmpty()) {
                    throw new Exception("Veuillez remplir tous les champs !");
                }

                // Mettre à jour les valeurs du médicament sélectionné
                medSelected.setNom_med(nomMed);
                medSelected.setRef_med(refMed);
                medSelected.setDate_amm(dateAmm);
                medSelected.setDate_expiration(dateExp);
                medSelected.setDescription(description);
                medSelected.setEtat(etatMed);
                medSelected.setImage(image);
                medSelected.setQte(qteMed);

                // Appeler la méthode pour modifier le médicament dans le service
                ms.updateMedicament(medSelected);

                // Afficher une alerte de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Médicament modifié avec succès !");
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
            // Afficher une alerte si aucun médicament n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner un médicament à modifier.");
            alert.showAndWait();
        }
    }


    @FXML
    void supprimerMed(ActionEvent event) {
        // Récupérer l'index de l'élément sélectionné dans la liste
        int selectedIndex = table_med.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            // Récupérer l'élément correspondant à l'index sélectionné
            Medicament medLineSelected = ms.getData().get(selectedIndex);

            // Supprimer le médicament sélectionné
            ms.deleteMedicament(medLineSelected);

            // Afficher une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Medicament removed successfully!");
            alert.show();

            // Rafraîchir la table
            refreshTable();
        } else {
            // Afficher une alerte si aucun élément n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select a medicament to delete.");
            alert.show();
        }
        nom_med.clear();
        ref_med.clear();
        dateamm.setValue(null);
        dateexp.setValue(null);
        qte.clear();
        desc.clear();
        etat.getSelectionModel().clearSelection();
        textArea.clear();
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


    private void handle(MouseEvent event) {
        // Vérifier si un clic de souris a été effectué avec le bouton gauche
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
            // Récupérer la ligne sélectionnée
            String medLineSelected = table_med.getSelectionModel().getSelectedItem();

            // Vérifier si la ligne sélectionnée n'est pas nulle
            if (medLineSelected != null) {
                // Extraire les valeurs de la ligne sélectionnée
                int id = getIdFromLine(medLineSelected);
                String ref = getRefFromLine(medLineSelected);
                String nom = getNomFromLine(medLineSelected);
                String description = getDescriptionFromLine(medLineSelected);
                LocalDate dateAmm = getDateAmmFromLine(medLineSelected);
                LocalDate dateExp = getDateExpFromLine(medLineSelected);
                int qte1 = getQteFromLine(medLineSelected);
                String etata = getEtatFromLine(medLineSelected);
                String img = getImageFromLine(medLineSelected);
                // Extraire les autres valeurs de la ligne sélectionnée ici ...

                // Afficher les valeurs dans les zones de texte
                nom_med.setText(nom);
                ref_med.setText(ref);
                desc.setText(description);
                dateamm.setValue(dateAmm);
                dateexp.setValue(dateExp);
                qte.setText(String.valueOf(qte1));
                etat.setValue(etata);
                textArea.setText(img);
            }
        }
    }
}
