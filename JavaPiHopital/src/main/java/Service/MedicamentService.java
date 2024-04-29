package Service;

import Interface.IMedicament;
import Model.Categorie;
import Model.Medicament;
import Util.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class MedicamentService implements IMedicament <Medicament> {
    private Connection cnx;
    public MedicamentService(){
        this.cnx = DataBase.getInstance().getCnx();
    }
    @Override
    public void addMedicament(Medicament medicament) {
        String requete="INSERT INTO medicament (categorie_id,ref_med,nom_med,date_amm,date_expiration,qte,description,etat,image)"+
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pst=cnx.prepareStatement(requete);
            pst.setInt(1, medicament.getCategorie().getId());
            pst.setString(2, medicament.getRef_med());
            pst.setString(3, medicament.getNom_med());
            pst.setDate(4, Date.valueOf(medicament.getDate_amm()));
            pst.setDate(5, Date.valueOf(medicament.getDate_expiration()));
            pst.setInt(6, medicament.getQte());
            pst.setString(7, medicament.getDescription());
            pst.setString(8, medicament.getEtat());
            pst.setString(9, medicament.getImage());

            pst.executeUpdate();
            System.out.println("Medicament Ajouté ");

        } catch (SQLException e) {
            throw new RuntimeException("erreur au nivau de l'ajout"+e) ;
        }


    }

    @Override
    public void deleteMedicament(Medicament medicament) {
        String requete= "DELETE FROM `medicament` WHERE `id` = ?";
        try {
            PreparedStatement pst= cnx.prepareStatement(requete);
            pst.setInt(1,medicament.getId());
            pst.executeUpdate();
            System.out.println("Medicament est supprimer");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMedicament(Medicament medicament) {
        String requete="UPDATE `medicament` SET `categorie_id` = ? ,`ref_med` = ? , `nom_med`=? , `date_amm`=?,`date_expiration` = ? , `qte`=? , `description`=?,`etat` = ? , `image`=?  WHERE `id` = ? ";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, medicament.getCategorie().getId());
            pst.setString(2, medicament.getRef_med());
            pst.setString(3, medicament.getNom_med());
            pst.setDate(4, Date.valueOf(medicament.getDate_amm()));
            pst.setDate(5, Date.valueOf(medicament.getDate_expiration()));
            pst.setInt(6, medicament.getQte());
            pst.setString(7, medicament.getDescription());
            pst.setString(8, medicament.getEtat());
            pst.setString(9, medicament.getImage());
            pst.setInt(10,medicament.getId());
            pst.executeUpdate();
            System.out.println("Medicament updated ");
        } catch (SQLException e) {
            throw new RuntimeException("message ghadika"+e);
        }


    }

    public  Categorie getBlogById(int id) {
        CategorieService cs = new CategorieService();
        List<Categorie> blog = cs.getData(); // Récupérer la liste des catégories une seule fois
        Optional<Categorie> optionalCategory =   blog.stream().filter(e->e.getId()==id).findFirst();

        return optionalCategory.orElse(null) ;
    }

    @Override
    public List<Medicament> getData() {
        List<Medicament> data = new ArrayList<>();
        String requete = "" +
                "SELECT * FROM `medicament`";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs =st.executeQuery(requete);
            while(rs.next()){
                Medicament m = new Medicament();
                m.setId(rs.getInt(1));
                int categorie_id  = rs.getInt("categorie_id");
                Categorie CategorieMedicament= getBlogById(categorie_id);
                m.setCategorie(CategorieMedicament);
                m.setRef_med(rs.getString("ref_med"));
                m.setNom_med(rs.getString("nom_med"));
                m.setDate_amm(rs.getDate("date_amm").toLocalDate());
                m.setDate_expiration(rs.getDate("date_expiration").toLocalDate());
                m.setQte(rs.getInt("qte"));
                m.setDescription(rs.getString("description"));
                m.setEtat(rs.getString("etat"));
                m.setImage(rs.getString("image"));
                data.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("message !!!!!"+e);
        }
        return data;
    }
    public boolean referenceExists(String reference) {
        for (Medicament medicament : getData()) {
            if (medicament.getRef_med().equals(reference)) {
                return true;
            }
        }
        return false;
    }


}
