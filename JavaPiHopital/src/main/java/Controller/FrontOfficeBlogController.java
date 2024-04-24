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
    private BlogService blogService = new BlogService();
    private List<Blog> blogList = blogService.getAll();

    @FXML
    public void initialize() {
        btnretournerback.setOnMouseClicked(event -> switchbloglist());
        btnajoutercommentfront.setOnMouseClicked(event ->addcommentairefront());

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
                                imageView.setFitWidth(300); // Ajustez la largeur de l'image
                                imageView.setFitHeight(200); // Ajustez la hauteur de l'image

                                AnchorPane anchorPane = new AnchorPane(imageView);
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
                Image image = new Image(fileName,343 , 350, false, true);
                imageviewdetaille.setImage(image);
            } else {
                imageviewdetaille.setImage(null);
            }
            BlogService bs=new BlogService();
            String titrecategories=bs.getTitleOfBlogById(blog.getId());
            String descriptioncategorie=bs.getDescriptionOfBlogById(blog.getId());
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
                Label commentLabel = new Label(  comment.getAdmin().getNom() + " " + comment.getAdmin().getPrenom() + "\n" +
                        comment.getContenue());
                // Créer deux ImageView pour les logos de like et dislike
                ImageView likeImageView = new ImageView("Template/front/images/icons8-pouce-en-l'air-16.png");
                ImageView dislikeImageView = new ImageView("Template/front/images/icons8-pouces-vers-le-bas-24.png");
                Text supprimercommentaire=new Text();
                Text modifiercommentaire=new Text();
                // Créer un Text pour afficher le nombre de likes
                Text nbrlike = new Text(String.valueOf(comment.getNbLike()));

// Créer un Text pour afficher le nombre de dislikes
                Text nbrdislike = new Text(String.valueOf(comment.getNbDislike()));
                supprimercommentaire.setText("Supprimer");
                modifiercommentaire.setText("Modifier");

                // Ajouter un événement de clic aux ImageView pour gérer les actions de like/dislike
                CommentaireService cs=new CommentaireService();
                supprimercommentaire.setOnMouseClicked(event -> {
                    // Suppression du commentaire
                    cs.delete(comment);

                    // Afficher une notification
                    Image successIcon = new Image(getClass().getResourceAsStream("/Template/back/images/icons8-ok-94.png"));

// Afficher une notification personnalisée
                    Notifications.create()
                            .title("Suppression réussie")
                            .text("Le commentaire a été supprimé avec succès. \uD83D\uDC4D")
                            .graphic(new ImageView(successIcon))
                            .position(Pos.TOP_CENTER)
                            .show();
                });
                Admin a=new Admin(4,4545,"","","");
                likeImageView.setOnMouseClicked(event-> cs.like(comment,a));




                // Créer une HBox pour contenir le label et les ImageView
                HBox commentBox = new HBox(commentLabel,modifiercommentaire,supprimercommentaire, likeImageView,nbrlike, dislikeImageView,nbrdislike);

                commentBox.setSpacing(10); // Espacement entre les éléments dans la HBox
// Créer une HBox pour contenir le label et les ImageView
                commentBox.setSpacing(10); // Espacement entre les éléments dans la HBox
                commentBox.setStyle("-fx-background-color: #f0f0f0; " + // Couleur de fond
                        "-fx-padding: 10px; " + // Rembourrage intérieur
                        "-fx-border-color: #cccccc; " + // Couleur de la bordure
                        "-fx-border-width: 1px; " + // Largeur de la bordure
                        "-fx-border-radius: 5px;"); // Rayon de la bordure arrondie
                nbrlike.setText(String.valueOf(comment.getNbLike()));
                nbrdislike.setText(String.valueOf(comment.getNbDislike()));
                // Ajouter la HBox à la liste des commentaires formatés
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

            // Appliquer le style CSS pour la ListView
            listviewcommentfront.getStyleClass().add("list-view-comment-back");

            // Afficher la liste des commentaires formatés dans le composant ListView
            listviewcommentfront.setItems(formattedComments);
            anchordetailleblog.setVisible(true);
            anchorlisteblog.setVisible(false);
        }
        // Obtenir l'indice de la ligne sélectionnée dans la ListView
        int selectedRowIndex = listViewFrontBlog.getSelectionModel().getSelectedIndex();

        // Vérifier si une ligne est sélectionnée
        if (selectedRowIndex >= 0) {
            // Obtenir l'objet Blog sélectionné dans la ListView
            List<Blog> selectedBlogs = listViewFrontBlog.getItems().get(selectedRowIndex);

            // Vérifier si des blogs sont sélectionnés dans cette ligne
            if (selectedBlogs != null && !selectedBlogs.isEmpty()) {
                // Obtenir l'indice de l'élément sélectionné dans la liste de blogs
                int selectedBlogIndex = listViewFrontBlog.getSelectionModel().getSelectedIndices().get(0);

                // Récupérer le blog correspondant à l'indice dans la liste
                Blog selectedBlog = selectedBlogs.get(selectedBlogIndex);

                // Récupérer l'identifiant du blog
                int blogId = selectedBlog.getId();

                // Récupérer le contenu du commentaire à partir de l'entrée utilisateur
                String commentaireContenu = inputaddcommentaire.getText();

                // Liste des gros mots
                String[] tabbadwords = {"fuck", "shit","merde"};

                // Filtrer les gros mots
                for (String badword : tabbadwords) {
                    // Remplacer chaque lettre du gros mot par "**"
                    commentaireContenu = commentaireContenu.replaceAll(badword, "**".repeat(badword.length()));
                }

                // Ajouter le commentaire en utilisant le service de commentaire
                CommentaireService commentaireService = new CommentaireService();
                commentaireService.add(blogId, commentaireContenu);

                // Rafraîchir la liste des commentaires
                listviewcommentfront.refresh();
            } else {
                System.out.println("Veuillez sélectionner un blog pour ajouter un commentaire.");
            }
        } else {
            System.out.println("Veuillez sélectionner une ligne pour ajouter un commentaire.");
        }
    }
    public int getSelectedBlogId() {
        int selectedRowIndex = listViewFrontBlog.getSelectionModel().getSelectedIndex();
        if (selectedRowIndex >= 0) {
            List<Blog> selectedBlogs = listViewFrontBlog.getItems().get(selectedRowIndex);
            if (selectedBlogs != null && !selectedBlogs.isEmpty()) {
                int selectedBlogIndex = listViewFrontBlog.getSelectionModel().getSelectedIndices().get(1);
                Blog selectedBlog = selectedBlogs.get(selectedBlogIndex);
                return selectedBlog.getId();
            }
        }
        return -1;
    }
    public void addcommentairefront() {
        int blogId = getSelectedBlogId();
        if (blogId != -1) {
            String commentaireContenu = inputaddcommentaire.getText();
            String[] tabbadwords = {"fuck", "shit"};
            for (String badword : tabbadwords) {
                commentaireContenu = commentaireContenu.replaceAll(badword, "**".repeat(badword.length()));
            }
            CommentaireService commentaireService = new CommentaireService();
            commentaireService.add(blogId, commentaireContenu);
            listviewcommentfront.refresh();
        } else {
            System.out.println("Veuillez sélectionner un blog pour ajouter un commentaire.");
        }
    }


}
