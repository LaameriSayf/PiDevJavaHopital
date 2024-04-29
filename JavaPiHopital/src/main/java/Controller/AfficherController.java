package Controller;

import Model.Medicament;
import Service.MedicamentService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherController implements Initializable {

    @FXML
    private ListView<Medicament> frontWost;

    private final MedicamentService medicamentService = new MedicamentService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Medicament> medicaments = medicamentService.getData();
        frontWost.getItems().addAll(medicaments);
        frontWost.setCellFactory(new Callback<ListView<Medicament>, ListCell<Medicament>>() {
            @Override
            public ListCell<Medicament> call(ListView<Medicament> param) {
                return new ListCell<Medicament>() {
                    @Override
                    protected void updateItem(Medicament medicament, boolean empty) {
                        super.updateItem(medicament, empty);

                        if (empty || medicament == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            int index = getIndex();
                            if (index % 3 == 0 && index + 2 < frontWost.getItems().size()) { // Nouvelle ligne avec au moins 3 éléments restants
                                HBox hbox = new HBox();
                                hbox.setSpacing(20); // Espacement entre les éléments
                                hbox.setStyle("-fx-padding: 10;" + // Espacement intérieur
                                        "-fx-border-style: solid inside;" +
                                        "-fx-border-width: 1;" +
                                        "-fx-border-insets: 5;" +
                                        "-fx-border-radius: 5;" +
                                        "-fx-border-color: black;"); // Style du cadre

                                for (int i = 0; i < 3; i++) {
                                    Medicament currentMedicament = frontWost.getItems().get(index + i);
                                    VBox vbox = createVBox(currentMedicament);
                                    hbox.getChildren().add(vbox);
                                }
                                setGraphic(hbox);
                            } else {
                                // Si ce n'est pas le début d'une nouvelle ligne ou si le nombre d'éléments restants est insuffisant, ne rien afficher
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
    }

    private VBox createVBox(Medicament medicament) {
        VBox vbox = new VBox();
        vbox.setSpacing(5); // Espacement entre les éléments
        vbox.setAlignment(Pos.CENTER); // Centrage vertical
        vbox.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))); // Ajout du cadre

        ImageView imageView = createImageView(medicament);
        Text nameText = new Text(medicament.getNom_med());
        nameText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12)); // Police Times New Roman, gras, taille 12
        nameText.setTextAlignment(TextAlignment.CENTER); // Centrage horizontal du nom
        Text referenceText = new Text(medicament.getRef_med());
        referenceText.setFont(Font.font("Times New Roman", 12)); // Police Times New Roman, taille 12
        referenceText.setTextAlignment(TextAlignment.CENTER); // Centrage horizontal de la référence

        vbox.getChildren().addAll(imageView, nameText, referenceText);

        return vbox;
    }

    private ImageView createImageView(Medicament medicament) {
        String imagePath = medicament.getImage(); // Supposons que vous avez un chemin d'image absolu ici
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        if (imagePath != null) {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }

        return imageView;
    }
}
