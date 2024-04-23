package Service;

import Interface.IMedicament;
import Model.Medicament;
import Util.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicamentService implements IMedicament <Medicament> {
    private Connection cnx;
    public MedicamentService(){
        this.cnx = DataBase.getInstance().getCnx();
    }
    @Override
    public void addMedicament(Medicament medicament) {
        String requete="INSERT INTO medicament (ref_med,nom_med,date_amm,date_expiration,qte,description,etat,image)"+
                "VALUES (?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pst=cnx.prepareStatement(requete);
            pst.setString(1, medicament.getRef_med());
            pst.setString(2, medicament.getNom_med());
            pst.setDate(3, Date.valueOf(medicament.getDate_amm()));
            pst.setDate(4, Date.valueOf(medicament.getDate_expiration()));
            pst.setInt(5, medicament.getQte());
            pst.setString(6, medicament.getDescription());
            pst.setString(7, medicament.getEtat());
            pst.setString(8, medicament.getImage());

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
        String requete="UPDATE `medicament` SET `ref_med` = ? , `nom_med`=? , `date_amm`=?,`date_expiration` = ? , `qte`=? , `description`=?,`etat` = ? , `image`=?  WHERE `id` = ? ";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, medicament.getRef_med());
            pst.setString(2, medicament.getNom_med());
            pst.setDate(3, Date.valueOf(medicament.getDate_amm()));
            pst.setDate(4, Date.valueOf(medicament.getDate_expiration()));
            pst.setInt(5, medicament.getQte());
            pst.setString(6, medicament.getDescription());
            pst.setString(7, medicament.getEtat());
            pst.setString(8, medicament.getImage());
            pst.setInt(9,medicament.getId());
            pst.executeUpdate();
            System.out.println("Medicament updated ");
        } catch (SQLException e) {
            throw new RuntimeException("message ghadika"+e);
        }


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


}
