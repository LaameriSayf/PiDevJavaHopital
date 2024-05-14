package Service;

import Model.Global_user;
import Util.DataBase;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;


public class Global_userService implements IService<Global_user> {
    private Connection cnx;

    public Global_userService() {
        cnx = DataBase.getInstance().getCnx();
    }


    @Override
    public void addGlobal_user(Global_user globalUser) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNaissanceFormatted = sdf.format(globalUser.getDateNaissance());

        String requete = "INSERT INTO global_user (cin,nom, prenom,genre,dateNaissance,numtel,email,password,interlock,role)" +
                "VALUES (" + globalUser.getCin() + ",'" + globalUser.getNom() + "','" + globalUser.getPrenom() + "','" + globalUser.getGenre() + "','" + dateNaissanceFormatted + "'," + globalUser.getNumtel() + ",'" + globalUser.getEmail() + "','" + globalUser.getPassword() + "'," + globalUser.isInterlock() + ",'" + globalUser.getRole() + "')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(requete);
            System.out.println("Personne ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteGlobal_user(int id) throws SQLException {
        String requete = "DELETE FROM global_user WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1, id);
        ps.executeUpdate();


    }

    @Override
    public void updateGlobal_user(Global_user globalUser) throws SQLException {
        String requete = "UPDATE global_user  SET cin = ?,nom = ?, prenom = ?,genre = ?,dateNaissance = ?,numtel = ?,email = ?,password = ?,interlock = ?,role =? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1, globalUser.getCin());
        ps.setString(2, globalUser.getNom());
        ps.setString(3, globalUser.getPrenom());
        ps.setInt(4, globalUser.getGenre());
        ps.setTimestamp(5, new Timestamp(globalUser.getDateNaissance().getTime()));
        ps.setInt(6, globalUser.getNumtel());
        ps.setString(7, globalUser.getEmail());
        ps.setString(8, globalUser.getPassword());
        ps.setBoolean(9, globalUser.isInterlock());
        ps.setString(10, globalUser.getRole());
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

            while (rs.next()) {
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

    /*public static Global_user getUtilisateurByEmail(String email) {
        Global_user user = null;

        // Préparez votre requête SQL pour rechercher un utilisateur par son e-mail
        String req = "SELECT * FROM global_user WHERE Email = ?";

        try {
            // Assurez-vous que la connexion est valide et non fermée
            //if (cnx != null && !cnx.isClosed()) {
                try (Statement preparedStatement = cnx.createStatement()) {
                    preparedStatement.setString(1, email);
                    ResultSet rs = st.executeQuery(requete);

                    try (ResultSet rs = preparedStatement.executeQuery()) {
                        if (rs.next()) {
                            user = mapResultSetToUtilisateur(rs);
                        }
                    }
                }
            //} else {
                // Lancez une exception si la connexion n'est pas établie
                //throw new SQLException("La connexion à la base de données n'est pas établie. Vérifiez la configuration de la connexion.");
            //}
        } catch (SQLException ex) {
            // Gérez les exceptions SQL et lancez une exception d'exécution plus informative
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur par e-mail. Détails : " + ex.getMessage(), ex);
        }

        return user;
    }*/

    public Global_user getEventByEmail(String Useremail) {
        Global_user p = null;
        // SQL query to retrieve the event by name
        String sql = "SELECT * FROM global_user WHERE email = ?";

        // Try-with-resources block to handle database resources
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            // Set the event name parameter in the prepared statement
            preparedStatement.setString(1, Useremail);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            // Check if an event is found
            if (rs.next()) {
                // Create a new Event object
                p = new Global_user();
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

                //user.add(p);

            }

        } catch (SQLException e) {
            // Handle SQL exceptions and print stack trace for debugging
            e.printStackTrace();
        }

        // Return the found Event object, or null if no event was found
        return p;
    }
    }
