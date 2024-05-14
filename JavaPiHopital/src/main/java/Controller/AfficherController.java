package Controller;

import Model.Medicament;
import Service.MedicamentService;
import Test.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @FXML
    private ImageView btnchatboltt;
    @FXML
    private Text AcceuilBtn;
    @FXML
    private Text blogBtn;
    @FXML
    private Button inscri_btn;

    @FXML
    private Button login;



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





    private static final String GEMINI_API_URL = "https://api.gemini.com/v1/symbols";

    private static final Map<String, String> QUERY_MAPPING = new HashMap<>();

    static {
        QUERY_MAPPING.put("hello", "Greetings! How can I assist you?");
        QUERY_MAPPING.put("symptômes de la grippe", "Les symptômes de la grippe incluent la fièvre, la toux, les douleurs musculaires, la fatigue et les maux de tête.");
        QUERY_MAPPING.put("comment prévenir le rhume", "Pour prévenir le rhume, vous pouvez vous laver régulièrement les mains, éviter les contacts étroits avec des personnes malades, et maintenir un système immunitaire fort par une alimentation saine et de l'exercice.");
        QUERY_MAPPING.put("alimentation équilibrée", "Une alimentation équilibrée devrait inclure une variété de fruits, de légumes, de protéines maigres, de grains entiers et de produits laitiers faibles en gras.");
        QUERY_MAPPING.put("exercices pour le mal de dos", "Certains exercices qui peuvent aider à soulager le mal de dos comprennent les étirements, les exercices de renforcement du dos, et le yoga ou le tai-chi.");
        QUERY_MAPPING.put("effets secondaires de l'ibuprofène", "Les effets secondaires courants de l'ibuprofène comprennent les maux d'estomac, les nausées, les maux de tête et les étourdissements. Consultez votre médecin si vous ressentez des effets secondaires graves.");
        QUERY_MAPPING.put("posologie du paracétamol", "La posologie standard du paracétamol pour les adultes est généralement de 500 mg à 1000 mg toutes les 4 à 6 heures, selon les besoins. Ne dépassez pas la dose recommandée.");
        QUERY_MAPPING.put("contre-indications de l'aspirine", "Les contre-indications de l'aspirine comprennent les ulcères d'estomac, les allergies à l'aspirine, l'asthme et la grossesse, surtout pendant le troisième trimestre.");
        QUERY_MAPPING.put("interaction entre le Warfarine et le pamplemousse", "Le pamplemousse peut augmenter les effets de la warfarine, augmentant ainsi le risque de saignement. Évitez de consommer du pamplemousse ou de boire du jus de pamplemousse lorsque vous prenez de la warfarine.");

    }

    @FXML
    void start(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Assuming the button is within a scene
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);

        TextField inputField = new TextField();
        inputField.setPromptText("Type your question here...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String question = inputField.getText().toLowerCase();
            if (!question.isEmpty()) {
                String response = getResponse(question);
                chatArea.appendText("You: " + question + "\n");
                chatArea.appendText("Gemini API: " + response + "\n\n");
                inputField.clear();
            }
        });

        VBox root = new VBox(10, chatArea, inputField, sendButton);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private String getResponse(String question) {
        if (QUERY_MAPPING.containsKey(question)) {
            return QUERY_MAPPING.get(question);
        } else {
            try {
                return queryGeminiAPI(GEMINI_API_URL);
            } catch (IOException e) {
                return "Error retrieving data from Gemini API.";
            }
        }
    }

    private String queryGeminiAPI(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return response.toString();
        } else {
            throw new IOException("Failed to retrieve data from API. Response code: " + responseCode);
        }
    }
    @FXML
    void Acceuil(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/GUI/Acceuil.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) AcceuilBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void BlogMove(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/GUI/FrontOfficeBlog.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) blogBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void inscri_btn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/inscription.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) inscri_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void login(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) login.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}



