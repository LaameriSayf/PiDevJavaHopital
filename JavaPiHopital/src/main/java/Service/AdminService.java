package Service;


import Model.Admin;
import Util.DataBase;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;


public class AdminService implements IService1<Admin> {
    private Connection cnx;
    public AdminService(){
        cnx = DataBase.getInstance().getCnx();
    }

    @Override
    public void addAdmin(Admin admin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNaissanceFormatted = sdf.format(admin.getDateNaissance());

        // Insertion dans la table global_user
        String requeteGlobalUser = "INSERT INTO global_user (cin, nom, prenom, genre, dateNaissance, numtel, email, password, interlock, role) " +
                "VALUES (" + admin.getCin() + ",'" + admin.getNom() + "','" + admin.getPrenom() + "','" + admin.getGenre() + "','" + dateNaissanceFormatted + "'," + admin.getNumtel() + ",'" + admin.getEmail() + "','" + admin.getPassword() + "'," + admin.isInterlock() + ",'" + admin.getRole() + "')";
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
            String requeteAdmin = "INSERT INTO admin (id) " +
                    "VALUES (" + id + " )";
            st.executeUpdate(requeteAdmin);
            System.out.println("Admin ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void deleteAdmin(int id) throws SQLException {
        String requete = "DELETE FROM global_user   WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,id);
        ps.executeUpdate();
        String requete2 = "DELETE FROM admin   WHERE id = ?";
        PreparedStatement ps2 = cnx.prepareStatement(requete2);
        ps2.setInt(1,id);
        ps2.executeUpdate();

    }

    @Override
    public void updateAdmin(Admin admin) throws SQLException {
        String requete = "UPDATE global_user  SET cin = ?,nom = ?, prenom = ?,genre = ?,dateNaissance = ?,numtel = ?,email = ?,password = ?,interlock = ?,role =? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,admin.getCin());
        ps.setString(2,admin.getNom());
        ps.setString(3,admin.getPrenom());
        ps.setInt(4,admin.getGenre());
        ps.setTimestamp(5, new Timestamp(admin.getDateNaissance().getTime()));
        ps.setInt(6,admin.getNumtel());
        ps.setString(7,admin.getEmail());
        ps.setString(8,admin.getPassword());
        ps.setBoolean(9,admin.isInterlock());
        ps.setString(10,admin.getRole());
        ps.setInt(11, admin.getId());
        ps.executeUpdate();
    }



    @Override
    public List<Admin> getData() {
        List<Admin> data = new ArrayList<>();
        String requete = "SELECT admin.*, global_user.* FROM admin INNER JOIN global_user ON admin.id = global_user.id";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Admin p = new Admin();

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






