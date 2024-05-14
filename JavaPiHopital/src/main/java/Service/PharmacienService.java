package Service;

import Model.Pharmacien;
import Util.DataBase;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PharmacienService implements IService4<Pharmacien> {
    private Connection cnx;
    public PharmacienService(){
        cnx = DataBase.getInstance().getCnx();
    }

    @Override
    public void addPharmacien(Pharmacien pharmacien) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNaissanceFormatted = sdf.format(pharmacien.getDateNaissance());

        // Insertion dans la table global_user
        String requeteGlobalUser = "INSERT INTO global_user (cin, nom, prenom, genre, dateNaissance, numtel, email, password, interlock, role) " +
                "VALUES (" + pharmacien.getCin() + ",'" + pharmacien.getNom() + "','" + pharmacien.getPrenom() + "','" + pharmacien.getGenre() + "','" + dateNaissanceFormatted + "'," + pharmacien.getNumtel() + ",'" + pharmacien.getEmail() + "','" + pharmacien.getPassword() + "'," + pharmacien.isInterlock() + ",'" + pharmacien.getRole() + "')";
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
            String requetePharmacien = "INSERT INTO pharmacien (id, poste) " +
                    "VALUES (" + id + " ,"+ pharmacien.getPoste()+")";
            st.executeUpdate(requetePharmacien);
            System.out.println("pharmacien ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void deletePharmacien(int id) throws SQLException {
        String requete = "DELETE FROM global_user   WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,id);
        ps.executeUpdate();
        String requete2 = "DELETE FROM pharmacien   WHERE id = ?";
        PreparedStatement psm2 = cnx.prepareStatement(requete2);
        psm2.setInt(1,id);
        psm2.executeUpdate();

    }

    @Override
    public void updatePharmacien(Pharmacien pharmacien) throws SQLException {
        String requete = "UPDATE global_user  SET cin = ?,nom = ?, prenom = ?,genre = ?,dateNaissance = ?,numtel = ?,email = ?,password = ?,interlock = ?,role =? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,pharmacien.getCin());
        ps.setString(2,pharmacien.getNom());
        ps.setString(3,pharmacien.getPrenom());
        ps.setInt(4,pharmacien.getGenre());
        ps.setTimestamp(5, new Timestamp(pharmacien.getDateNaissance().getTime()));
        ps.setInt(6,pharmacien.getNumtel());
        ps.setString(7,pharmacien.getEmail());
        ps.setString(8,pharmacien.getPassword());
        ps.setBoolean(9,pharmacien.isInterlock());
        ps.setString(10,pharmacien.getRole());
        ps.setInt(11, pharmacien.getId());
        String requetePharmacien = "UPDATE pharmacien SET poste = ? WHERE id = ?";
        PreparedStatement psPharmacien = cnx.prepareStatement(requetePharmacien);
        psPharmacien.setInt(1, pharmacien.getPoste());
        psPharmacien.setInt(2, pharmacien.getId());
        ps.executeUpdate();
        psPharmacien.executeUpdate();

    }

    @Override
    public List<Pharmacien> getData() {
        List<Pharmacien> data = new ArrayList<>();
        String requete = "SELECT pharmacien.*, global_user.* FROM pharmacien INNER JOIN global_user ON pharmacien.id = global_user.id";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Pharmacien p = new Pharmacien();

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
                p.setPoste(rs.getInt("poste"));

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
