package Controller;

import Model.Admin;
import Model.Blog;
import Model.Commentaire;
import Model.CtegorieBlog;
import Service.BlogService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;

import Service.CategoriBlogService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static java.awt.Color.white;

public class CategorieBlogBack {
    private Image image;
    @FXML
    private TableView<Blog> tabblog;
    @FXML
    private TableColumn<Blog, String> thcategorieblog;
    @FXML
    private TableColumn<Blog, String> thdatepublication;
    @FXML
    private TableColumn<Blog, String> thlieu;

    @FXML
    private TableColumn<Blog, String> thrate;
    @FXML
    private TableColumn<Blog, String> thtitreblog;

    @FXML
    private TableColumn<Blog, String> thtitredescriptionblog;
    @FXML
    private Pagination paginationblog;

    @FXML
    private TableView<CtegorieBlog> tabcategorie;

    @FXML
    private TableColumn<CtegorieBlog, String> thdescription;

    @FXML
    private TableColumn<CtegorieBlog, String> thtitre;

    @FXML
    private TextArea inputdescription;

    @FXML
    private TextField inputtitre;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField inputsearch;
    @FXML
    private AnchorPane panCategorie;
    @FXML
    private Text sidecateg1;
    @FXML
    private Text sideblog;
    @FXML
    private AnchorPane panblog;
    @FXML
    private AnchorPane panformcateg;
    @FXML
    private ImageView btnmodifierblog;

    @FXML

    private ChoiceBox<String> choicecategorie;

    @FXML
    private ImageView imageviewblog;
    @FXML
    private TextField inputdescriptionblog;

    @FXML
    private TextField inputlieublog;
    @FXML
    private TextField inputtitreblog;
    @FXML
    private TextField inputrate;
    @FXML
    private ImageView btnafficheform;


    @FXML
    private AnchorPane panformulaireblog;

    @FXML
    private AnchorPane panimageblog;

    @FXML
    private ImageView btnvisibleblog;
    @FXML
    private AnchorPane panstatblog;
    @FXML
    private Text sidestat;

    @FXML
    private ImageView btndetaillecomment;
    @FXML
    private Text textnomacteur;
    @FXML
    private Text textcontenueacteur;

    @FXML
    private ListView listviewcommentback;

    private CategoriBlogService ctbs = new CategoriBlogService();

    private int itemsPerPage = 10;
    private ObservableList<CtegorieBlog> allCategories = FXCollections.observableArrayList();
    private ObservableList<CtegorieBlog> displayedCategories = FXCollections.observableArrayList();
    /********************************Navigation****************************************************************************************/
    private void switchToCategory() {
        panCategorie.setVisible(true);
        panblog.setVisible(false);
        panstatblog.setVisible(false);}

    private void switchToMain() {
        panCategorie.setVisible(false);
        panblog.setVisible(true);
        panstatblog.setVisible(false);

    }
    private void switchToStatBlog(){
        panCategorie.setVisible(false);
        panblog.setVisible(false);
        panstatblog.setVisible(true);

    }
    @FXML
    private AnchorPane sidebar;
    @FXML
    private Text dash;
    @FXML
    private VBox sidebardash;
    @FXML
    private HBox navbarback;

    @FXML
    private ImageView btndarkmode;
    @FXML
    private AnchorPane pancommentback;
    @FXML

