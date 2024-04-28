package Test;

import Models.ordonnance;
import Services.ordonnanceService;
import Util.DataBase;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataBase mc = new DataBase();

        // Créer une instance d'ordonnance avec des valeurs appropriées
        ordonnance o = new ordonnance();
        o.setDateprescription(new java.util.Date()); // ou utilisez la date appropriée

        // Saisir ou attribuer la date de renouvellement selon les besoins
        // Exemple de saisie de date :
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez saisir la date de renouvellement (format : dd-MM-yyyy) : ");
        String dateString = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date renouvellementDate = dateFormat.parse(dateString);
            o.setRenouvellement(renouvellementDate);
        } catch (ParseException e) {
            System.out.println("Format de date invalide. Assurez-vous d'utiliser le format dd-MM-yyyy.");
            return;
        }

        System.out.println("Veuillez saisir le médicament prescrit : ");
        String medicamentPrescrit = scanner.nextLine();
        o.setMedecamentprescrit(medicamentPrescrit);

        System.out.println("Veuillez saisir l'adresse : ");
        String adresse = scanner.nextLine();
        o.setAdresse(adresse);

        // Créer une instance de ordonnanceService
        ordonnanceService ps = new ordonnanceService();

        // Ajouter l'ordonnance à la base de données
        ps.ajouter(o);
        o.setId(12);
        // Saisir ou attribuer les nouvelles valeurs à modifier
        // Exemple : Modifier le médicament prescrit
        System.out.println("Veuillez saisir le nouveau médicament prescrit : ");
        String nouveauMedicament = scanner.nextLine();
        o.setMedecamentprescrit(nouveauMedicament);

        // Modifier l'ordonnance dans la base de données
        ps.modifier(o);
        // Supprimer l'ordonnance avec l'ID 1 (remplacez par l'ID réel)
        int ordonnanceIdASupprimer = 12; // Remplacez 1 par l'ID de l'ordonnance à supprimer
        ps.supprimer(ordonnanceIdASupprimer);
    }



}
