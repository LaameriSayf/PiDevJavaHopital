package services;
import entities.RendezVous;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendezVousService implements RdvService {
    private final Connection connection;


    public RendezVousService (Connection connection) {
        this.connection = MyDataBase.getInstance().getConnection();}


    @Override
    public void ajouterRDV(RendezVous rendezVous) throws SQLException {
        String sql = "INSERT INTO rendezvous (daterendezvous, heurerendezvous, description, file) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Convert LocalDate to Timestamp for DATETIME column
            Timestamp timestamp = Timestamp.valueOf(rendezVous.getDaterdv().atStartOfDay());

            ps.setTimestamp(1, timestamp);
            ps.setString(2, rendezVous.getHeurerdv());
            ps.setString(3, rendezVous.getDescription());
            ps.setString(4, rendezVous.getFile());

            ps.executeUpdate();
        }
    }



    @Override
    public void modifier(RendezVous rendezVous, int id) throws SQLException {
        String sql = "update rendezvous set daterendezvous = ?, heurerendezvous = ?, description = ?, file = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setDate(1, Date.valueOf(rendezVous.getDaterdv()));
        ps.setString(2, rendezVous.getHeurerdv());
        ps.setString(3, rendezVous.getDescription());
        ps.setString(4, rendezVous.getFile());
        ps.setInt(5, id);

        int rowsAffected = ps.executeUpdate();
        System.out.println(rowsAffected + " row(s) updated.");
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
                rendezVous.setHeurerdv(resultSet.getString("heurerendezvous"));
                rendezVousList.add(rendezVous);
                /*
                System.out.println(rendezVous.getDescription());
                //*/
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception or handle it appropriately
        }

        return rendezVousList;
    }

}

