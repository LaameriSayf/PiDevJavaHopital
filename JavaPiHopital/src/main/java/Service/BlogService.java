package Service;

import Interface.IBlog;
import Model.Admin;
import Model.Blog;
import Model.Commentaire;
import Model.CtegorieBlog;
import Util.DataBase;
import java.net.URI;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlogService implements IBlog<Blog> {
    private Connection cnx;
    ArrayList<Commentaire> cmtr;

    public BlogService(){
        this.cnx = DataBase.getInstance().getCnx();

    }

    /********************************Ajouter Blog En meme temps affecter Categorie au blog ***********************************/
    @Override
    public void add(Blog blog) {
        String rqt = "INSERT INTO `blog` (`categorieblogs_id`, `idadmin_id`, `titre`, `description`, `datepub`, `image`, `lieu`, `rate`) VALUES (?,NULL,?,?,?,?,?,?);";
        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            if (blog.getCtgb() != null) {
                stm.setInt(1, blog.getCtgb().getId());
            } else {
                // Si la catégorie est null, utilisez null dans la requête SQL
                stm.setNull(1, java.sql.Types.INTEGER);
            }
            stm.setString(2, blog.getTitre());
            stm.setString(3, blog.getDescription());
            // Vérifiez si la date est null
            if (blog.getDate() != null) {
                Date sqlDate = Date.valueOf(blog.getDate());
                stm.setDate(4, sqlDate);
            } else {
                // Si la date est null, utilisez null dans la requête SQL
                stm.setNull(4, java.sql.Types.DATE);
            }
            stm.setString(5, blog.getIamge());
            stm.setString(6, blog.getLieu());
            stm.setFloat(7, blog.getRate());

            // Vérifiez si la catégorie est null
            stm.executeUpdate();
            System.out.println("Ajout de blog effectué avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException("Ereure au niveau de fonctin ajouter Blog"+e);
        }
    }

    /**************************************Afficher Tous les Blogs Avec Categorie associer*********************************************/
    @Override
    public ArrayList<Blog> getAll() {
        ArrayList<Blog> lbm=new ArrayList<>();
        String rqt="SELECT * FROM `blog`";
        try {
            PreparedStatement stm= cnx.prepareStatement(rqt);
            ResultSet rs= stm.executeQuery();
            CategoriBlogService categoriBlogService = new CategoriBlogService();
            while (rs.next()){
                Blog b=new Blog();
                b.setId(rs.getInt("id"));
                b.setTitre(rs.getString("titre"));
                b.setDescription(rs.getString("description"));
                b.setDate(rs.getDate("datepub").toLocalDate()); // Correction : Utilisation de setDate au lieu de stm.setDate                        b.setIamge(rs.getString("image"));
                b.setLieu(rs.getString("lieu"));
                b.setRate(rs.getFloat("rate"));
                b.setIamge(rs.getString("image"));


                // Récupérer l'ID de la catégorie
                int categoryId = rs.getInt("categorieblogs_id");

                // Utiliser cet ID pour obtenir l'objet CatégorieBlog correspondant
                CtegorieBlog categorieBlog = categoriBlogService.getCategorieBlogById(categoryId);

                // Définir la catégorie dans l'objet Blog
                b.setCtgb(categorieBlog);

                lbm.add(b);
            }
            System.out.println("Affichage de blog avec succes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lbm;
    }

    /************************************** Modifier Blog Avec Categorie de bLogs Associer*************************************************/

    @Override
    public void update(Blog blog) {
        String rqt = "UPDATE blog SET categorieblogs_id = ? ,titre = ?, description = ?,datepub=?,image=?,lieu=?,rate=? WHERE id = ?";
        try {
            PreparedStatement stm= cnx.prepareStatement(rqt);
            if (blog.getCtgb() != null) {
                stm.setInt(1, blog.getCtgb().getId());
            } else {
                // Si la catégorie est null, utilisez null dans la requête SQL
                stm.setNull(1, java.sql.Types.INTEGER);
            }
            stm.setString(2, blog.getTitre());
            stm.setString(3, blog.getDescription());
            // Vérifiez si la date est null
            if (blog.getDate() != null) {
                Date sqlDate = Date.valueOf(blog.getDate());
                stm.setDate(4, sqlDate);
            } else {
                // Si la date est null, utilisez null dans la requête SQL
                stm.setNull(4, java.sql.Types.DATE);
            }
            stm.setString(5, blog.getIamge());
            stm.setString(6, blog.getLieu());
            stm.setFloat(7,blog.getRate());
            stm.setInt(8,blog.getId());
            stm.executeUpdate();
            System.out.println("Le blog est modifier aevec succes.");
        } catch (SQLException e) {
            throw new RuntimeException("Ereurer au niveau fonction update de blog"+e);
        }

    }

    /****************************************GetBlogById**************************************************************************************/
    public  Blog getBlogById(int id) {
        List<Blog> blog = getAll(); // Récupérer la liste des catégories une seule fois
        Optional<Blog> optionalCategory =   blog.stream().filter(e->e.getId()==id).findFirst();

        return optionalCategory.orElse(null) ;
    }

    public String getTitleOfBlogById(int id) {
        List<Blog> blogs = getAll(); // Récupérer la liste des blogs une seule fois
        Optional<Blog> optionalBlog = blogs.stream().filter(e -> e.getId() == id).findFirst();

        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            CategoriBlogService ctb = new CategoriBlogService();
            CtegorieBlog cb = ctb.getCategorieBlogById(blog.getCtgb().getId());
            if (cb != null) {
                return cb.getTitrecategorie();
            }
        }

        return null;
    }


    public String getDescriptionOfBlogById(int id) {
        List<Blog> blogs = getAll(); // Récupérer la liste des blogs une seule fois
        Optional<Blog> optionalBlog = blogs.stream().filter(e -> e.getId() == id).findFirst();

        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            CategoriBlogService ctb = new CategoriBlogService();
            CtegorieBlog cb = ctb.getCategorieBlogById(blog.getCtgb().getId());
            if (cb != null) {
                return cb.getDescription();
            }
        }

        return null;
    }


    /***********************************Supprimer Blogs et leur affectaion de blogs associer***************************************************/

    @Override
    public boolean delete(Blog blog) {
        try {
            // Supprimer d'abord les commentaires associés à ce blog
            String deleteCommentsQuery = "DELETE FROM commentaire WHERE idblog_id=?";
            PreparedStatement deleteCommentsStm = cnx.prepareStatement(deleteCommentsQuery);
            deleteCommentsStm.setInt(1, blog.getId());
            deleteCommentsStm.executeUpdate();
            CommentaireService commentaireService = new CommentaireService();

// Supprimer un commentaire en utilisant la méthode delete
            //Commentaire commentaire =; /* Obtenez le commentaire que vous souhaitez supprimer */;
            //commentaireService.delete(commentaire);


            // Ensuite, supprimer le blog
            String deleteBlogQuery = "DELETE FROM blog WHERE id=?";
            PreparedStatement deleteBlogStm = cnx.prepareStatement(deleteBlogQuery);
            deleteBlogStm.setInt(1, blog.getId());
            int rowsAffected = deleteBlogStm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Blog est supprimé avec succès");
                return true;
            } else {
                System.out.println("Aucun blog trouvé avec l'ID spécifié : " + blog.getId());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du blog : " + e.getMessage());
            return false;
        }
    }


    /***********************************Rechercher Blogs selon Tous critere ****************************************************************/
 /*  public List<Blog> RechercherSelonTousCriterer(Blog b){
       List<Blog> lbs=getAll();
       lbs.stream().filter().findAny();
   }*/

    /*************************************Partager Blog SUr facebook*******************************************************************************/
    private String serializeLocation(String lieu) {
        return lieu;
    }

    public void shareMaps(String lieu) {
        String mapsUrl = "https://www.google.com/maps/search/?api=1&query=";

        String locationString = serializeLocation(lieu);

        try {
            String encodedContent = URLEncoder.encode(locationString, "UTF-8");
            mapsUrl += encodedContent;
            Desktop.getDesktop().browse(new URI(mapsUrl));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void shareFacebook(Blog blog) {
        String facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=";

        String blogString = serializeBlog(blog);

        try {
            String encodedContent = URLEncoder.encode(blogString, "UTF-8");
            facebookUrl += encodedContent;
            Desktop.getDesktop().browse(new URI(facebookUrl));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void shareTwitter(Blog blog) {
        String facebookUrl = "https://twitter.com/intent/tweet?text=";

        String blogString = serializeBlog(blog);

        try {
            String encodedContent = URLEncoder.encode(blogString, "UTF-8");
            facebookUrl += encodedContent;
            Desktop.getDesktop().browse(new URI(facebookUrl));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void shareGoogle(Blog blog) {
        String facebookUrl = "https://www.google.com/search?q=";

        String blogString = serializeBlog(blog);

        try {
            String encodedContent = URLEncoder.encode(blogString, "UTF-8");
            facebookUrl += encodedContent;
            Desktop.getDesktop().browse(new URI(facebookUrl));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sharePintrest(Blog blog) {
        String facebookUrl = "https://www.pinterest.com/pin/create/button/?url=";

        String blogString = serializeBlog(blog);

        try {
            String encodedContent = URLEncoder.encode(blogString, "UTF-8");
            facebookUrl += encodedContent;
            Desktop.getDesktop().browse(new URI(facebookUrl));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String serializeBlog(Blog blog) {

        String serializedBlog = "Date: " + blog.getDate() +
                ", Description: " + blog.getDescription() +
                ", Lieu: " + blog.getLieu();
        return serializedBlog;
    }


    public int NombreBlog(){
        String rqt="SELECT COUNT(*) FROM `blog`";
        int nombreblog=0;

        try {
            PreparedStatement stm=cnx.prepareStatement(rqt);
            ResultSet rs=stm.executeQuery();
            while (rs.next()){
                nombreblog=rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nombreblog;
    }



    public List<Commentaire> getCommentsByBlogId(int blogId) {
        List<Commentaire> comments = new ArrayList<>();
        String query = "SELECT commentaire.id, commentaire.contenue, admin.id AS admin_id, global_user.cin AS admin_cin, global_user.nom AS admin_nom, global_user.prenom AS admin_prenom " +
                "FROM commentaire " +
                "JOIN admin ON commentaire.idadmin_id = admin.id " +
                "JOIN global_user ON admin.id = global_user.id " +
                "WHERE commentaire.idblog_id = ?";
        try {
            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setInt(1, blogId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(resultSet.getInt("id"));
                commentaire.setContenue(resultSet.getString("contenue"));

                // Create an Admin object and set its attributes
                Admin admin = new Admin();
                admin.setId(resultSet.getInt("admin_id"));
                admin.setCin(resultSet.getInt("admin_cin"));
                admin.setNom(resultSet.getString("admin_nom"));
                admin.setPrenom(resultSet.getString("admin_prenom"));

                // Set the Admin object to the Commentaire object
                commentaire.setAdmin(admin);

                comments.add(commentaire);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commentaires : " + e.getMessage());
        }
        return comments;
    }



}