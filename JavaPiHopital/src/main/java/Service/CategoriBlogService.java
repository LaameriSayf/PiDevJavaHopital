package Service;


import Interface.IcategoriBlog;
import Model.Blog;
import Model.CtegorieBlog;
import Util.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CategoriBlogService implements IcategoriBlog<CtegorieBlog> {

    private final Connection cnx;

    public CategoriBlogService() {
       this.cnx = DataBase.getInstance().getCnx();
    }

    /*********************************AddCategorieBlog***************************************************/
    @Override
    public void add(CtegorieBlog ctegorieBlog) {
        String rqt = "INSERT INTO `categorieblogs`(`titrecategorie`,`descriptioncategorie`) VALUES (?,?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            stm.setString(1, ctegorieBlog.getTitrecategorie());
            stm.setString(2, ctegorieBlog.getDescription());
            stm.executeUpdate();
            System.out.println("Categorie Blog Ajouter Avec succes avec Titre" + "" + ctegorieBlog.getTitrecategorie());
        } catch (SQLException e) {
            throw new RuntimeException(e + "Ereure Dans la methode Ajouter Categorie Blog");
        }


    }

    /*******************************GetAllListCatgorieBlog***************************************************************/

    @Override
    public ArrayList<CtegorieBlog> getAll() {
        ArrayList<CtegorieBlog> ListCategBlog = new ArrayList<>();

        String rqt = "SELECT * FROM `categorieblogs`";
        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            ResultSet rs = stm.executeQuery(rqt);


            while (rs.next()) {
                CtegorieBlog cbs = new CtegorieBlog();
                cbs.setId(rs.getInt("id"));
                cbs.setTitrecategorie(rs.getString("titrecategorie"));
                cbs.setDescription(rs.getString("descriptioncategorie"));
                ListCategBlog.add(cbs);
            }


        } catch (SQLException e) {
            throw new RuntimeException("Ereure Au niveua fonction Affichage Categorie Blog" + e);
        }
        return ListCategBlog;

    }
    public CtegorieBlog getCatBlogByNom(String titre) {
        List<CtegorieBlog> Categ = getAll(); // Récupérer la liste des catégories une seule fois
        Optional<CtegorieBlog> optionalCategory =   Categ.stream().filter(e->e.getTitrecategorie().equals(titre)).findFirst();

        return optionalCategory.orElse(null) ;
    }
    /***********************************UpdateCategorieBlog**********************************************/

    @Override
    public void update(CtegorieBlog ctegorieBlog) {
        String rqt = "UPDATE categorieblogs SET titrecategorie = ?, descriptioncategorie = ? WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = cnx.prepareStatement(rqt);

            stm.setString(1, ctegorieBlog.getTitrecategorie());
            stm.setString(2, ctegorieBlog.getDescription());
            stm.setInt(3, ctegorieBlog.getId());
            int rowsUpdated = stm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("La mise à jour de la catégorie de blog a été effectuée avec succès.");
            } else {
                System.out.println("Aucune catégorie de blog n'a été mise à jour.");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /*****************************************SupprimerCategorieBlog************************************************/

    @Override
    public boolean delete(CtegorieBlog ctegorieBlog) {
        String rqt = "DELETE FROM categorieblogs WHERE id=?";
        try (PreparedStatement stm = cnx.prepareStatement(rqt)) {
            stm.setInt(1, ctegorieBlog.getId());
            int rowsDeleted = stm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("La catégorie de blog a été supprimée avec succès.");
                return true;
            } else {
                System.out.println("Aucune catégorie de blog n'a été supprimée.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la catégorie de blog.", e);
        }
    }



    /**********************************RechercherCategorieBlogParTitreOrDescription*************************************/
    @Override
    public ArrayList<CtegorieBlog> getBytitreDescription(CtegorieBlog ctegorieBlog) {
        ArrayList<CtegorieBlog> listrechercheCatblog = new ArrayList<>();
        String rqt = "SELECT * FROM categorieblogs WHERE titrecategorie LIKE ? OR descriptioncategorie LIKE ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            stm.setString(1, "%" + ctegorieBlog.getTitrecategorie() + "%");
            stm.setString(2, "%" + ctegorieBlog.getDescription() + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                CtegorieBlog ctm = new CtegorieBlog();
                ctm.setId(rs.getInt("id"));
                ctm.setTitrecategorie(rs.getString("titrecategorie"));
                ctm.setDescription(rs.getString("descriptioncategorie"));
                listrechercheCatblog.add(ctm);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des catégories de blog par titre ou description.", e);
        }

        return listrechercheCatblog.stream()
                .filter(cat -> cat.getTitrecategorie().contains(ctegorieBlog.getTitrecategorie())
                        || cat.getDescription().contains(ctegorieBlog.getDescription()))
                .collect(Collectors.toCollection(ArrayList::new));
    }




    /****************************TrierByTitre******************************************************/
    @Override
    public ArrayList<CtegorieBlog> TrierByTitre(boolean ascending) {
        ArrayList<CtegorieBlog> sortedCategories = new ArrayList<>();
        String sortOrder = ascending ? "ASC" : "DESC";
        String rqt = "SELECT * FROM categorieblogs ORDER BY titrecategorie " + sortOrder;
        try  {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                CtegorieBlog ctm = new CtegorieBlog();
                ctm.setId(rs.getInt("id"));
                ctm.setTitrecategorie(rs.getString("titrecategorie"));
                ctm.setDescription(rs.getString("descriptioncategorie"));
                sortedCategories.add(ctm);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du tri des catégories de blog par titre.", e);
        }
        return sortedCategories;
    }

    /*********************************  TrierByDescription ********************************************************/
    @Override
    public ArrayList<CtegorieBlog> TrierByDescription(boolean ascending) {
        ArrayList<CtegorieBlog> lctm=new ArrayList<>();
        String sortorder= ascending ? "ASC" :"DESC";
        String rqt="SELECT * FROM categorieblogs ORDER BY descriptioncategorie "+sortorder;
        try {
            PreparedStatement stm= cnx.prepareStatement(rqt);
            ResultSet rs=stm.executeQuery(rqt);
            while (rs.next()){
                CtegorieBlog ctm =new CtegorieBlog();
                ctm.setId(rs.getInt("id"));
                ctm.setTitrecategorie(rs.getString("titrecategorie"));
                ctm.setDescription(rs.getString("descriptioncategorie"));
                lctm.add(ctm);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lctm;
    }
    /*********************************************RechercherCategorie Blogs By id **********************************************/
    public  CtegorieBlog getCategorieBlogById(int id) {

       // String rqt="SELECT * FROM `categorieblogs` WHERE id=?";
        /*try {
            PreparedStatement stm= cnx.prepareStatement(rqt);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        List<CtegorieBlog> categories = getAll(); // Récupérer la liste des catégories une seule fois
        Optional<CtegorieBlog> optionalCategory =   categories.stream().filter(e->e.getId()==id).findFirst();

        return optionalCategory.orElse(null) ;
    }


    /****************************************************Fin**********************************************/


    public int NombreCategorieBlog() {
        String rqt = "SELECT COUNT(*) FROM `categorieblogs`";
        int nombreCategories = 0; // Initialiser à zéro pour gérer les cas d'erreur ou aucune catégorie

        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            ResultSet resultSet = stm.executeQuery();

            // Vérifier si le résultat contient une valeur
            if (resultSet.next()) {
                nombreCategories = resultSet.getInt(1); // Récupérer la première colonne du résultat (le COUNT)
            }

            resultSet.close(); // Fermer le ResultSet pour libérer les ressources
            stm.close(); // Fermer la PreparedStatement pour libérer les ressources
        } catch (SQLException e) {
            throw new RuntimeException(e); // Lancer une exception en cas d'erreur
        }

        return nombreCategories;
    }


}

