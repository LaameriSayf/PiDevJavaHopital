package Controller;

import Model.Medicament;
import Service.MedicamentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherController implements Initializable {
    @FXML
    private ImageView imageId;

    @FXML
    private Text txt1;

    @FXML
    private Text txt2;

    @FXML
    private Text txt3;

    @FXML
    private Text txt4;

    @FXML
    private AnchorPane DetailForm;


    @FXML
    private AnchorPane ListMedicamentForm;

    @FXML
    private GridPane GridMedicament;


    private static final int NUM_COLUMNS = 3;

    private final MedicamentService medicamentService = new MedicamentService();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Medicament> listeMedicaments = medicamentService.getData();

        int row = 0;
        int col = 0;

        // Centrer les éléments dans le GridPane
        GridMedicament.setAlignment(Pos.CENTER);
        GridMedicament.setHgap(10); // Ajouter un espacement horizontal entre les éléments
        GridMedicament.setVgap(10); // Ajouter un espacement vertical entre les éléments

        for (Medicament medicament : listeMedicaments) {
            // Créer une HBox pour contenir l'image et le bouton "Voir plus"
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5); // Ajouter un espacement entre l'image et le bouton
            hbox.setPadding(new Insets(5)); // Ajouter des marges autour de la HBox

            // Créez une ImageView pour afficher l'image
            ImageView imageView = new ImageView(new Image(new File(medicament.getImage()).toURI().toString()));
            imageView.setFitWidth(120); // Ajustez la taille selon vos besoins
            imageView.setFitHeight(120);

            // Créez le bouton "Voir plus"
            Button voirPlusButton = new Button("Voir plus");

            // Configuration de l'action du bouton "Voir plus"
            configureVoirPlusButton(voirPlusButton, medicament);

            // Ajoutez l'ImageView et le bouton "Voir plus" à la HBox
            hbox.getChildren().addAll(imageView, voirPlusButton);

            // Ajoutez la HBox à la grille
            GridMedicament.add(hbox, col, row);

            // Incrémentez les indices de colonne
            col++;

            // Passez à la ligne suivante s'il y a trop d'éléments dans la même ligne
            if (col == NUM_COLUMNS) {
                col = 0;
                row++; // Passez à la ligne suivante
            }
        }
    }

    private void configureVoirPlusButton(Button voirPlusButton, Medicament medicament) {
        voirPlusButton.setOnAction(event -> {
            afficherDetailsMedicament(medicament);
        });
    }

    private void afficherDetailsMedicament(Medicament medicament) {
        // Mettre à jour les éléments visuels avec les détails du médicament
        imageId.setImage(new Image(new File(medicament.getImage()).toURI().toString()));
        txt1.setText(medicament.getNom_med());
        txt3.setText(medicament.getRef_med());
        txt2.setText(medicament.getEtat());
        txt4.setText(medicament.getCategorie().getNom_cat());
        DetailForm.setVisible(true);
        ListMedicamentForm.setVisible(false);
    }

    @FXML
    void RetourBtn(ActionEvent event) {
        DetailForm.setVisible(false);
        ListMedicamentForm.setVisible(true);
    }


}
