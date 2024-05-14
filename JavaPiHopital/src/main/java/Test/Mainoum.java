package Test;
import Service.*;
import Util.DataBase;

import java.sql.Timestamp;

public class Mainoum {
    public static void main(String[] args) {
        Global_userService ps = new Global_userService();
        AdminService p = new AdminService();
        MedecinService psm = new MedecinService();
        PatientService psp = new PatientService();
        PharmacienService psph = new PharmacienService();


        Timestamp dateNaissance = Timestamp.valueOf("2000-06-24 00:00:00");

        //ps.addGlobal_user(new Global_user(1231456, "maroui", "yassine", 1, "yassine.maroui@esprit.tn", "yassine", "https//jhgfdsqdcfvgbhnj.jpg", "admin", dateNaissance, true, 56640965));
        //System.out.println(ps.getData());
        //p.addAdmin(new Admin(123133324, "benhaj", "oumaima", 1, "oumaima.benhaj@esprit.tn", "yassine", "https//jhgfdsqdcfvgbhnj.jpg", "admin", dateNaissance, true, 56640965));
        /*****************************************ajout d'un medecin***************************************/
        //psm.addMedecin(new Medecin(123133341, "benhaj", "oumaima", 1, "oumaima.benhaj@esprit.tn", "yassine", "https//jhgfdsqdcfvgbhnj.jpg", "admin", dateNaissance, true, 56640965, "homme", 0));
        /*********************************************Ajout d'un patient************************************/
        /*********************************************Ajout d'un pharmacien************************************/
        //psph.addPharmacien(new Pharmacien( 12333542, "ahmed", "benhmida", 1, "ahmed.benhmida@esprit.tn", "ahmed", "https//jhgfdsqdcfvgbhnj.jpg", "pharmacien", dateNaissance, true, 95436478, 0));
        /******************** suppression d'un administrateur**********************************************/
       /*try {
            // Call the deleteAdmin function with the desired ID
            p.deleteAdmin(10);  // Replace 1 with the actual ID you want to delete
            System.out.println("User with ID 1 deleted successfully!");  // Confirmation message
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        */
        /*************************************suppression d'un medecin********************************/
        /*try {
            // Call the deleteAdmin function with the desired ID
            psm.deleteMedecin(13);  // Replace 1 with the actual ID you want to delete
            System.out.println("User with ID 1 deleted successfully!");  // Confirmation message
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }*/
        /************************************modification d'un medecin**********************************************************/
        /*try {
            psm.updateMedecin(new Medecin(14,13412588,56640965, 1, "yassinee", "oumaima", "yassine.benhaj@esprit.tn", "yassineoumaima", "https//jhgfdsqdcfvgbhnj.jpg","medecin",dateNaissance, true, "medecin géneraliste", 0));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
        /********************************suppression d'un patient************************************/
        /*try {
            // Call the deleteAdmin function with the desired ID
            psp.deletePatient(17);  // Replace 1 with the actual ID you want to delete
            System.out.println("User with ID 1 deleted successfully!");  // Confirmation message
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }*/
        /***************************************modification d'un patient*****************************/
        /*try {
            psp.updatePatient(new Patient(18,123112222, 56417542, 1, "oumaima", "hanine ", "oumaima.hanine@esprit.tn", "hanineoumaima", "https//jhgfdsqdcfvgbhnj.jpg", "administrateur", dateNaissance, true, 184115));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
        /***************************************suppression d'un pharmacien************************************/
        /*try {
            // Call the deleteAdmin function with the desired ID
            psph.deletePharmacien(28);  // Replace 1 with the actual ID you want to delete
            System.out.println("User with ID 1 deleted successfully!");  // Confirmation message
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }*/
        /*try {
           psph.updatePharmacien(new Pharmacien( 29,1222542, 96235547, 1, "ahmed", "benhmida", "ahmed.benhmida@esprit.tn", "ahmed", "https//jhgfdsqdcfvgbhnj.jpg", "pharmacien", dateNaissance, false, 0));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }*/

        System.out.println(psph.getData());



        DataBase mc = DataBase.getInstance();
        DataBase mc2 = DataBase.getInstance();
        System.out.println(mc);
        System.out.println(mc2);
}
}