    public void DarkMode() {
        try {
            ObservableList<String> stylesheets = sidebardash.getStylesheets();
            ObservableList<String> navbarStylesheets = navbarback.getStylesheets();
            ObservableList<String> panelstatStylesheets = panstatblog.getStylesheets(); // Assurez-vous que panstatblog est un AnchorPane

            // Assurez-vous que dash et sidecateg1 sont des composants de type Text
            Text dashText = (Text) dash;
            Text sidecateg1Text = (Text) sidecateg1;
            Text sideblogText= (Text) sideblog;
            Text sidestatText=(Text) sidestat;

            // Vérifiez si le mode sombre est déjà activé
            boolean darkModeActive = stylesheets.contains("/CssBlog/dark-mode.css");
            boolean darkModeActiveNavbar = navbarStylesheets.contains("/CssBlog/dark-mode.css");
            boolean darkModeActivePanstatblog = panelstatStylesheets.contains("/CssBlog/dark-mode.css");
            boolean darkModeActiveDashboard = dashText.getFill().equals(Color.WHITE);
            boolean darkModeActiveCategorie = sidecateg1Text.getFill().equals(Color.WHITE);
            boolean darkModeActiveBlog=sideblogText.getFill().equals(Color.WHITE);
            boolean darkModeActiveStat=sidestatText.getFill().equals(Color.WHITE);
            // Si le mode sombre est activé, supprimez la feuille de style
            if (darkModeActive && darkModeActiveNavbar && darkModeActiveDashboard && darkModeActivePanstatblog) {
                stylesheets.remove("/CssBlog/dark-mode.css");
                navbarStylesheets.remove("/CssBlog/dark-mode.css");
                panelstatStylesheets.remove("/CssBlog/dark-mode.css");

                // Changez la couleur du texte de dash et sidecateg1 pour désactiver le mode sombre
                dashText.setFill(Color.BLACK);
                sidecateg1Text.setFill(Color.BLACK);
                sideblogText.setFill(Color.BLACK);
                sidestatText.setFill(Color.BLACK);
            } else {
                // Sinon, ajoutez la feuille de style pour activer le mode sombre
                stylesheets.add("/CssBlog/dark-mode.css");
                navbarStylesheets.add("/CssBlog/dark-mode.css");
                panelstatStylesheets.add("/CssBlog/dark-mode.css");

                // Changez la couleur du texte de dash et sidecateg1 pour activer le mode sombre
                dashText.setFill(Color.WHITE);
                sidecateg1Text.setFill(Color.WHITE);
                sideblogText.setFill(Color.WHITE);
                sidestatText.setFill(Color.WHITE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**************************Principe ngOnInit*******************************************************************************************/
    private static final String API_KEY = "AIzaSyCQN8Tz7g6nX3BAmhBdFaVXq_Prb_04fLg";

    @FXML
    public void initialize() {
btnmodifierblog.setOnMouseClicked(event->modifierBlogs());
        inputlieublog.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                // Récupérer les suggestions de lieu en fonction de ce que l'utilisateur saisit
                List<String> suggestions = getPlaceSuggestions(newValue);
                // Afficher les suggestions à l'utilisateur
                if (!suggestions.isEmpty()) {
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(null, suggestions);
                    dialog.setTitle("Choisir un lieu");
                    dialog.setHeaderText("Choisissez un lieu parmi les suggestions :");
                    dialog.setContentText("Lieu :");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        inputlieublog.setText(result.get());
                    }
                }
            }
        });

/************************** Blogs*******************************************************************************/
        thtitreblog.setCellValueFactory(new PropertyValueFactory<>("titre"));
        thtitredescriptionblog.setCellValueFactory(new PropertyValueFactory<>("description"));
        thcategorieblog.setCellValueFactory(new PropertyValueFactory<>("ctgb"));
        thdatepublication.setCellValueFactory(new PropertyValueFactory<>("date"));
        thlieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        thrate.setCellValueFactory(new PropertyValueFactory<>("rate"));

        // Bind pagination control
        paginationblog.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> createPageBlog(newIndex.intValue()));

        // Load blogs from the service
        loadBlogs();

        tabblog.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedBlogsToInputFields();
        });
/***************************************CategorieBlogs*******************************************************************/
        // Initialize table columns
        thtitre.setCellValueFactory(new PropertyValueFactory<>("titrecategorie"));
        thdescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        // Bind selected item to input fields
        tabcategorie.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedCategorieToInputFields();
        });

        inputsearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchCategories(newValue);
        });

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            createPage(newIndex.intValue());
        });

        addcategorieTable();
/*********************************************************************/
        sidecateg1.setOnMouseClicked(event -> switchToCategory());
        sideblog.setOnMouseClicked(event -> switchToMain());
        sidestat.setOnMouseClicked(event->switchToStatBlog());

