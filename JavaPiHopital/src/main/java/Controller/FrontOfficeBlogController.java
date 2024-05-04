package Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import Model.Admin;
import Model.Blog;
import Model.Commentaire;
import Service.BlogService;
import Service.CommentaireService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Callback;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.util.Duration;
import org.apache.http.client.fluent.Request;
import org.controlsfx.control.Notifications;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.event.EventHandler;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
public class FrontOfficeBlogController {
    @FXML
    private ListView<List<Blog>> listViewFrontBlog;
    @FXML
    private AnchorPane anchordetailleblog;
    @FXML
    private AnchorPane anchorlisteblog;
    @FXML
    private Text titreDetaille;
    @FXML
    private Text contenuDetaille;
    @FXML
    private ImageView btnretournerback;
    @FXML
    private Text textdate;
    @FXML
    private Text textlieu;

    @FXML
    private ListView listviewcommentfront;
    @FXML
    private ImageView imageviewdetaille;
    @FXML
    private TextField inputaddcommentaire;

    @FXML
    private Text texttitrecategoriefff;
    @FXML Text textcategoriedescription;
    @FXML ImageView btnajoutercommentfront;
    @FXML Text textrate1;
    @FXML
    private ImageView btnsharefacebbok;
    private final double zoomFactor = 1.5;
    @FXML
    private FlowPane imagePane;
    @FXML
    private TextField ttc;
    @FXML
    private ScrollPane scrollPane;

   @FXML
   private  ImageView searchButton;
   @FXML
   private AnchorPane pangoogle;
   @FXML
   private ImageView btnbackgoogle;

   @FXML
   private ImageView btngorecherche;
   @FXML
   private HBox root;
    private static final String API_KEY = "AIzaSyCdCuUdiDDEz7TiHTtfJik8XnXZg24UJEg";
    private static final String SEARCH_ENGINE_ID = "623dc2de16ef345cc";

    private BlogService blogService = new BlogService();
    private List<Blog> blogList = blogService.getAll();
    private static final List<String> NEWS = Arrays.asList(
            "Adoptez une alimentation équilibrée pour fournir à votre corps les nutriments essentiels.",
            "Faites de l'exercice régulièrement pour renforcer votre système cardiovasculaire et maintenir une santé musculaire.",
            "Apprenez des techniques de gestion du stress telles que la méditation et la respiration profonde.",
            "Assurez-vous de dormir suffisamment chaque nuit pour favoriser la récupération physique et mentale.",
            "Buvez suffisamment d'eau chaque jour pour maintenir une hydratation adéquate.",
            "Ne négligez pas les examens médicaux réguliers pour détecter les problèmes de santé à temps."
    );

    private static final double SPEED = 0.5; // Pixels par seconde

    private int currentIndex = 0;

