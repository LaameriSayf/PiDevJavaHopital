package Service;

import Model.RendezVous;
import Util.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Time.valueOf;

public class RendezVousService implements RdvService {
    private final Connection connection;


    public RendezVousService (Connection connection) {
        this.connection = MyDataBase.getInstance().getConnection();}


    @Override
    public void ajouterRDV(RendezVous rendezVous) throws SQLException {
        String sql = "INSERT INTO rendezvous (daterendezvous, heurerendezvous, description, file) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            LocalDate date = rendezVous.getDaterdv();
            LocalTime heure = rendezVous.getHeurerdv();

            // Formater l'heure en tant que chaîne au format "HH:mm:ss"
            String heureFormatted = heure.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // Paramètres de la requête
            ps.setDate(1, Date.valueOf(date)); // Date
            ps.setString(2, heureFormatted); // Heure formatée
            ps.setString(3, rendezVous.getDescription());
            ps.setString(4, rendezVous.getFile());

            ps.executeUpdate();
        }
    }







    public RendezVous selectRDV(int id){
        String sql = "SELECT * FROM rendezvous where id="+id;
        RendezVous rendezVous = new RendezVous();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {

                rendezVous.setId(resultSet.getInt("id"));
                rendezVous.setDescription(resultSet.getString("description"));
                rendezVous.setFile(resultSet.getString("file"));
                rendezVous.setDaterdv(resultSet.getDate("daterendezvous").toLocalDate());
                rendezVous.setHeurerdv(resultSet.getTime("heurerendezvous").toLocalTime());
            }else  return  null;

        } catch (SQLException e) {
            e.printStackTrace();
        }return rendezVous;
    }

    @Override
    public void modifier(RendezVous rendezVous, int id) throws SQLException {
        String sql = "update rendezvous set daterendezvous = ?, heure = ?, description = ?, file = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setDate(1, Date.valueOf(rendezVous.getDaterdv()));
        ps.setTime(2, valueOf(rendezVous.getHeurerdv()));
        ps.setString(3, rendezVous.getDescription());
        ps.setString(4, rendezVous.getFile());
        ps.setInt(5, id);

        ps.executeUpdate();


    }


    @Override
    public void supprimer(int id) throws SQLException {
        String requete= "DELETE FROM rendezvous WHERE id = ?";
        try {
            PreparedStatement pst= connection.prepareStatement(requete);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("valeur supprimer");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<RendezVous> recuperer() {
        String sql = "SELECT * FROM rendezvous";
        List<RendezVous> rendezVousList = new ArrayList<>();

        try
        {
        Connection connection = MyDataBase.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {

                RendezVous rendezVous = new RendezVous();
                rendezVous.setId(resultSet.getInt("id"));
                rendezVous.setDescription(resultSet.getString("description"));
                rendezVous.setFile(resultSet.getString("file"));
                rendezVous.setDaterdv(resultSet.getDate("daterendezvous").toLocalDate());
                rendezVous.setHeurerdv(resultSet.getTime("heurerendezvous").toLocalTime());
                rendezVousList.add(rendezVous);

            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception or handle it appropriately
        }

        return rendezVousList;
    }
    public int nombreTotalRendezVous() {
        String sql = "SELECT COUNT(*) AS total FROM rendezvous";
        int totalRendezVous = 0;

        try {
            Connection connection = MyDataBase.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                totalRendezVous = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRendezVous;
    }
    // Méthode pour récupérer le nombre de rendez-vous ayant dépassé la date d'aujourd'hui
    public int nombreRendezVousDepasseDateAujourdhui() {
        int totalRendezVousDepasse = 0;

        // La requête SQL pour obtenir le nombre de rendez-vous dépassés
        String sql = "SELECT COUNT(*) AS total FROM rendezvous WHERE daterendezvous < CURDATE()";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Si le résultat contient des lignes
            if (resultSet.next()) {
                totalRendezVousDepasse = resultSet.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'exécution de la requête SQL.");
        }

        return totalRendezVousDepasse;
    }



}