/*********NgOninitVisibiliteformulaire*****************************************/
        btnafficheform.setOnMouseClicked(event -> AfficherFormPanel());
        btnvisibleblog.setOnMouseClicked(event->AfficherFormBlog());
        btndarkmode.setOnMouseClicked(event->DarkMode());

        btndetaillecomment.setOnMouseClicked(event->AfficherCommentBack());

        remplirChoiceCategorie();
/********************Statestique ************************************************/
        AfficherNombreBlog();
        AfficherNombreCategorie();
        loadBlogs();





    }

    // Méthode pour récupérer les suggestions de lieu statiques
    // Afficher les suggestions en fonction de ce que l'utilisateur saisit
    private List<String> getPlaceSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        // Liste statique de lieux prédéfinis
        List<String> predefinedPlaces = new ArrayList<>();
        predefinedPlaces.add("Tunis");
        predefinedPlaces.add("Ben Arous");
        predefinedPlaces.add("Benzarte");
        predefinedPlaces.add("Manouba ");
        predefinedPlaces.add("Centre ville");
        predefinedPlaces.add("Beja");
        predefinedPlaces.add("Touzeur");



        // Filtrer les lieux prédéfinis en fonction de ce que l'utilisateur saisit
        for (String place : predefinedPlaces) {
            if (place.toLowerCase().contains(input.toLowerCase())) {
                suggestions.add(place);
            }
        }
        return suggestions;
    }
   /* private List<String> getPlaceSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        try {
            String encodedInput = URLEncoder.encode(input, "UTF-8");
            URL url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                    + encodedInput + "&key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray predictions = jsonResponse.getJSONArray("predictions");
            for (int i = 0; i < predictions.length(); i++) {
                String description = predictions.getJSONObject(i).getString("description");
                suggestions.add(description);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la récupération des suggestions de lieu.");
            alert.showAndWait();
        }
        return suggestions;
    }
*/

    /****************************Visibilite Formulaire**********************************************************************************************/

    public void AfficherFormPanel() {
        if (panformcateg != null) {
            panformcateg.setVisible(!panformcateg.isVisible());
        } else {
            System.err.println("panformcateg n'est pas correctement référencé dans votre fichier FXML.");
        }
    }

    public void AfficherFormBlog(){
        if (panformulaireblog!=null && panimageblog !=null){
            panformulaireblog.setVisible(!panformulaireblog.isVisible());
            panimageblog.setVisible(!panimageblog.isVisible());
        }
    }
    public void AfficherCommentBack(){
        if (pancommentback ==null){
            pancommentback.setVisible(pancommentback.isVisible());

        }else {
            pancommentback.setVisible(!pancommentback.isVisible());

        }
    }
    /************************************************   AjouterCategorie autable********************************************************************/
    @FXML
    public void addcategorieTable() {
        // Load all categories if not already loaded
        if (allCategories.isEmpty()) {
            loadCategories();
        }

        // Calculate total number of pages
        int pageCount = (int) Math.ceil((double) allCategories.size() / itemsPerPage);

        // Set up pagination
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0); // Set to first page
    }

    private void loadCategories() {
        List<CtegorieBlog> all = ctbs.getAll();
        allCategories.addAll(all);
    }

    private void createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allCategories.size());
        displayedCategories.setAll(allCategories.subList(fromIndex, toIndex));
        tabcategorie.setItems(displayedCategories);
    }


    /**********************Ajouter Depuis Formulaire ********************************************************************************************/

    @FXML
    public void ajouterCategorieFormulaire() {
        String titre = inputtitre.getText();
        String description = inputdescription.getText();
        CategoriBlogService cbs = new CategoriBlogService();
        List<CtegorieBlog> lsb = cbs.getAll();
        for (CtegorieBlog c : lsb) {
            if (titre.equals(c.getTitrecategorie())) {
                Notifications.create()
                        .title("Avertissement")
                        .text("Cette Catégorie existe déjà !")
                        .darkStyle()
                        .position(Pos.TOP_CENTER)
                        .show();

                return;
            }
        }

        if (titre.isEmpty() || description.isEmpty()) {
            Notifications.create()
                    .title("Alerte")
                    .text("Le titre ou la description est vide !")
                    .darkStyle()
                    .position(Pos.TOP_CENTER)
                    .show();
            return;
        }

        CtegorieBlog newCategorie = new CtegorieBlog(1, titre, description);
        ctbs.add(newCategorie);
        loadCategories();
        pagination.setPageCount((int) Math.ceil((double) allCategories.size() / itemsPerPage));
        pagination.setCurrentPageIndex(pagination.getPageCount() - 1);

        Notifications.create()
                .title("Succès")
                .text("La catégorie a été ajoutée avec succès.")
                .darkStyle()
                .position(Pos.TOP_CENTER)
                .show();
    }


    /*****************************Supprimer Element *******************************************************************************/

    @FXML
    public void supprimerCategorieSelectionnee() {
        CtegorieBlog selectedCategorie = tabcategorie.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer la catégorie de blog");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette catégorie de blog?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = ctbs.delete(selectedCategorie);
                    if (deleted) {
                        // Afficher une notification de succès personnalisée
                        Notifications.create()
                                .title("Success")
                                .text("La catégorie de blog a été supprimée avec succès.")
                                .darkStyle()
                                .position(Pos.TOP_CENTER)
                                .show();
                        loadCategories();
                        pagination.setPageCount((int) Math.ceil((double) allCategories.size() / itemsPerPage));
                        pagination.setCurrentPageIndex(0);
                    } else {
                        Notifications.create()
                                .title("Error")
                                .text("Erreur lors de la suppression de la catégorie de blog.")
                                .darkStyle()
                                .position(Pos.TOP_CENTER)

                                .show();
                    }
                }
            });
        } else {
            // Afficher une notification d'avertissement personnalisée
            Notifications.create()
                    .title("Avertissement")
                    .text("Veuillez sélectionner une catégorie de blog à supprimer.")
                    .darkStyle()
                    .show();
        }
    }
    /**************************************Clear les champs ********************************************************************************/


    public void ClearInput() {
        inputtitre.clear();
        inputdescription.clear();
    }

    /********************************Selected Item**********************************************************************************************/
    @FXML
    public void setSelectedCategorieToInputFields() {
        CtegorieBlog selectedCategorie = tabcategorie.getSelectionModel().getSelectedItem();

        if (selectedCategorie != null) {
            inputtitre.setText(selectedCategorie.getTitrecategorie());
            inputdescription.setText(selectedCategorie.getDescription());
        } else {
            ClearInput();
        }
    }


    /*****************************************ModificationInformationFormulaire***************************************************************************/
    @FXML
    public void modifier() {
        CtegorieBlog selectedCategorie = tabcategorie.getSelectionModel().getSelectedItem();

        if (selectedCategorie != null) {
            String newTitre = inputtitre.getText();
            String newDescription = inputdescription.getText();

            if (newTitre.isEmpty()) {

                Notifications.create()

                        .title("Modification échouée")
                        .text("Le titre ne peut pas être vide. \uD83D\uDE1E")
                        .position(Pos.TOP_CENTER)
                        .showWarning();
                return;
            } else if (newDescription.isEmpty()) {

                Notifications.create()
                        .title("Modification échouée")
                        .text("La description ne peut pas être vide. \uD83D\uDE1E")
                        .position(Pos.TOP_CENTER)
                        .showWarning();
                return;
            }

            selectedCategorie.setTitrecategorie(newTitre);
            selectedCategorie.setDescription(newDescription);

            tabcategorie.refresh();

            Notifications.create()
                    .title("Succès")
                    .text("Catégorie mise à jour avec succès!" +"\uD83D\uDE0A")
                    .position(Pos.TOP_CENTER)
                    .showInformation(); // Utilisation de showInformation() pour une notification d'information

            ClearInput();
        } else {
            Notifications.create()
                    .title("Avertissement")
                    .text("Aucune catégorie sélectionnée. Veuillez sélectionner une catégorie de blog à modifier.")
                    .position(Pos.TOP_CENTER)
                    .showWarning();
        }
    }

    /********************************RechercherGlobale*****************************************************************************/
    @FXML
    public void searchCategories(String searchText) {
        if (searchText.isEmpty()) {

            addcategorieTable();
        } else {

            CtegorieBlog searchCriteria = new CtegorieBlog();
            searchCriteria.setTitrecategorie(searchText);
            searchCriteria.setDescription(searchText);

            // Send AJAX request to the backend and update the TableView with search results
            ArrayList<CtegorieBlog> searchResults = ctbs.getBytitreDescription(searchCriteria);
            ObservableList<CtegorieBlog> searchResultsList = FXCollections.observableArrayList(searchResults);
            tabcategorie.setItems(searchResultsList);
        }

    }
    /*********************************************Actualiser Tableau**************************************************************/
    public void ActualiserTableau(){
        addcategorieTable();
        tabcategorie.refresh();
    }

