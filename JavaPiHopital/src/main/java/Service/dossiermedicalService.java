package Service;

import Model.dossiermedical;
import Util.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dossiermedicalService implements IService5<dossiermedical> {
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
                    //dossier.setId(resultSet.getInt("id"));
                    dossier.setNumdossier(resultSet.getInt("numdossier"));
                    dossier.setResultatexamen(resultSet.getString("resultatexamen"));
                    dossier.setDate_creation(resultSet.getDate("date_creation"));
                    dossier.setAntecedentspersonelles(resultSet.getString("antecedentspersonnelles"));
                    dossier.setImage(resultSet.getString("image"));
                    //dossier.setPatient_id(resultSet.getInt("patient_id"));
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
        String requete = "INSERT INTO dossiermedical(date_creation,resultatexamen,antecedentspersonnelles,image,numdossier) " +
                "VALUES (?, ?, ?, ?,?)";

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setTimestamp(1, dateActuelle); // Date actuelle pour la date de creation
            statement.setString(2, dossiermedical.getResultatexamen());
            statement.setString(3, dossiermedical.getAntecedentspersonelles());
            statement.setString(4, dossiermedical.getImage());
            //statement.setInt(5, 1);
            statement.setInt(5,dossiermedical.getNumdossier() );

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
        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

        // Préparation de la requête SQL pour obtenir le nom et prénom du patient
      String requetePatient = "SELECT nom, prenom FROM global_user WHERE id = ?";
        String nomPatient = "";
        String prenomPatient = "";

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statementPatient = connection.prepareStatement(requetePatient)) {
            // Attribution de l'ID du patient à la requête
            statementPatient.setInt(1, 1); // Vous avez mentionné que vous voulez récupérer les informations pour l'ID 1

            // Exécution de la requête pour obtenir les informations du patient
            ResultSet rs = statementPatient.executeQuery();
            if (rs.next()) {
                nomPatient = rs.getString("nom");
                prenomPatient = rs.getString("prenom");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des informations du patient: " + e.getMessage());
        }

        // Préparation de la requête SQL avec des paramètres de substitution
        String requete = "INSERT INTO dossiermedical(date_creation,resultatexamen,antecedentspersonnelles,image,patient_id,nom_patient,prenom_patient,numdossier) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = new DataBase().getCnx();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setTimestamp(1, dateActuelle); // Date actuelle pour la date de creation
            statement.setString(2, dossiermedical.getResultatexamen());
            statement.setString(3, dossiermedical.getAntecedentspersonelles());
            statement.setString(4, dossiermedical.getImage());
            statement.setInt(5, 1); // ID du patient
            statement.setInt(8, dossiermedical.getNumdossier()); // ID du patient
            statement.setString(6, nomPatient); // Nom du patient
            statement.setString(7, prenomPatient); // Prénom du patient

            // Exécution de la requête d'insertion
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Dossier ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du dossier medical !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du dossier medical: " + e.getMessage());
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
                dossier.setNumdossier(resultSet.getInt("numdossier"));
                dossier.setResultatexamen(resultSet.getString("resultatexamen"));
                dossier.setDate_creation(resultSet.getDate("date_creation"));
                dossier.setAntecedentspersonelles(resultSet.getString("antecedentspersonnelles"));
                dossier.setImage(resultSet.getString("image"));
                dossier.setPatient_id(resultSet.getInt("patient_id"));

                dossiers.add(dossier); // Ajout du dossier médical à la liste
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des dossiers médicaux : " + e.getMessage());
        }

        return dossiers;

    }

    public dossiermedical rechercherParNumDossier(int numDossier) throws SQLException {
        dossiermedical dossier = null;

        // Requête SQL pour sélectionner un dossier médical par numéro de dossier
        String requete = "SELECT * FROM dossiermedical WHERE numdossier = ?";

        try (PreparedStatement statement = connection.prepareStatement(requete)) {
            statement.setInt(1, numDossier); // Ajout du paramètre numéro de dossier

            try (ResultSet resultSet = statement.executeQuery()) {
                // Vérification s'il y a un résultat
                if (resultSet.next()) {
                    dossier = new dossiermedical();
                    dossier.setId(resultSet.getInt("id"));
                    dossier.setNumdossier(resultSet.getInt("numdossier"));
                    dossier.setResultatexamen(resultSet.getString("resultatexamen"));
                    dossier.setDate_creation(resultSet.getDate("date_creation"));
                    dossier.setAntecedentspersonelles(resultSet.getString("antecedentspersonnelles"));
                    dossier.setImage(resultSet.getString("image"));
                    dossier.setPatient_id(resultSet.getInt("patient_id"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du dossier médical par numéro : " + e.getMessage());
        }

        return dossier;
    }



}
