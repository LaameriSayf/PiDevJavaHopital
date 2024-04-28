package Controllers;

import Models.dossiermedical;
import Services.dossiermedicalService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class showDetailsDossier implements Initializable {
    private dossiermedical selectedDossier;
    private final dossiermedicalService dossierService = new dossiermedicalService();

    @FXML
    private Label idLabel;

    @FXML
    private Label resultatExamenLabel;

    @FXML
    private Label dateCreationLabel;

    @FXML
    private Label antecedentsLabel;

    @FXML
    private Label imagePathLabel;

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initializeDossier(int dossierId) {
        try {
            dossiermedical dossier = new dossiermedical();
            dossier.setId(dossierId);
            this.selectedDossier = dossierService.afficher(dossier);

            if (selectedDossier != null) {
                updateDetailsLabels();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur
        }
    }

    private void updateDetailsLabels() {
        if (selectedDossier != null) {
            idLabel.setText("ID: " + selectedDossier.getId());
            resultatExamenLabel.setText("Résultat Examen: " + selectedDossier.getResultatexamen());
            dateCreationLabel.setText("Date de Création: " + selectedDossier.getDate_creation());
            antecedentsLabel.setText("Antécédents Personnels: " + selectedDossier.getAntecedentspersonelles());
            imagePathLabel.setText("Chemin de l'image: " + selectedDossier.getImage());

            afficherImage(selectedDossier.getImage());
        } else {
            idLabel.setText("ID: N/A");
            resultatExamenLabel.setText("Résultat Examen: N/A");
            dateCreationLabel.setText("Date de Création: N/A");
            antecedentsLabel.setText("Antécédents Personnels: N/A");
            imagePathLabel.setText("Chemin de l'image: N/A");
            imageView.setImage(null);
            System.out.println("Aucun dossier sélectionné !");
        }
    }

    private void afficherImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image(imagePath);
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            imageView.setImage(null);
            System.out.println("Chemin d'image invalide !");
        }
    }
}

