package Service;
import Model.Patient;
import Util.DataBase;

import java.sql.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PatientService implements IService3<Patient> {
    private Connection cnx;

    public PatientService() {
        cnx = DataBase.getInstance().getCnx();
    }

    @Override
    public void addPatient(Patient patient) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNaissanceFormatted = sdf.format(patient.getDateNaissance());

        // Insertion dans la table global_user
        String requeteGlobalUser = "INSERT INTO global_user (cin, nom, prenom, genre, dateNaissance, numtel, email, password, interlock, role) " +
                "VALUES (" + patient.getCin() + ",'" + patient.getNom() + "','" + patient.getPrenom() + "','" + patient.getGenre() + "','" + dateNaissanceFormatted + "'," + patient.getNumtel() + ",'" + patient.getEmail() + "','" + patient.getPassword() + "'," + patient.isInterlock() + ",'" + patient.getRole() + "')";
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
            String requetePatient = "INSERT INTO patient (id, numcarte) " +
                    "VALUES (" + id + " ,'"+ patient.getNumcarte()+"')";
            st.executeUpdate(requetePatient);
            System.out.println("Patient ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deletePatient(int id) throws SQLException {
        String requete = "DELETE FROM global_user   WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,id);
        ps.executeUpdate();
        String requete2 = "DELETE FROM patient  WHERE id = ?";
        PreparedStatement psm2 = cnx.prepareStatement(requete2);
        psm2.setInt(1,id);
        psm2.executeUpdate();

    }

    @Override
    public void updatePatient(Patient patient) throws SQLException {
        String requete = "UPDATE global_user  SET cin = ?,nom = ?, prenom = ?,genre = ?,dateNaissance = ?,numtel = ?,email = ?,password = ?,interlock = ?,role =? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(requete);
        ps.setInt(1,patient.getCin());
        ps.setString(2,patient.getNom());
        ps.setString(3,patient.getPrenom());
        ps.setInt(4,patient.getGenre());
        ps.setTimestamp(5, new Timestamp(patient.getDateNaissance().getTime()));
        ps.setInt(6,patient.getNumtel());
        ps.setString(7,patient.getEmail());
        ps.setString(8,patient.getPassword());
        ps.setBoolean(9,patient.isInterlock());
        ps.setString(10,patient.getRole());
        ps.setInt(11, patient.getId());
        String requetePatient = "UPDATE patient SET numcarte = ? WHERE id = ?";
        PreparedStatement psPatient = cnx.prepareStatement(requetePatient);
        psPatient.setInt(1, patient.getNumcarte());
        psPatient.setInt(2, patient.getId());
        ps.executeUpdate();
        psPatient.executeUpdate();

    }

    @Override
    public List<Patient> getData() {

        List<Patient> data = new ArrayList<>();
        String requete = "SELECT patient.*, global_user.* FROM patient INNER JOIN global_user ON patient.id = global_user.id";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Patient p = new Patient();

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
                p.setNumcarte(rs.getInt("numcarte"));

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
