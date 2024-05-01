package services;

import entities.Emploi;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpService implements EmploiService {

    private final Connection connection;

    public EmpService(Connection connection) {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouterEmploi(Emploi E) throws SQLException {
        String sql = "INSERT INTO emploi (titre, start, end, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Timestamp start = Timestamp.valueOf(E.getStart().atStartOfDay());
            Timestamp end = Timestamp.valueOf(E.getEnd().atStartOfDay());
            ps.setString(1, E.getTitre());
            ps.setTimestamp(2, start);
            ps.setTimestamp(3, end);
            ps.setString(4, E.getDescription());
            ps.executeUpdate();
        }

    }

    @Override
    public void modifierEmploi(Emploi E, int id) throws SQLException {
        String sql = "update emploi set titre = ?, start = ?, end = ?, description = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, E.getTitre());
        ps.setDate(2, Date.valueOf(E.getStart()));
        ps.setDate(3, Date.valueOf(E.getEnd()));
        ps.setString(4, E.getDescription());
        ps.setInt(5, id);
    }

    @Override
    public void supprimerEmploi(int id) throws SQLException {
        String requete = "DELETE FROM emploi WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("valeur supprimer");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Emploi> recuperer() throws SQLException {
        String sql = "SELECT * FROM emploi";
        List<Emploi> emploiList = new ArrayList<>();
        try {

            Connection connection = MyDataBase.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Emploi emploi = new Emploi();
                emploi.setId(resultSet.getInt("id"));
                emploi.setTitre(resultSet.getString("titre"));
                emploi.setStart(resultSet.getDate("start").toLocalDate());
                emploi.setEnd(resultSet.getDate("end").toLocalDate());
                emploi.setDescription(resultSet.getString("description"));
                emploiList.add(emploi);
                System.out.println(emploi.getId());

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return emploiList;
    }

    public Emploi selectEmploi(int id) {
        String sql = "SELECT * FROM emploi where id=" + id;
        Emploi E = new Emploi();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                E.setId(resultSet.getInt("id"));
              E.setTitre(resultSet.getString("titre"));
              E.setStart(resultSet.getDate("start").toLocalDate());
              E.setEnd(resultSet.getDate("end").toLocalDate());
              E.setDescription(resultSet.getString("description"));


            } else return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return E;
    }
}
