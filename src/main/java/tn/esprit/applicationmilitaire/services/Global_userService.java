package tn.esprit.applicationmilitaire.services;

import tn.esprit.applicationmilitaire.models.Global_user;
import tn.esprit.applicationmilitaire.utils.MyConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;


public class Global_userService implements IService<Global_user> {
     private Connection cnx;
     public Global_userService(){
         cnx = MyConnection.getInstance().getCnx();
     }


    @Override
    public void addGlobal_user(Global_user globalUser) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNaissanceFormatted = sdf.format(globalUser.getDateNaissance());

        String requete = "INSERT INTO global_user (cin,nom, prenom,genre,dateNaissance,numtel,email,password,interlock,role)" +
                "VALUES ("+globalUser.getCin()+",'"+globalUser.getNom()+"','"+globalUser.getPrenom()+"','"+globalUser.getGenre()+"','"+dateNaissanceFormatted+"',"+globalUser.getNumtel()+",'"+globalUser.getEmail()+"','"+globalUser.getPassword()+"',"+globalUser.isInterlock()+",'"+globalUser.getRole()+"')";
        try {
            Statement st =cnx.createStatement();
            st.executeUpdate(requete);
            System.out.println("Personne ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteGlobal_user(int id) throws SQLException  {
        String requete = "DELETE FROM global_user WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,id);
        ps.executeUpdate();



    }

    @Override
    public void updateGlobal_user(Global_user globalUser) throws SQLException {
        String requete = "UPDATE global_user  SET cin = ?,nom = ?, prenom = ?,genre = ?,dateNaissance = ?,numtel = ?,email = ?,password = ?,interlock = ?,role =? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,globalUser.getCin());
        ps.setString(2,globalUser.getNom());
        ps.setString(3,globalUser.getPrenom());
        ps.setInt(4,globalUser.getGenre());
        ps.setTimestamp(5, new Timestamp(globalUser.getDateNaissance().getTime()));
        ps.setInt(6,globalUser.getNumtel());
        ps.setString(7,globalUser.getEmail());
        ps.setString(8,globalUser.getPassword());
        ps.setBoolean(9,globalUser.isInterlock());
        ps.setString(10,globalUser.getRole());
        ps.setInt(11, globalUser.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Global_user> getData() {
        List<Global_user> data = new ArrayList<>();
        String requete = "SELECT * FROM global_user";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while(rs.next()){
                Global_user p = new Global_user();
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

                data.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

         return data;
    }
}