/****************************************************Gestion*******************************************************************************/
/************************************************************Blog*******************************************************************/
/*****************************************************************Table BLOG****************************************************************/
/***************************************************************************************************************************************/
/***************************************************************************************************************************************/


    /*****************************************************************RemplirChoicede blog par Nom de categorie**************************************************************************/

    public void remplirChoiceCategorie() {
        ArrayList<CtegorieBlog> categories = ctbs.getAll();
        ObservableList<String> categoryNames = FXCollections.observableArrayList();

        for (CtegorieBlog category : categories) {
            categoryNames.add(category.getTitrecategorie());
        }

        choicecategorie.setItems(categoryNames);
    }
    /*****************************************************Image upload ********************************************************************************************************************/

    public void InsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(panblog.getScene().getWindow());

        if (file != null) {
            String sourceFilePath = file.getAbsolutePath();

            String destinationDirectory = "C:\\Users\\laame\\Desktop\\HopitalMeliataire\\PIDev\\public\\uploads\\images\\products\\";

            try {
                Path sourcePath = Path.of(sourceFilePath);
                Path destinationPath = Path.of(destinationDirectory, file.getName());
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                getData.path = destinationPath.toString();

                Image image = new Image(destinationPath.toUri().toString(), 142, 136, false, true);
                imageviewblog.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**********************************************************************GetAllBlog*****************************************************************************************************************/
    private ObservableList<Blog> allBlogs= FXCollections.observableArrayList();
    private ObservableList<Blog> displayedBlogs = FXCollections.observableArrayList();
    BlogService bs=new BlogService();
    @FXML
    public void addblogTable() {
        // Load all blogs if not already loaded
        if (allBlogs.isEmpty()) {
            loadBlogs();
        }

        int pageCount = (int) Math.ceil((double) allBlogs.size() / itemsPerPage);

        // Set up pagination
        paginationblog.setPageCount(pageCount);
        paginationblog.setCurrentPageIndex(1);
    }

    private void createPageBlog(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allBlogs.size());
        if (fromIndex <= toIndex) {
            displayedBlogs.setAll(allBlogs.subList(fromIndex, toIndex));
            tabblog.setItems(displayedBlogs);
        } else {
            tabblog.getItems().clear();
        }
    }

    private void loadBlogs() {
        allBlogs.clear();
        allBlogs.addAll(bs.getAll());
    }
    /******************************Selctionner Blogs ******************************************************************************************************/
    public void recupererEtCopierImage(String sourceDirectory, String destinationDirectory, String fileName) {
        // Vérifier si le nom de fichier est valide
        if (fileName != null && !fileName.isEmpty()) {
            // Chemin complet du fichier source
            String sourcePath = sourceDirectory + fileName;

            // Créer un objet File pour le fichier source
            File sourceFile = new File(sourcePath);

            // Vérifier si le fichier source existe
            if (sourceFile.exists()) {
                // Chemin complet du fichier de destination
                String destinationPath = destinationDirectory + fileName;

                try {
                    Path source = sourceFile.toPath();
                    Path destination = new File(destinationPath).toPath();
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("Fichier copié avec succès : " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Erreur lors de la copie du fichier : " + fileName);
                }
            } else {
                System.err.println("Le fichier source n'existe pas : " + fileName);
            }
        } else {
            System.err.println("Nom de fichier invalide");
        }
    }
    @FXML
    public void setSelectedBlogsToInputFields() {
        Blog selectedBlog = tabblog.getSelectionModel().getSelectedItem();

        if (selectedBlog != null) {
            inputtitreblog.setText(selectedBlog.getTitre());
            inputdescriptionblog.setText(selectedBlog.getDescription());
            inputlieublog.setText(selectedBlog.getLieu());
            choicecategorie.setValue(selectedBlog.getCtgb().getTitrecategorie());
            inputrate.setText(String.valueOf(selectedBlog.getRate()));

            String imagePath = "C:\\Users\\laame\\Desktop\\HopitalMeliataire\\PIDev\\public\\uploads\\images\\products\\"; // Chemin d'accès au répertoire des images
            String fileName = selectedBlog.getIamge(); // Nom de fichier de l'image

            if (fileName != null && !fileName.isEmpty()) {
                String fullPath = imagePath + fileName;
                Image image = new Image(fileName, 180, 160, false, true);
                imageviewblog.setImage(image);
            } else {
                imageviewblog.setImage(null);
            }

            List<Commentaire> comments = bs.getCommentsByBlogId(selectedBlog.getId());

            ObservableList<String> formattedComments = FXCollections.observableArrayList();

            for (Commentaire comment : comments) {
                String formattedComment = "Author: " + comment.getAdmin().getNom() + " " + comment.getAdmin().getPrenom() + "\n" +
                        "Comment: " + comment.getContenue();
                formattedComments.add(formattedComment);
            }

            // Personnaliser les cellules de la ListView pour afficher les commentaires sous forme de cartes
            listviewcommentback.setCellFactory(param -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Créer une carte pour le commentaire
                        VBox card = new VBox();
                        card.getStyleClass().add("list-view-card");
                        card.setPadding(new Insets(10));
                        card.setSpacing(5);

                        // Créer un label pour afficher le commentaire formaté
                        Label commentLabel = new Label(item);

                        // Ajouter le label à la carte
                        card.getChildren().addAll(commentLabel);

                        // Afficher la carte dans la cellule de la ListView
                        setGraphic(card);
                    }
                }
            });

            // Appliquer le style CSS pour la ListView
            listviewcommentback.getStyleClass().add("list-view-comment-back");

            // Afficher la liste des commentaires formatés dans le composant ListView
            listviewcommentback.setItems(formattedComments);
        }
    }



    /******************************************AjouterBlog*******************************************************************************************************/
    @FXML
    public void ajouterBlogFormulaire() {
        String titre = inputtitreblog.getText();
        String description = inputdescriptionblog.getText();
        String lieu = inputlieublog.getText();
        String rateText = inputrate.getText();
        int rate = 0;
        String categorie = choicecategorie.getValue();

        // Vérifier si une image est sélectionnée
        String image = "";
        if (imageviewblog.getImage() != null) {
            image = imageviewblog.getImage().getUrl();
        }

        // Vérifier si le titre, la description ou la catégorie est vide
        if (titre.isEmpty() || description.isEmpty() || categorie == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerte");
            alert.setHeaderText(null);
            alert.setContentText("Le titre, la description ou la catégorie est vide !");
            alert.showAndWait();
            return;
        }

        // Conversion du taux de string à int
        try {
            rate = Integer.parseInt(rateText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le taux doit être un nombre entier !");
            alert.showAndWait();
            return;
        }

        // Créer une nouvelle instance de Blog avec les données fournies
        LocalDate date = LocalDate.now();
        Blog newBlog = new Blog(1, titre, description, image, lieu, rate, date, ctbs.getCatBlogByNom(choicecategorie.getValue()));

        // Ajouter le nouveau blog
        bs.add(newBlog);

        // Recharger les blogs
        loadBlogs();

        // Mettre à jour la pagination
        paginationblog.setPageCount((int) Math.ceil((double) allCategories.size() / itemsPerPage));
        paginationblog.setCurrentPageIndex(paginationblog.getPageCount() - 1);
        paginationblog.setCurrentPageIndex(paginationblog.getPageCount() + 1);

    }
    /****************************************************ClearInputBlog******************************************************************************************************/

    public void clearInputBlog(){

        inputtitreblog.clear();
        inputrate.clear();
        inputlieublog.clear();
        inputdescriptionblog.clear();
        choicecategorie.setValue("");
        imageviewblog.setImage(null);

    }
    /*************************************************Refresh Table Blog ***********************************************************************************************/

    public void  refrechBlog(){

        tabblog.refresh();
        loadBlogs();

    }
    /*************************************************DeleteBlog***********************************************************************************************/

    @FXML
    public void deleteBlog() {
        Blog selectedBlog = tabblog.getSelectionModel().getSelectedItem();
        if (selectedBlog != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer le blog");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce blog?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = bs.delete(selectedBlog);
                    if (deleted) {
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Succès");
                        success.setHeaderText(null);
                        success.setContentText("Le blog a été supprimé avec succès.");
                        success.showAndWait();
                        refrechBlog();
                        // Mise à jour de l'affichage
                        loadBlogs();
                        clearInputBlog();
                        paginationblog.setPageCount((int) Math.ceil((double) allBlogs.size() / itemsPerPage));
                        paginationblog.setCurrentPageIndex(0);
                        paginationblog.setCurrentPageIndex(1);

                    } else {
                        Alert failure = new Alert(Alert.AlertType.ERROR);
                        failure.setTitle("Erreur");
                        failure.setHeaderText(null);
                        failure.setContentText("Erreur lors de la suppression du blog.");
                        failure.showAndWait();
                    }
                } else {
                    Alert failure = new Alert(Alert.AlertType.ERROR);
                    failure.setTitle("Erreur");
                    failure.setHeaderText(null);
                    failure.setContentText("Annulation de la suppression du blog.");
                    failure.showAndWait();
                }
            });
        } else {
            Alert noSelection = new Alert(Alert.AlertType.WARNING);
            noSelection.setTitle("Avertissement");
            noSelection.setHeaderText("Aucun blog sélectionné");
            noSelection.setContentText("Veuillez sélectionner un blog à supprimer.");
            noSelection.showAndWait();
        }
    }

    @FXML
    public void modifierBlogs() {
        Blog selectedBlog = tabblog.getSelectionModel().getSelectedItem();

        if (selectedBlog != null) {
            String newTitre = inputtitreblog.getText();
            String newDescription = inputdescriptionblog.getText();
            String newLieu = inputlieublog.getText();
            String newRate = inputrate.getText();
            String newChoicetype = choicecategorie.getValue();
            Image newFile = imageviewblog.getImage();

            if (newTitre.isEmpty() || newDescription.isEmpty()) {
                Notifications.create()
                        .title("Modification échouée")
                        .text("Le titre et la description ne peuvent pas être vides. \uD83D\uDE1E")
                        .position(Pos.TOP_CENTER)
                        .showWarning();
                return;
            }

            selectedBlog.setTitre(newTitre);
            selectedBlog.setDescription(newDescription);
            // Assurez-vous de mettre à jour les autres attributs du blog également

            tabblog.refresh();

            Notifications.create()
                    .title("Succès")
                    .text("Blog mis à jour avec succès! \uD83D\uDE0A")
                    .position(Pos.TOP_CENTER)
                    .showInformation(); // Utilisation de showInformation() pour une notification d'information

            ClearInput();
        } else {
            Notifications.create()
                    .title("Avertissement")
                    .text("Aucun blog sélectionné. Veuillez sélectionner un blog à modifier.")
                    .position(Pos.TOP_CENTER)
                    .showWarning();
        }
    }




/****************************************************Gestion*******************************************************************************/
/************************************************************Statestique*******************************************************************/
/*****************************************************************Backoffice****************************************************************/
/***************************************************************************************************************************************/
    /***************************************************************************************************************************************/
    @FXML
    private Text nombrecateg;
    public void AfficherNombreCategorie(){
        int nn= ctbs.NombreCategorieBlog();
        nombrecateg.setText(String.valueOf(nn));
    }

    @FXML
    private Text statnombreblog;
    public void AfficherNombreBlog(){
        int nn=bs.NombreBlog();
        statnombreblog.setText(String.valueOf(nn));
    }

}





