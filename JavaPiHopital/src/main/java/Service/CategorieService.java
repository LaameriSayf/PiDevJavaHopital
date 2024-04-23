package Service;

import Interface.ICategorie;
import Model.Categorie;
import Util.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategorieService implements ICategorie<Categorie> {
    private static Connection cnx;
    private Set<Categorie> all;

    public CategorieService(){
    this.cnx = DataBase.getInstance().getCnx();
}

    @Override
    public void addCategorie(Categorie categorie) {
        String requete="INSERT INTO categorie (nom_cat,type_cat,description_cat)"+
                "VALUES (?,?,?)";

        try {
            PreparedStatement pst=cnx.prepareStatement(requete);
            pst.setString(1, categorie.getNom_cat());
            pst.setString(2, categorie.getType_cat());
            pst.setString(3, categorie.getDescription_cat());
            pst.executeUpdate();
            System.out.println("Categorie Ajouté ");

        } catch (SQLException e) {
           throw new RuntimeException("erreur au nivau de l'ajout"+e) ;
        }

    }

    @Override
    public void deleteCategorie(Categorie categorie) {
String requete= "DELETE FROM `categorie` WHERE `id` = ?";
        try {
            PreparedStatement pst= cnx.prepareStatement(requete);
            pst.setInt(1,categorie.getId());
            pst.executeUpdate();
            System.out.println("Categorie est supprimer");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCategorie(Categorie categorie) {
        String requete="UPDATE `categorie` SET `nom_cat` = ? , `type_cat`=? , `description_cat`=? WHERE `id` = ? ";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, categorie.getNom_cat());
            pst.setString(2, categorie.getType_cat());
            pst.setString(3, categorie.getDescription_cat());
            pst.setInt(4,categorie.getId());
            pst.executeUpdate();
            System.out.println("Categorie updated ");
        } catch (SQLException e) {
            throw new RuntimeException("message ghadika"+e);
        }


    }

    @Override
    public List<Categorie> getData() {
        List<Categorie> data = new ArrayList<>();
        String requete = "SELECT * FROM `categorie`";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Categorie c = new Categorie();
                c.setId(rs.getInt(1));
                c.setNom_cat(rs.getString("nom_cat"));
                c.setType_cat(rs.getString("type_cat"));
                c.setDescription_cat(rs.getString("description_cat"));
                data.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des données : " + e);
        }
        return data;
    }



    public Set<Categorie> getAll() {
        return (Set<Categorie>) getData();
    }

    public void setAll(Set<Categorie> all) {
        this.all = all;
    }

}