    @FXML
    public void initialize() {

        root.setPrefWidth(800); // Largeur de la bande de news
        root.setPrefHeight(50); // Hauteur de la bande de news
        root.setStyle("-fx-background-color: red; -fx-border-color: #ccc; -fx-border-width: 1px;");

        Label allNewsLabel = new Label("All News:");
        allNewsLabel.setFont(Font.font("System", 12));
        allNewsLabel.setTextFill(Color.WHITE);
        root.getChildren().add(allNewsLabel);

        for (String news : NEWS) {
            Label newsLabel = new Label(news);
            newsLabel.setFont(Font.font("System", 12));
            newsLabel.setTextFill(Color.WHITE);
            root.getChildren().add(newsLabel);

            Label separatorLabel = new Label(" News ");
            separatorLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            separatorLabel.setTextFill(Color.WHITE);
            root.getChildren().add(separatorLabel);
        }

        // Animer le défilement des actualités
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            for (int i = 0; i < root.getChildren().size(); i++) {
                Label label = (Label) root.getChildren().get(i);
                double newX = label.getTranslateX() - SPEED;
                label.setTranslateX(newX);
                if (newX < -root.getWidth()) {
                    label.setTranslateX(root.getWidth()); // Réinitialiser la position
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();



        btngorecherche.setOnMouseClicked(event->switchrechercheGoogle());

        searchButton.setOnMouseClicked(event -> {
            try {
                String query = ttc.getText(); // Example query for demonstration
                displayImageSearchResults(query, imagePane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnbackgoogle.setOnMouseClicked(event->switchbackfromsearch());
        btnretournerback.setOnMouseClicked(event -> switchbloglist());
        btnajoutercommentfront.setOnMouseClicked(event ->addCommentaireFront());

        ObservableList<List<Blog>> observableList = FXCollections.observableArrayList(chunkList(blogList, 3));

        listViewFrontBlog.setItems(observableList);
        listViewFrontBlog.setCellFactory(new Callback<ListView<List<Blog>>, ListCell<List<Blog>>>() {
            @Override
            public ListCell<List<Blog>> call(ListView<List<Blog>> param) {
                return new ListCell<List<Blog>>() {
                    @Override
                    protected void updateItem(List<Blog> itemList, boolean empty) {
                        super.updateItem(itemList, empty);
                        if (empty || itemList == null || itemList.isEmpty()) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox();
                            hbox.setSpacing(80); // Espacement entre les AnchorPanes

                            for (Blog blog : itemList) {
                                ImageView imageView = new ImageView(blog.getIamge());
                                Text titleText = new Text(blog.getTitre());
                                titleText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Style du texte du titre
                                imageView.setFitWidth(300); // Ajustez la largeur de l'image
                                imageView.setFitHeight(200); // Ajustez la hauteur de l'image

                                VBox vbox = new VBox(); // Créez un VBox pour empiler les éléments verticalement
                                vbox.getChildren().addAll(imageView, titleText); // Ajoutez l'image et le titre au VBox
                                vbox.setAlignment(Pos.CENTER); // Centrez les éléments à l'intérieur du VBox

                                imageView.setFitWidth(300); // Ajustez la largeur de l'image
                                imageView.setFitHeight(200); // Ajustez la hauteur de l'image

                                AnchorPane anchorPane = new AnchorPane();
                                AnchorPane.setTopAnchor(titleText, 10.0); // Position du texte du titre par rapport au haut
                                AnchorPane.setLeftAnchor(titleText, 10.0); // Position du texte du titre par rapport à gauche
                                AnchorPane.setBottomAnchor(imageView, 0.0); // Position de l'image par rapport au bas
                                AnchorPane.setLeftAnchor(imageView, 0.0); // Position de l'image par rapport à gauche

                                anchorPane.getChildren().addAll(imageView, titleText);
                                anchorPane.setOnMouseClicked(event -> {
                                    // Récupérer l'ID du blog sélectionné
                                    int blogId = blog.getId();
                                    // Appeler la méthode pour afficher les détails du blog
                                    displayBlogDetail(blog);
                                    // Stocker l'ID du blog sélectionné dans une variable pour une utilisation ultérieure
                                    // Faites quelque chose avec l'ID, par exemple, vous pouvez le stocker dans une variable globale
                                    // Ou vous pouvez le passer directement à une méthode qui a besoin de l'ID
                                });

                                hbox.getChildren().add(anchorPane);
                            }

                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    // Méthode pour diviser une liste en sous-listes de taille donnée
    private <T> List<List<T>> chunkList(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(new ArrayList<>(list.subList(i, Math.min(i + chunkSize, list.size()))));
        }
        return chunks;
    }
    private void zoomImage(javafx.scene.input.MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();

        // Supprimer les transformations existantes pour réinitialiser le zoom
        imageView.getTransforms().clear();

        // Définir le point central du zoom au centre de l'image
        imageView.setTranslateX(imageView.getBoundsInLocal().getWidth() / 2);
        imageView.setTranslateY(imageView.getBoundsInLocal().getHeight() / 2);

        // Appliquer le zoom sur l'ImageView
        double zoomFactor = 1.5; // Facteur de zoom
        Scale scaleTransform = new Scale(zoomFactor, zoomFactor);
        imageView.getTransforms().add(scaleTransform);
    }

    public void switchbloglist() {
        if (anchordetailleblog != null) {
            anchordetailleblog.setVisible(!anchordetailleblog.isVisible());
            anchorlisteblog.setVisible(true);
        }
    }

    public  void switchbackfromsearch(){
        if (pangoogle!=null){
            pangoogle.setVisible(!pangoogle.isVisible());
            anchordetailleblog.setVisible(true);


        }
    }
    public void switchrechercheGoogle(){
        if (pangoogle!=null){
            pangoogle.setVisible(true);
            anchordetailleblog.setVisible(false);
            anchorlisteblog.setVisible(false);
        }
    }
    public void displayBlogDetail(Blog blog) {
        if (blog != null) {

            titreDetaille.setText(blog.getTitre());
            contenuDetaille.setText(blog.getDescription());
            textdate.setText(blog.getDate().toString());
            textlieu.setText(blog.getLieu());
            BlogService bss=new BlogService();
            textlieu.setOnMouseClicked(event->bss.shareMaps(blog.getLieu()));
            // Charger l'image associée au blog s'il y en a une
            String imagePath = "C:\\Users\\laame\\Desktop\\HopitalMeliataire\\PIDev\\public\\uploads\\images\\products\\"; // Chemin d'accès au répertoire des images
            String fileName = blog.getIamge(); // Nom de fichier de l'image

            if (fileName != null && !fileName.isEmpty()) {
                String fullPath = imagePath + fileName;
                Image image = new Image(fileName, 343, 350, false, true);
                imageviewdetaille.setImage(image);
            } else {
                imageviewdetaille.setImage(null);
            }
            BlogService bs = new BlogService();
            String titrecategories = bs.getTitleOfBlogById(blog.getId());
            String descriptioncategorie = bs.getDescriptionOfBlogById(blog.getId());
            texttitrecategoriefff.setText(titrecategories);
            textcategoriedescription.setText(descriptioncategorie);
            titreDetaille.setText(blog.getTitre());
            contenuDetaille.setText(blog.getDescription());
            String descriptionCategorie = bs.getDescriptionOfBlogById(blog.getId());

            // Insérer un retour à la ligne après chaque groupe de 9 mots
            StringBuilder stringBuilder = new StringBuilder();
            String[] mots = descriptionCategorie.split("\\s+");
            int compteur = 0;
            for (String mot : mots) {
                stringBuilder.append(mot).append(" ");
                compteur++;
                if (compteur % 5 == 0) {
                    stringBuilder.append("\n");
                }
            }
            String descriptionAvecRetourLigne = stringBuilder.toString();

            // Afficher la description de la catégorie dans le composant TextArea
            textcategoriedescription.setText(descriptionAvecRetourLigne);            // Charger la liste des commentaires associés à ce blog
            // Récupérer la liste des commentaires
            List<Commentaire> comments = bs.getCommentsByBlogId(blog.getId());

            // Créer une liste pour stocker les commentaires formatés avec le contenu et les logos de like/dislike
            ObservableList<HBox> formattedComments = FXCollections.observableArrayList();

            for (Commentaire comment : comments) {
                // Créer un label pour afficher le contenu du commentaire
                Label commentLabel = new Label(comment.getAdmin().getNom() + " " + comment.getAdmin().getPrenom() + "\n" +
                        comment.getContenue());
                // Créer deux ImageView pour les logos de like et dislike
                ImageView likeImageView = new ImageView("Template/front/images/icons8-pouce-en-l'air-16.png");
                ImageView dislikeImageView = new ImageView("Template/front/images/icons8-pouces-vers-le-bas-24.png");
                Text supprimercommentaire = new Text();
                Text modifiercommentaire = new Text();

                // Créer un Text pour afficher le nombre de likes
                Text nbrlike = new Text(String.valueOf(comment.getNbLike()));

// Créer un Text pour afficher le nombre de dislikes
                Text nbrdislike = new Text(String.valueOf(comment.getNbDislike()));
                supprimercommentaire.setText("Supprimer");
                modifiercommentaire.setText("Modifier");

                // Ajouter un événement de clic aux ImageView pour gérer les actions de like/dislike
                CommentaireService cs = new CommentaireService();

                supprimercommentaire.setOnMouseClicked(event -> {
                    // Suppression du commentaire
                    cs.delete(comment);

                    // Récupérer les détails du blog mis à jour après la suppression du commentaire
                    Blog updatedBlog = bs.getBlogById(blog.getId());
                    if (updatedBlog != null) {
                        // Afficher les détails du blog mis à jour avec la liste de commentaires mise à jour
                        displayBlogDetail(updatedBlog);
                    } else {
                        System.out.println("Impossible de récupérer les détails du blog mis à jour.");
                    }

                    // Afficher une notification
                    Image successIcon = new Image(getClass().getResourceAsStream("/Template/back/images/icons8-ok-94.png"));
                    Notifications.create()
                            .title("Suppression réussie")
                            .text("Le commentaire a été supprimé avec succès. \uD83D\uDC4D")
                            .graphic(new ImageView(successIcon))
                            .position(Pos.TOP_CENTER)
                            .show();
                });

                Admin a = new Admin(6, 4545, "", "", "");
                likeImageView.setOnMouseClicked(event -> cs.like(comment, a));
                modifiercommentaire.setStyle("-fx-text-fill:green;");
                supprimercommentaire.setStyle("-fx-text-fill:red;");

                modifiercommentaire.setOnMouseClicked(event -> {
                    // Récupérer le contenu actuel du commentaire
                    String contenuActuel = comment.getContenue();

                    // Vérifier si le champ de texte est vide ou s'il contient le contenu actuel du commentaire
                    if (inputaddcommentaire.getText().isEmpty() || inputaddcommentaire.getText().equals(contenuActuel)) {
                        // Si le champ de texte est vide ou contient déjà le contenu actuel, remplissez-le avec le contenu actuel du commentaire
                        inputaddcommentaire.setText(contenuActuel);
                    } else {
                        // Si le champ de texte contient un nouveau contenu, cela signifie que l'utilisateur a déjà modifié le commentaire
                        // Récupérer le nouveau contenu du champ de texte
                        String nouveauContenu = inputaddcommentaire.getText();

                        // Récupérer l'ID de l'utilisateur actuel (vous devrez obtenir cela à partir de votre système d'authentification)
                        int userId = 4; // ID de l'utilisateur à remplacer par l'ID réel de l'utilisateur

                        // Vérifier si le commentaire appartient à l'utilisateur actuel
                        if (cs.belongsToUser(comment.getId(), userId)) {
                            // Créer un nouvel objet Commentaire avec le nouveau contenu
                            Commentaire commentaire = new Commentaire();
                            commentaire.setId(comment.getId()); // Définir l'ID du commentaire à modifier
                            commentaire.setContenue(nouveauContenu);

                            // Appeler la méthode update avec l'ID du commentaire, le nouveau contenu et l'ID de l'utilisateur
                            cs.update(commentaire.getId(), nouveauContenu, userId);
                            

                            // Réinitialiser le champ de texte
                            inputaddcommentaire.clear();
                            Blog updatedBlog = bs.getBlogById(blog.getId());
                            if (updatedBlog != null) {
                                // Afficher les détails du blog mis à jour avec la liste de commentaires mise à jour
                                displayBlogDetail(updatedBlog);
                            }
                                listviewcommentfront.refresh();
                        } else {
                            // Afficher une alerte indiquant que l'utilisateur n'est pas autorisé à modifier ce commentaire
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Modification non autorisée");
                            alert.setHeaderText(null);
                            alert.setContentText("Vous n'êtes pas autorisé à modifier ce commentaire.");
                            alert.showAndWait();
                        }
                    }
                });


                HBox commentBox = new HBox(commentLabel, modifiercommentaire, supprimercommentaire, likeImageView, nbrlike, dislikeImageView, nbrdislike);
                commentBox.setSpacing(10);
                commentBox.setStyle("-fx-background-color: #f0f0f0; " +
                        "-fx-padding: 10px; " +
                        "-fx-border-color: #cccccc; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px;");

                nbrlike.setText(String.valueOf(comment.getNbLike()));
                nbrdislike.setText(String.valueOf(comment.getNbDislike()));
                formattedComments.add(commentBox);

            }

            // Personnaliser les cellules de la ListView pour afficher les commentaires sous forme de HBox
            listviewcommentfront.setCellFactory(param -> new ListCell<HBox>() {
                @Override
                protected void updateItem(HBox item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Afficher la HBox dans la cellule de la ListView
                        setGraphic(item);
                    }
                }
            });

            listviewcommentfront.getStyleClass().add("list-view-comment-back");

            listviewcommentfront.setItems(formattedComments);
            anchordetailleblog.setVisible(true);
            anchorlisteblog.setVisible(false);
        }
    }

    public void addCommentaireFront() {
        BlogService bs = new BlogService();
        List<Blog> lsb = bs.getAll();
        int blogId = -1;
        for (Blog bb : lsb) {
            if (contenuDetaille.getText().equals(bb.getDescription())) {
                blogId = bb.getId();
                break;
            }
        }

        if (blogId != -1) {
            String commentaireContenu = inputaddcommentaire.getText();
            String[] tabBadWords = {"fuck", "shit"};
            for (String badWord : tabBadWords) {
                commentaireContenu = commentaireContenu.replaceAll(badWord, "**".repeat(badWord.length()));
            }
            CommentaireService commentaireService = new CommentaireService();
            commentaireService.add(blogId, commentaireContenu);
            inputaddcommentaire.setText("");

            // Mettre à jour les détails du blog après l'ajout du commentaire
            Blog updatedBlog = bs.getBlogById(blogId);
            if (updatedBlog != null) {
                displayBlogDetail(updatedBlog);
            } else {
                System.out.println("Impossible de récupérer les détails du blog mis à jour.");
            }
        } else {
            System.out.println("Veuillez sélectionner un blog pour ajouter un commentaire.");
        }
    }


    public void shareFacebook() {
        BlogService bs = new BlogService();
        List<Blog> blogs = bs.getAll();

        for (Blog blog : blogs) {
            if (titreDetaille.getText().equals(blog.getTitre())) {
                bs.shareFacebook(blog);
            }
        }
    }
    public void shareTwitter() {
        BlogService bs = new BlogService();
        List<Blog> blogs = bs.getAll();

        for (Blog blog : blogs) {
            if (titreDetaille.getText().equals(blog.getTitre())) {
                bs.shareTwitter(blog);
            }
        }
    }
    public void shareGoogle() {
        BlogService bs = new BlogService();
        List<Blog> blogs = bs.getAll();

        for (Blog blog : blogs) {
            if (titreDetaille.getText().equals(blog.getTitre())) {
                bs.shareGoogle(blog);
            }
        }
    }
    public void sharePintrest() {
        BlogService bs = new BlogService();
        List<Blog> blogs = bs.getAll();

        for (Blog blog : blogs) {
            if (titreDetaille.getText().equals(blog.getTitre())) {
                bs.sharePintrest(blog);
            }
        }
    }

    public void modifierCommentaire(){


    }

    public void traduire() throws MalformedURLException, IOException {
        URL url = new URL("//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit");
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            System.out.println(inputLine);
        }
        reader.close();
    }

    private void displayImageSearchResults(String query, FlowPane imagePane) throws IOException {
        String url = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + SEARCH_ENGINE_ID + "&q=" + query + "&searchType=image";
        String response = Request.Get(url).execute().returnContent().asString();

        JSONObject json = new JSONObject(response);
        JSONArray items = json.getJSONArray("items");

        imagePane.getChildren().clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String imageUrl = item.getString("link");
            Image image = new Image(imageUrl, true);

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            imagePane.getChildren().add(imageView);
        }
    }



}
