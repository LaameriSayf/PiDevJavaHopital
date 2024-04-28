package Services;

import Models.dossiermedical;
import Util.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dossiermedicalService implements IService <dossiermedical> {
    private Connection connection;
    public dossiermedicalService(){
        connection = DataBase.getInstance().getCnx();
    }

    @Override
    public dossiermedical afficher(dossiermedical dossiermedical) throws SQLException {
        dossiermedical dossier = null;

        // Requête SQL pour sélectionner un dossier médical par ID
        String requete = "SELECT * FROM dossiermedical WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(requete)) {
            statement.setInt(1, dossiermedical.getId()); // Ajout du paramètre ID

            try (ResultSet resultSet = statement.executeQuery()) {
                // Vérification s'il y a un résultat
                if (resultSet.next()) {
                    dossier = new dossiermedical();
                    dossier.setId(resultSet.getInt("id"));
                    dossier.setResultatexamen(resultSet.getString("resultatexamen"));
                    dossier.setDate_creation(resultSet.getDate("date_creation"));
                    dossier.setAntecedentspersonelles(resultSet.getString("antecedentspersonelles"));
                    dossier.setImage(resultSet.getString("image"));
                    dossier.setPatient_id(resultSet.getInt("patient_id"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des détails du dossier médical : " + e.getMessage());
        }

        return dossier;
    }



    @Override
    public void ajouter(dossiermedical dossiermedical) throws SQLException {
        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

        // Préparation de la requête SQL avec des paramètres de substitution
        String requete = "INSERT INTO dossiermedical(date_creation,resultatexamen,antecedentspersonelles ,image) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setTimestamp(1, dateActuelle); // Date actuelle pour la date de creation
            statement.setString(2, dossiermedical.getResultatexamen());
            statement.setString(3, dossiermedical.getAntecedentspersonelles());
            statement.setString(4, dossiermedical.getImage());

            // Exécution de la requête d'insertion
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Dossier ajoutée avec succès !");
            } else {
                System.out.println("Échec de l'ajout du dossier medical !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du dossier medical: " + e.getMessage());
        }


    }
    @Override
    public void modifier(dossiermedical dossiermedical) throws SQLException {
        // Préparation de la requête SQL avec des paramètres de substitution
        String requete = "UPDATE dossiermedical SET resultatexamen=?, antecedentspersonelles=?, image=? WHERE id=?";

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setString(1, dossiermedical.getResultatexamen());
            statement.setString(2, dossiermedical.getAntecedentspersonelles());
            statement.setString(3, dossiermedical.getImage());
            statement.setInt(4, dossiermedical.getId()); // Utilisation de l'ID pour identifier le dossier à modifier

            // Exécution de la requête de mise à jour
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Dossier modifié avec succès !");
            } else {
                System.out.println("Échec de la modification du dossier médical !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du dossier médical: " + e.getMessage());
        }
    }



    @Override
    public void supprimer(int dossierId) throws SQLException {

    }

    @Override
    public List<dossiermedical> getData() throws SQLException {
        List<dossiermedical> dossiers = new ArrayList<>();

        // Requête SQL pour sélectionner tous les dossiers médicaux
        String requete = "SELECT * FROM dossiermedical";

        try (Connection connection = new DataBase().getCnx();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(requete)) {

            // Parcours des résultats
            while (resultSet.next()) {
                dossiermedical dossier = new dossiermedical();
                dossier.setId(resultSet.getInt("id"));
                dossier.setResultatexamen(resultSet.getString("resultatexamen"));
                dossier.setDate_creation(resultSet.getDate("date_creation"));
                dossier.setAntecedentspersonelles(resultSet.getString("antecedentspersonelles"));
                dossier.setImage(resultSet.getString("image"));
                dossier.setPatient_id(resultSet.getInt("patient_id"));

                dossiers.add(dossier); // Ajout du dossier médical à la liste
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des dossiers médicaux : " + e.getMessage());
        }

        return dossiers;

    }

}
