package Service;

import Model.Medecin;
import Service.IService2;
import Util.DataBase;

import java.sql.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MedecinService implements IService2<Medecin> {
    private Connection cnx;
    public MedecinService(){
        cnx = DataBase.getInstance().getCnx();
    }


    @Override
    public void addMedecin(Medecin medecin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNaissanceFormatted = sdf.format(medecin.getDateNaissance());

        // Insertion dans la table global_user
        String requeteGlobalUser = "INSERT INTO global_user (cin, nom, prenom, genre, dateNaissance, numtel, email, password, interlock, role) " +
                "VALUES (" + medecin.getCin() + ",'" + medecin.getNom() + "','" + medecin.getPrenom() + "','" + medecin.getGenre() + "','" + dateNaissanceFormatted + "'," + medecin.getNumtel() + ",'" + medecin.getEmail() + "','" + medecin.getPassword() + "'," + medecin.isInterlock() + ",'" + medecin.getRole() + "')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(requeteGlobalUser, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = st.getGeneratedKeys();

            // Récupération de l'ID généré automatiquement
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Fermeture du ResultSet
            rs.close();

            // Insertion dans la table admin avec l'ID généré
            String requeteMedecin = "INSERT INTO medecin (id, specialite, etat) " +
                    "VALUES (" + id + " ,'"+ medecin.getSpecialite()+"',"+ medecin.getEtat()+")";
            st.executeUpdate(requeteMedecin);
            System.out.println("Medecin ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteMedecin(int id) throws SQLException {
        String requete = "DELETE FROM global_user   WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,id);
        ps.executeUpdate();
        String requete2 = "DELETE FROM medecin   WHERE id = ?";
        PreparedStatement psm2 = cnx.prepareStatement(requete2);
        psm2.setInt(1,id);
        psm2.executeUpdate();

    }

    @Override
    public void updateMedecin(Medecin medecin) throws SQLException {
        String requete = "UPDATE global_user  SET cin = ?,nom = ?, prenom = ?,genre = ?,dateNaissance = ?,numtel = ?,email = ?,password = ?,interlock = ?,role =? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,medecin.getCin());
        ps.setString(2,medecin.getNom());
        ps.setString(3,medecin.getPrenom());
        ps.setInt(4,medecin.getGenre());
        ps.setTimestamp(5, new Timestamp(medecin.getDateNaissance().getTime()));
        ps.setInt(6,medecin.getNumtel());
        ps.setString(7,medecin.getEmail());
        ps.setString(8,medecin.getPassword());
        ps.setBoolean(9,medecin.isInterlock());
        ps.setString(10,medecin.getRole());
        ps.setInt(11, medecin.getId());
        String requeteMedecin = "UPDATE medecin SET specialite = ?, etat = ? WHERE id = ?";
        PreparedStatement psMedecin = cnx.prepareStatement(requeteMedecin);
        psMedecin.setString(1, medecin.getSpecialite());
        psMedecin.setInt(2, medecin.getEtat());
        psMedecin.setInt(3, medecin.getId());
        ps.executeUpdate();
        psMedecin.executeUpdate();

    }

    @Override
    public List<Medecin> getData() {
        List<Medecin> data = new ArrayList<>();
        String requete = "SELECT medecin.*, global_user.* FROM medecin INNER JOIN global_user ON medecin.id = global_user.id";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Medecin p = new Medecin();

                // Assign fields from both tables
                p.setId(rs.getInt("id"));
                p.setCin(rs.getInt("cin"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setGenre(rs.getInt("genre"));
                p.setDateNaissance(rs.getTimestamp("Datenaissance"));
                p.setNumtel(rs.getInt("numtel"));
                p.setEmail(rs.getString("email"));
                p.setPassword(rs.getString("password"));
                p.setInterlock(rs.getBoolean("interlock"));
                p.setRole(rs.getString("role"));
                p.setSpecialite(rs.getString("specialite"));
                p.setEtat(rs.getInt("etat"));

                // Fields from global_user table
                // ... (Assign fields based on your global_user table structure)

                data.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }
}

