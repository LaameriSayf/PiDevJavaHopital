package Controller;

import Interface.ICategorie;
import Model.Categorie;

import Model.Medicament;
import Service.CategorieService;

import Service.MedicamentService;
import javafx.beans.binding.Bindings;
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
import javafx.scene.chart.*;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;


public class CategorieController implements Initializable {
    CategorieService cs =new CategorieService();
    MedicamentService ms =new MedicamentService();
    @FXML
    private Pagination PaginationCat;


    @FXML
    private BarChart<String,Integer> BarStat;
    @FXML
    private PieChart PieStat;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private PieChart PieStatCat;

    @FXML
    private ListView<String> ListTop;

    @FXML
    private AnchorPane statistiqueForm;

    @FXML
    private Button StatistiqueBtn;

    @FXML
    private ComboBox<String> ComboCategorie;
    @FXML
    private AnchorPane MedicamentForm;

    @FXML
    private ComboBox<String> etat;

    @FXML
    private ImageView btnD;

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
    private int itemsPage =10;

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
            statistiqueForm.setVisible(false); // Assurez-vous de masquer le formulaire de statistiques si nécessaire

        } else if (event.getSource() == medicamentBtn) {
            categorieForm.setVisible(false);
            medicamentForm.setVisible(true);
            statistiqueForm.setVisible(false); // Assurez-vous de masquer le formulaire de statistiques si nécessaire

        } else if (event.getSource() == StatistiqueBtn) {
            categorieForm.setVisible(false);
            medicamentForm.setVisible(false);
            statistiqueForm.setVisible(true);
        }
    }


