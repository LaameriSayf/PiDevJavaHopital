package Services;

import Models.dossiermedical;
import Models.ordonnance;
import Util.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ordonnanceService implements IService <ordonnance>{
    private Connection connection;
    public ordonnanceService(){
        connection = DataBase.getInstance().getCnx();
    }


    @Override
    public dossiermedical afficher(ordonnance ordonnance) throws SQLException {
        return null;
    }

    @Override
    public void ajouter(ordonnance ordonnance) throws SQLException {
        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

        // Préparation de la requête SQL avec des paramètres de substitution
        String requete = "INSERT INTO ordonnance (dateprescription, renouvellement, medecamentprescrit, adresse, dossiermedical_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setTimestamp(1, dateActuelle); // Date actuelle pour la prescription
            statement.setDate(2, new java.sql.Date(ordonnance.getRenouvellement().getTime())); // Date de renouvellement
            statement.setString(3, ordonnance.getMedecamentprescrit());
            statement.setString(4, ordonnance.getAdresse());
            statement.setInt(5, ordonnance.getDossiermedical_id()); // Ajout de l'ID du dossier médical
            statement.setString(6, ordonnance.getDigitalSignature()); // Ajout de la signature numérique


            // Exécution de la requête d'insertion
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Ordonnance ajoutée avec succès !");
            } else {
                System.out.println("Échec de l'ajout de l'ordonnance !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'ordonnance : " + e.getMessage());
        }
    }





    @Override
    public void modifier(ordonnance ordonnance) throws SQLException {
        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

        // Construction de la requête de mise à jour
        StringBuilder requeteBuilder = new StringBuilder("UPDATE ordonnance SET ");
        if (ordonnance.getRenouvellement() != null) {
            requeteBuilder.append("renouvellement=?, ");
        }
        if (ordonnance.getMedecamentprescrit() != null) {
            requeteBuilder.append("medecamentprescrit=?, ");
        }
        if (ordonnance.getAdresse() != null) {
            requeteBuilder.append("adresse=?, ");
        }
        // Supprimer la virgule finale
        requeteBuilder.deleteCharAt(requeteBuilder.length() - 2);
        requeteBuilder.append(" WHERE id=?"); // Supposons que vous avez un champ id dans ordonnance

        String requete = requeteBuilder.toString();

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            int index = 1; // Indice de paramètre

            // Attribution des valeurs aux paramètres de la requête
            if (ordonnance.getRenouvellement() != null) {
                statement.setDate(index++, new java.sql.Date(ordonnance.getRenouvellement().getTime()));
            }
            if (ordonnance.getMedecamentprescrit() != null) {
                statement.setString(index++, ordonnance.getMedecamentprescrit());
            }
            if (ordonnance.getAdresse() != null) {
                statement.setString(index++, ordonnance.getAdresse());
            }
            statement.setInt(index, ordonnance.getId()); // Remplacez id par le champ approprié

            // Exécution de la requête de mise à jour
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Ordonnance modifiée avec succès !");
            } else {
                System.out.println("Échec de la modification de l'ordonnance !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'ordonnance : " + e.getMessage());
        }


    }





    @Override
    public void supprimer(int ordonnanceId) throws SQLException {
        String requete = "DELETE FROM ordonnance WHERE id = ?";

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution de la valeur à paramètre de la requête
            statement.setInt(1, ordonnanceId);
            // Exécution de la requête de suppression
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Ordonnance supprimée avec succès !");
            } else {
                System.out.println("Aucune ordonnance trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'ordonnance : " + e.getMessage());
        }

    }

    @Override
    public List<ordonnance> getData() throws SQLException {
        List<ordonnance> ordonnances = new ArrayList<>();

        // Requête SQL pour sélectionner toutes les ordonnances
        String requete = "SELECT * FROM ordonnance";

        try (Connection connection = new DataBase().getCnx();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(requete)) {

            // Parcours des résultats
            while (resultSet.next()) {
                ordonnance o = new ordonnance();
                o.setId(resultSet.getInt("id"));
                o.setDateprescription(resultSet.getDate("dateprescription"));
                o.setRenouvellement(resultSet.getDate("renouvellement"));
                o.setMedecamentprescrit(resultSet.getString("medecamentprescrit"));
                o.setAdresse(resultSet.getString("adresse"));

                ordonnances.add(o); // Ajout de l'ordonnance à la liste
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des ordonnances : " + e.getMessage());
        }

        return ordonnances;
    }


}
