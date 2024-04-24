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
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
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

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            // Obtenez les feuilles de style actuelles
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

    @FXML
    public void initialize() {
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

    /****************************Visibilite Formulaire**********************************************************************************************/

// Méthode pour afficher ou masquer le formulaire en fonction de sa visibilité actuelle
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

        if (titre.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("Titre ou description vide !");
            alert.showAndWait();
            return;
        }

        CtegorieBlog newCategorie = new CtegorieBlog(1, titre, description);
        ctbs.add(newCategorie);
        loadCategories();
        pagination.setPageCount((int) Math.ceil((double) allCategories.size() / itemsPerPage));
        pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
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
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success");
                        success.setHeaderText(null);
                        success.setContentText("La catégorie de blog a été supprimée avec succès.");
                        success.showAndWait();
                        loadCategories();
                        pagination.setPageCount((int) Math.ceil((double) allCategories.size() / itemsPerPage));
                        pagination.setCurrentPageIndex(0);
                    } else {
                        Alert failure = new Alert(Alert.AlertType.ERROR);
                        failure.setTitle("Error");
                        failure.setHeaderText(null);
                        failure.setContentText("Erreur lors de la suppression de la catégorie de blog.");
                        failure.showAndWait();
                    }
                }
            });
        } else {
            Alert noSelection = new Alert(Alert.AlertType.WARNING);
            noSelection.setTitle("Avertissement");
            noSelection.setHeaderText("Aucune catégorie sélectionnée");
            noSelection.setContentText("Veuillez sélectionner une catégorie de blog à supprimer.");
            noSelection.showAndWait();
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
        // Get the selected item from the TableView
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("Titre vide !");
                alert.showAndWait();
                return;
            } else if (newDescription.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("Description vide !");
                alert.showAndWait();
                return;
            }


            selectedCategorie.setTitrecategorie(newTitre);
            selectedCategorie.setDescription(newDescription);


            tabcategorie.refresh();


            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Category updated successfully!");
            successAlert.showAndWait();


            ClearInput();
        } else {

            Alert noSelection = new Alert(Alert.AlertType.WARNING);
            noSelection.setTitle("Avertissement");
            noSelection.setHeaderText("Aucune catégorie sélectionnée");
            noSelection.setContentText("Veuillez sélectionner une catégorie de blog à modifier.");
            noSelection.showAndWait();
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
            // Get the absolute path of the selected file
            String sourceFilePath = file.getAbsolutePath();

            // Set the destination directory where you want to copy the image
            String destinationDirectory = "C:\\Users\\laame\\Desktop\\HopitalMeliataire\\PIDev\\public\\uploads\\images\\products\\";

            try {
                // Copy the selected image file to the destination directory
                Path sourcePath = Path.of(sourceFilePath);
                Path destinationPath = Path.of(destinationDirectory, file.getName());
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // Set the path to the copied image file
                getData.path = destinationPath.toString();

                // Load the copied image and set it to the ImageView
                Image image = new Image(destinationPath.toUri().toString(), 142, 136, false, true);
                imageviewblog.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
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

        // Calculate total number of pages
        int pageCount = (int) Math.ceil((double) allBlogs.size() / itemsPerPage);

        // Set up pagination
        paginationblog.setPageCount(pageCount);
        paginationblog.setCurrentPageIndex(1); // Set to first page
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
                    // Copier le fichier source vers le répertoire de destination
                    Path source = sourceFile.toPath();
                    Path destination = new File(destinationPath).toPath();
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

                    // Afficher un message indiquant que la copie a été effectuée avec succès
                    System.out.println("Fichier copié avec succès : " + fileName);
                } catch (IOException e) {
                    // Afficher une erreur si la copie du fichier échoue
                    e.printStackTrace();
                    System.err.println("Erreur lors de la copie du fichier : " + fileName);
                }
            } else {
                // Afficher un message d'erreur si le fichier source n'existe pas
                System.err.println("Le fichier source n'existe pas : " + fileName);
            }
        } else {
            // Afficher un message d'erreur si le nom de fichier est vide ou null
            System.err.println("Nom de fichier invalide");
        }
    }
    @FXML
    public void setSelectedBlogsToInputFields() {
        // Récupérer l'élément sélectionné dans la TableView
        Blog selectedBlog = tabblog.getSelectionModel().getSelectedItem();

        if (selectedBlog != null) {
            // Mettre les données de l'élément sélectionné dans les champs d'entrée
            inputtitreblog.setText(selectedBlog.getTitre());
            inputdescriptionblog.setText(selectedBlog.getDescription());
            inputlieublog.setText(selectedBlog.getLieu());
            choicecategorie.setValue(selectedBlog.getCtgb().getTitrecategorie());
            inputrate.setText(String.valueOf(selectedBlog.getRate()));

            // Charger l'image associée au blog s'il y en a une
            String imagePath = "C:\\Users\\laame\\Desktop\\HopitalMeliataire\\PIDev\\public\\uploads\\images\\products\\"; // Chemin d'accès au répertoire des images
            String fileName = selectedBlog.getIamge(); // Nom de fichier de l'image

            if (fileName != null && !fileName.isEmpty()) {
                String fullPath = imagePath + fileName;
                Image image = new Image(fileName, 180, 160, false, true);
                imageviewblog.setImage(image);
            } else {
                imageviewblog.setImage(null);
            }

            // Charger la liste des commentaires associés à ce blog
            List<Commentaire> comments = bs.getCommentsByBlogId(selectedBlog.getId());

            // Créer une liste pour stocker les commentaires formatés avec le contenu et le nom de l'auteur
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
        String categorie = choicecategorie.getValue(); // Vérifier si une catégorie est sélectionnée

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





