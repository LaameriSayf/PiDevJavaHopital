package Controller;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import Model.Admin;
import Model.Blog;
import Model.Commentaire;
import Service.BlogService;
import Service.CommentaireService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import org.controlsfx.control.Notifications;

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
    private BlogService blogService = new BlogService();
    private List<Blog> blogList = blogService.getAll();

    @FXML
    public void initialize() {
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
    public void switchbloglist() {
        if (anchordetailleblog != null) {
            anchordetailleblog.setVisible(!anchordetailleblog.isVisible());
            anchorlisteblog.setVisible(true);
        }
    }
    public void displayBlogDetail(Blog blog) {
        if (blog != null) {

            titreDetaille.setText(blog.getTitre());
            contenuDetaille.setText(blog.getDescription());
            textdate.setText(blog.getDate().toString());
            textlieu.setText(blog.getLieu());
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


}