//Send mail
public static void sendEmail(String recipientEmail) {
    // Sender's email
    final String senderEmail = "batoutbata5@gmail.com";
    final String senderName = "Team MediConnect"; // Your desired sender name
    if (recipientEmail == null || recipientEmail.isEmpty()) {
        System.out.println("Recipient email is null or empty. Cannot send email.");
        return;
    }
    // Password
    final String password = "ialgvzhizvvrwozy";

    // SMTP server properties
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");

    Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(senderEmail, password);
        }
    });

    try {
        // Create a MimeMessage object
        MimeMessage message = new MimeMessage(session);

        // Set the sender's name and email address
        message.setFrom(new InternetAddress(senderEmail, senderName));

        // Set the recipient's email address
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

        // Set the email subject
        message.setSubject("Thank you for your feedback");

        // Create a MimeMultipart object
        Multipart multipart = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();
        String htmlContent = "<p style=\"font-family: Arial, sans-serif; font-size: 14px;\">Cher patient,</p>"
                + "<p style=\"font-family: Arial, sans-serif; font-size: 14px;\">Votre rendez-vous a été envoyé avec succès.</p>"
                + "<p style=\"font-family: Arial, sans-serif; font-size: 14px;\">À bientôt,<br/>Hopital Militaire de Tunis<br/>@MediConnect</p>"
                ;
        htmlPart.setContent(htmlContent, "text/html");
        multipart.addBodyPart(htmlPart);

        // Create and add the image part of the message
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource source = new FileDataSource("C:\\Users\\ASUS\\Desktop\\PiDevJava\\PiDevJavaHopital\\JavaPiHopital\\src\\main\\resources\\uploads\\logo1.png"); // Replace with the path to your image file
        imagePart.setDataHandler(new DataHandler(source));
        imagePart.setHeader("Content-ID", "<image>");
        multipart.addBodyPart(imagePart);

        // Set the content of the message to the multipart object
        message.setContent(multipart);

        // Send the message
        Transport.send(message);

        System.out.println("Email sent successfully!");

    } catch (MessagingException | IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    void ajouterCategorie(ActionEvent event) {
        String nom_cat1 = nom_cat.getText();
        String description1_cat = description_cat.getText();
        String type_cat1 = type_cat.getText();
        String mail = "abdessalemchaouch9217@gmail.com";

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
        Categorie c = new Categorie(1, nom_cat1, type_cat1, description1_cat);
        cs.addCategorie(c);
        // Ajouter la nouvelle catégorie à la liste observable
        allCategories.add(c);
        sendEmail(mail);

        // Affichage d'une alerte de succès
        new Alert(Alert.AlertType.INFORMATION, "Catégorie ajoutée avec succès!").showAndWait();

        // Effacement des champs de saisie
        nom_cat.clear();
        description_cat.clear();
        type_cat.clear();


        // Rafraîchissement automatique de la table
        refreshTable();

        // Mettre à jour la pagination
        int newPageCount = (int) Math.ceil((double) (allCategories.size() + 1) / itemsPage);
        PaginationCat.setPageCount(newPageCount);
        PaginationCat.setCurrentPageIndex(newPageCount - 1);
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
            // Supprimer la catégorie sélectionnée
            cs.deleteCategorie(categorieSelectionnee);

            // Supprimer la catégorie de la liste observable
            allCategories.remove(categorieSelectionnee);

            // Afficher une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Categorie removed successfully!");
            alert.show();

            // Rafraîchir la table
            refreshTable();

            // Mettre à jour la pagination
            int newPageCount = (int) Math.ceil((double) allCategories.size() / itemsPage);
            PaginationCat.setPageCount(newPageCount);
            PaginationCat.setCurrentPageIndex(Math.min(PaginationCat.getCurrentPageIndex(), newPageCount - 1));

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
            String categoryName = med.getCategorie() != null ? med.getCategorie().getNom_cat() : "null";
            String item = String.format("ID: %d, Catégorie: %s,Reference: %s, nom medicament: %s," +
                            " Description: %s, Date expiration: %s, date amm: %s," +
                            " etat: %s, Quantite: %s, image: %s",
                    med.getId(),categoryName, med.getRef_med(), med.getNom_med(),
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


    /* @FXML
    void rechercher(ActionEvent event) {
        table.getItems().clear();
        table.getItems().addAll(searchList(recherche.getText(), cs.getData()));
    }*/

    /*private List<Categorie> searchList(String searchWords, List<Categorie> data) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return data.stream().filter(input -> {
            boolean matchNom = false;
            boolean matchType = false;
            boolean matchDescription = false;

            // Vérifie si le nom, le type ou la description contient l'un des mots derecherche
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

            // Renvoie vrai si le nom, le type ou la description correspond à l'un des mots derecherche
            return matchNom || matchType || matchDescription;
        }).collect(Collectors.toList());
    }*/

    /*private List<String> searchList1(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }*/
    private List<Medicament> obtenirTop5MedicamentsProchesExpiration() {
        // Supposons que vous avez une liste de médicaments quelconque
        List<Medicament> tousLesMedicaments = ms.getData();

        // Trier la liste en fonction de la date d'expiration, en ordre croissant
        Collections.sort(tousLesMedicaments, Comparator.comparing(Medicament::getDate_expiration));

        // Prendre les cinq premiers éléments de la liste triée
        int nombreDeMedicaments = Math.min(tousLesMedicaments.size(), 5);
        return tousLesMedicaments.subList(0, nombreDeMedicaments);
    }
    private ObservableList<Categorie> allCategories = FXCollections.observableArrayList();
    private ObservableList<Categorie> displayedCategories = FXCollections.observableArrayList();
    @FXML
    public void addcategorieTable() {
        // Load all categories if not already loaded
        if (allCategories.isEmpty()) {
            loadCategories();
        }

        // Calculate total number of pages
        int pageCount = (int) Math.ceil((double) allCategories.size() / itemsPage);

        // Set up pagination
        PaginationCat.setPageCount(pageCount);
        PaginationCat.setCurrentPageIndex(0); // Set to first page
    }

    private void loadCategories() {
        List<Categorie> all = cs.getData();
        allCategories.addAll(all);
    }

    private void createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPage;
        int toIndex = Math.min(fromIndex + itemsPage, allCategories.size());
        displayedCategories.setAll(allCategories.subList(fromIndex, toIndex));
        table.setItems(displayedCategories);
    }

    @FXML
    public void searchCategories(String searchText) {
        if (searchText.isEmpty()) {
            refreshTable();
        } else {
            // Création d'un objet Categorie pour chaque critère de recherche
            Categorie searchCriteria = new Categorie();
            searchCriteria.setNom_cat(searchText); // Recherche par nom de catégorie
            searchCriteria.setType_cat(searchText); // Recherche par type de catégorie
            searchCriteria.setDescription_cat(searchText); // Recherche par description de catégorie

            // Envoyer une requête au backend pour récupérer les résultats de la recherche
            ArrayList<Categorie> searchResults = cs.getBytitreDescription(searchCriteria);

            // Mettre les résultats de la recherche dans une liste observable
            ObservableList<Categorie> searchResultsList = FXCollections.observableArrayList(searchResults);

            // Mettre à jour la table avec les résultats de la recherche
            table.setItems(searchResultsList);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Recherche
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            searchCategories(newValue);
        });


        //Pagination
        // Chargez toutes les catégories si elles ne sont pas déjà chargées
        PaginationCat.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> createPage(newIndex.intValue()));
        loadCategories();


        // Calculez le nombre total de pages
        int pageCount = (int) Math.ceil((double) allCategories.size() / itemsPage);
        PaginationCat.setPageCount(pageCount);
        PaginationCat.setCurrentPageIndex(0); // Définir sur la première page





        //Statistique
        // Obtention des Top 5 des médicaments les plus proches de la date d'expiration
        List<Medicament> top5Medicaments = obtenirTop5MedicamentsProchesExpiration();

        // Création d'une liste formatée pour les éléments à afficher dans la ListView
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Medicament medicament : top5Medicaments) {
            String item = "Nom du médicament : " + medicament.getNom_med() + ", Date d'expiration : " + medicament.getDate_expiration();
            items.add(item);
        }

        // Définition de la liste formatée comme source de données de la ListView
        ListTop.setItems(items);
        try {
            // Récupération des données des médicaments depuis le service
            List<Medicament> medicaments = ms.getData();
            List<Categorie> categories = cs.getData();

            // Création d'une série de données pour le graphique à barres
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Quantité de médicaments");


            // Parcours des données des médicaments pour obtenir la quantité de chaque médicament
            for (Medicament medicament : medicaments) {
                // Ajout d'un point de données au graphique à barres avec le nom du médicament et sa quantité
                series.getData().add(new XYChart.Data<>(medicament.getNom_med(), medicament.getQte()));

            }

            // Ajout de la série de données au graphique à barres
            BarStat.getData().add(series);
            // Création d'une série de données pour le diagramme circulaire
            ObservableList<PieChart.Data> pieChartData = observableArrayList();
            // Comptage du nombre de médicaments pour chaque état
            Map<String, Integer> etatCount = new HashMap<>();
            for (Medicament medicament : medicaments) {
                String etat = medicament.getEtat();
                etatCount.put(etat, etatCount.getOrDefault(etat, 0) + 1);
            }

            // Création de tranches de données pour chaque état
            for (String etat : etatCount.keySet()) {
                int count = etatCount.get(etat);
                pieChartData.add(new PieChart.Data(etat, count));
            }
            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " = ", data.pieValueProperty()
                            )
                    )
            );
            PieStat.getData().addAll(pieChartData);

        } catch (Exception e) {
            // Gestion des exceptions
            e.printStackTrace();
        }
        //stat2
        try {
            // Simulation de données
            List<Categorie> categories = cs.getData();
            List<Medicament> medicaments = ms.getData();

            // Création d'une carte pour stocker le nombre de médicaments par catégorie
            Map<String, Integer> medicationDistributionStats = new HashMap<>();

            // Initialiser le nombre de médicaments par catégorie à zéro
            for (Categorie categorie : categories) {
                medicationDistributionStats.put(categorie.getNom_cat(), 0);
            }

            // Parcourir chaque médicament et incrémenter le nombre de médicaments dans sa catégorie
            for (Medicament medicament : medicaments) {
                String categorie = medicament.getCategorie().getNom_cat();
                int count = medicationDistributionStats.get(categorie);
                medicationDistributionStats.put(categorie, count + 1);
            }

            // Création de la liste observable pour les données du diagramme circulaire
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Ajout des données de la carte à la liste observable
            for (Map.Entry<String, Integer> entry : medicationDistributionStats.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " = ", data.pieValueProperty()
                            )
                    )
            );
            // Ajout des données au PieChart
            PieStatCat.setData(pieChartData);

        } catch (Exception e) {
            // Gestion des exceptions
            e.printStackTrace();
        }


        etat.setItems(observableArrayList("En Stock","Non disponible"));
        List<Categorie> categories1 = cs.getData();
        List<String> categoryNames = new ArrayList<>();

        // Ajouter les noms des catégories à la liste
        for (Categorie category : categories1) {
            categoryNames.add(category.getNom_cat());
        }

        // Ajouter les noms des catégories à la ComboBox
        ComboCategorie.setItems(observableArrayList(categoryNames));

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
    private Categorie getCategoryByName(String categoryName) {
        List<Categorie> categories = cs.getData(); // Supposons que vous ayez une méthode getCategories() dans votre service
        for (Categorie categorie : categories) {
            if (categorie.getNom_cat().equals(categoryName)) {
                return categorie;
            }
        }
        return null; // Retourne null si aucune catégorie correspondante n'est trouvée
    }
    @FXML
    void AjouterMed(ActionEvent event) {
        // Vérification si tous les champs sont remplis
        if (dateamm.getValue() == null || dateexp.getValue() == null || desc.getText().isEmpty() ||
                nom_med.getText().isEmpty() || ref_med.getText().isEmpty() || etat.getSelectionModel().isEmpty() ||
                ComboCategorie.getSelectionModel().isEmpty()||textArea.getText().isEmpty() || qte.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return; // Sortie de la méthode en cas de champs manquants
        }

        String catName = Optional.ofNullable(ComboCategorie.getSelectionModel().getSelectedItem()).orElse("");
        // Recherche de la catégorie correspondante dans la liste des catégories
        Categorie categorie = getCategoryByName(catName);
        LocalDate date = dateamm.getValue();
        LocalDate date1 = dateexp.getValue();
        String description = desc.getText();
        String nom1 = nom_med.getText();
        String ref1 = ref_med.getText();
        String etat1 = etat.getSelectionModel().getSelectedItem();
        String image1 = textArea.getText();
        // Vérification si la référence est unique
        String reference = ref_med.getText();
        if (ms.referenceExists(reference)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("La référence du médicament déjà existante.");
            alert.showAndWait();
            return; // Sortie de la méthode en cas de référence déjà existante
        }
        // Vérification si la date d'expiration est supérieure à la date d'ajout
        if (date1.isBefore(date)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("La date d'expiration doit être ultérieure à la date d'ajout.");
            alert.showAndWait();
            return; // Sortie de la méthode en cas d'erreur
        }

        // Vérification si la quantité est un entier positif
        int qte1;
        try {
            qte1 = Integer.parseInt(qte.getText());
            if (qte1 < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            // Gestion de l'erreur de conversion ou de quantité négative
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("La quantité doit être un entier positif valide.");
            alert.showAndWait();
            return; // Sortie de la méthode en cas d'erreur
        }
        // Calcul de la différence entre la date d'expiration et la date de mise en marché
        long joursRestants = ChronoUnit.DAYS.between(date, date1);
        // Appel de la méthode addMedicament avec les données récupérées
        if (ms != null) {
           Medicament medicament = new Medicament(1, categorie,ref1, nom1, date, date1, qte1, description, etat1, image1);
            ms.addMedicament(medicament);

            // Affichage de l'alerte avec le nombre de jours restants
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Médicament ajouté avec succès ! " + joursRestants + " jours restants avant l'expiration.");
            alert.showAndWait();
        } else {
            System.err.println("Échec de l'ajout du médicament!");
        }
        // Effacement des champs de saisie
        ComboCategorie.getSelectionModel().clearSelection();
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
        refreshTable();
    }

    @FXML
    void modifierMed(ActionEvent event) {
        int selectedIndex = table_med.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            // Récupérer le médicament sélectionné
            Medicament medSelected = ms.getData().get(selectedIndex);

            String catName = Optional.ofNullable(ComboCategorie.getSelectionModel().getSelectedItem()).orElse("");
            // Recherche de la catégorie correspondante dans la liste des catégories
            Categorie categorie = getCategoryByName(catName);
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
                        ComboCategorie.getSelectionModel().isEmpty()||description.isEmpty() || etatMed.isEmpty() || image.isEmpty()) {
                    throw new Exception("Veuillez remplir tous les champs !");
                }

                // Mettre à jour les valeurs du médicament sélectionné
                medSelected.setCategorie(categorie);
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
        ComboCategorie.getSelectionModel().clearSelection();
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
        ObservableList<String> items = FXCollections.observableArrayList();
        System.out.println("loading");

        // Récupérer les données de la base de données
        List<Medicament> medicamentList = ms.getData();

        // Parcourir la liste des médicaments et formater chaque élément
        for (Medicament med : medicamentList) {
            // Récupérer le nom de la catégorie associée au médicament
            String categoryName = med.getCategorie() != null ? med.getCategorie().getNom_cat() : "null";

            // Formater la chaîne de caractères avec le nom de la catégorie
            String item = String.format("ID: %d, Catégorie: %s, Référence: %s, Nom du médicament: %s," +
                            " Description: %s, Date d'expiration: %s, Date d'AMM: %s," +
                            " État: %s, Quantité: %s, Image: %s",
                    med.getId(), categoryName, med.getRef_med(), med.getNom_med(),
                    med.getDescription(), med.getDate_expiration(),
                    med.getDate_amm(), med.getEtat(), med.getQte(), med.getImage());

            // Ajouter l'élément à la liste observable
            items.add(item);
        }

        // Définir les éléments dans la ListView
        table_med.setItems(items);
    }


    private void handle(MouseEvent event) {
        // Vérifier si un clic de souris a été effectué avec le bouton gauche
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
            // Récupérer la ligne sélectionnée
            String medLineSelected = table_med.getSelectionModel().getSelectedItem();
            int selectedIndex = table_med.getSelectionModel().getSelectedIndex();
            // Vérifier si la ligne sélectionnée n'est pas nulle
            if (medLineSelected != null && selectedIndex != -1) {
                // Extraire les valeurs de la ligne sélectionnée
                Medicament selectedMedicament = ms.getData().get(selectedIndex);

                String[] parts = medLineSelected.split(","); // Diviser la chaîne en parties en utilisant ","
                String cat = parts[1].substring(parts[0].indexOf(":") + 10).trim(); // Récupérer la catégorie
                String nom = selectedMedicament.getNom_med();
                String ref = selectedMedicament.getRef_med();
                String description = selectedMedicament.getDescription();
                LocalDate dateAmm = selectedMedicament.getDate_amm();
                LocalDate dateExp = selectedMedicament.getDate_expiration();
                String etata = selectedMedicament.getEtat();
                int qte1 = selectedMedicament.getQte();
                String img = selectedMedicament.getImage();


                // Afficher les valeurs dans les zones de texte
                nom_med.setText(nom);
                ref_med.setText(ref);
                desc.setText(description);
                dateamm.setValue(dateAmm);
                dateexp.setValue(dateExp);
                etat.setValue(etata);
                qte.setText(String.valueOf(qte1));
                textArea.setText(img);

                // Sélectionner la catégorie correspondante dans la liste déroulante
                ComboCategorie.getSelectionModel().select(cat);
            }
        }
    }


}
