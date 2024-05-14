package Controller;

import Model.Emploi;
import Service.EmpService;
import Test.HelloApplication;
import Util.MyDataBase;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
        String toPhoneNumber = "+21623340490";
        String messageBody = "L'emploi de travail de cette semaine a été publié. Consultez Medi-Connect";

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
                //SEND SMS TFAKARHA FEL VALIDATION
                sendSMS(toPhoneNumber,messageBody);
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
                   showSuccess("l'Emploi a été supprimé !");

               }else loadAndDisplayData();
           }catch (SQLException e){
               System.out.println(e.getMessage());
           }



       }else {
           System.out.println("ID not found");
       }


   }

    public static void sendSMS(String toPhoneNumber, String messageBody) {
        // Initialize Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


        try {
            // Sending the SMS
            Message message = Message.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(FROM_PHONE_NUMBER),
                            messageBody)
                    .create();

            System.out.println("SMS sent successfully. SID: " + message.getSid());
        } catch (ApiException e) {
            System.out.println("Failed to send SMS. Error: " + e.getMessage());
        }
    }
    public void switchToRdv(javafx.event.ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterRdv.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("medi_connect!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // or handle the exception according to your application's logic
        } catch (Exception e) {
            // Handle other exceptions, including InvocationTargetException
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace(); // print the cause's stack trace
            } else {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private Button logout_btn;
    @FXML
    void logout(javafx.event.ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}