package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.EmpService;
import utils.MyDataBase;
import entities.Emploi;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmploiController {
    @FXML
    private ListView<String> listeEmploi;
    @FXML
    private Button deleteEM;
    @FXML
    private Button updateEM;
    @FXML
    private Button ajouterEmploi;

    @FXML
    private TextArea descLabel;

    @FXML
    private DatePicker endLabel;

    @FXML
    private DatePicker startLabel;

    @FXML
    private TextField titreLabel;
    @FXML
    private Button rdvNav;
    @FXML
    private Button emploiNav;
    private EmpService empService;
    @FXML
    private BorderPane borderNav;


    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private void btnrdvNav(ActionEvent event) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/AjouterRdv.fxml"));
        Parent root = loader.load();

        // Get the current scene
        Scene scene = rdvNav.getScene();

        // Set the root of the scene to the new view
        scene.setRoot(root);
    }

    public int getListEmploi(String chaine){

        String selectedItem =chaine;
        System.out.println("Selected item: " + selectedItem);
        Pattern pattern = Pattern.compile("ID: (\\d+)");
        Matcher matcher = pattern.matcher(selectedItem);

        if (matcher.find()) {
            return  Integer.parseInt(matcher.group(1));

        } else {
            System.out.println("ID not found");
        }return 0;
    }



    @FXML
    private void initialize(){

        final Connection connection = MyDataBase.getInstance().getConnection();
        empService = new EmpService(connection);
        loadAndDisplayData();
        listeEmploi.setOnMouseClicked(event -> {
            String selectedItem = listeEmploi.getSelectionModel().getSelectedItem();
            Pattern pattern = Pattern.compile("ID: (\\d+)");
            Matcher matcher = pattern.matcher(selectedItem);
            if(matcher.find()){
                try {

                    Emploi emp=empService.selectEmploi(Integer.parseInt(matcher.group(1)));
                    if (emp!= null){
                        titreLabel.setText(emp.getTitre());
                        startLabel.setValue(emp.getStart());
                        endLabel.setValue(emp.getEnd());
                        descLabel.setText(emp.getDescription());

                    }else System.out.println("emploi not found");

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }


            }else {
                System.out.println("ID not found");
            }
        });

    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Inforamation");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void showSuccess(String msg){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Succees");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void ajouterEmploi(javafx.event.ActionEvent event) {
        LocalDate start = startLabel.getValue();
        LocalDate end = endLabel.getValue();
        String titre = titreLabel.getText();
        String description = descLabel.getText();
        LocalDate currentDate = LocalDate.now();

        if (titre.isEmpty() || description.isEmpty() || start == null || end == null || start.isAfter(end)) {
            if (titre.isEmpty()) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (start == null) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (end == null) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (description.isEmpty()) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (end.isBefore(start)) {
                showAlert("La date de fin ne peut pas être antérieure qu debut.");
            }
            return;
        }
        if (start.isBefore(currentDate)) {
            showAlert("La date de début ne peut pas être antérieure à aujourd'hui.");
            return;
        }


        if (empService != null) {
            try {
                Emploi emploi = new Emploi(titre, start, end, description);
                empService.ajouterEmploi(emploi);
                startLabel.setValue(null);
                endLabel.setValue(null);
                titreLabel.setText("");
                descLabel.setText("");
                showSuccess("Emploi ajouté avec succès!");
                loadAndDisplayData();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur lors de l'ajout de l'emploi. Veuillez réessayer.");
            }
        } else {
            System.err.println("Service emploi non initialisé.");
            showAlert("Service emploi non initialisé. Veuillez contacter l'administrateur.");
        }
    }
    private void loadAndDisplayData() {
        ObservableList<String> items = FXCollections.observableArrayList();
        System.out.println("loading");
        try {
            List<Emploi> emploiList = empService.recuperer();
            for (Emploi emploi : emploiList) {
                String item = String.format("ID: %d, Titre: %s, Start: %s,End: %s, Description: %s",
                      emploi.getId(),emploi.getTitre(), emploi.getStart(), emploi.getEnd(), emploi.getDescription());
                items.add(item);
            }
            listeEmploi.setItems(items);

            

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
   @FXML
   private void supprimerEmploi(){

        String selectedIem = listeEmploi.getSelectionModel().getSelectedItem();
       Pattern pattern = Pattern.compile("ID: (\\d+)");
       Matcher matcher = pattern.matcher(selectedIem);
       if (matcher.find()) {
           try {
               empService.supprimerEmploi(Integer.parseInt(matcher.group(1)));
               if (listeEmploi.getItems().isEmpty() == false){
                   listeEmploi.getItems().removeAll();
                   loadAndDisplayData();

               }else loadAndDisplayData();
           }catch (SQLException e){
               System.out.println(e.getMessage());
           }



       }else {
           System.out.println("ID not found");
       }


   }
   public void modifierEmploi(javafx.event.ActionEvent event) {
        LocalDate start = startLabel.getValue();
        LocalDate end = endLabel.getValue();
        String titre = titreLabel.getText();
        String description = descLabel.getText();
        LocalDate currentDate = LocalDate.now();

        if (titre.isEmpty() || description.isEmpty() || start == null || end == null || start.isAfter(end)) {
            if (titre.isEmpty()) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (start == null) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (end == null) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (description.isEmpty()) {
                showAlert("Veuillez remplir tous les champs.");
            } else if (end.isBefore(start)) {
                showAlert("La date de fin ne peut pas être antérieure qu debut.");
            }
            return;
        }
        if (start.isBefore(currentDate)) {
            showAlert("La date de début ne peut pas être antérieure à aujourd'hui.");
            return;
        }

        if (empService != null) {
            try {
                Emploi emploi = new Emploi(titre,start,end,description);
                empService.modifierEmploi(emploi,getListEmploi(listeEmploi.getSelectionModel().getSelectedItem()));
                showSuccess("emploi modifie");
                loadAndDisplayData();
            } catch (SQLException e)
            { e.printStackTrace();
            }if (!listeEmploi.getItems().isEmpty()){
                listeEmploi.getItems().removeAll();
                loadAndDisplayData();
            }else {loadAndDisplayData();
            }
        }else
        {
            System.err.println("RDVservice echec");

        }
    }

    public void switchToRdv(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AjouterRdv.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
