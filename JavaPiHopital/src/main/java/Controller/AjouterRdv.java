package Controller;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import entities.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.RdvService;
import services.RendezVousService;
import utils.MyDataBase;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AjouterRdv {

    @FXML
    private Button pdfLabel;

    @FXML
    private DatePicker dateLabel;

    @FXML
    private TextField descriptionLabel;

    @FXML
    private TextField heureLabel;

    @FXML
    private Button importFile;


    @FXML
    private ListView<String> fileList;

    @FXML
    private ListView<String> listeRdv;
    private RdvService rdvService;

    public int get_List_RDV(String chaine){
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
    private void initialize() {


        final Connection connection = MyDataBase.getInstance().getConnection();

        rdvService = new RendezVousService(connection);
        loadAndDisplayData();
        listeRdv.setOnMouseClicked(event -> {
            String selectedItem = listeRdv.getSelectionModel().getSelectedItem();
            System.out.println("Selected item: " + selectedItem);
            Pattern pattern = Pattern.compile("ID: (\\d+)");
            Matcher matcher = pattern.matcher(selectedItem);
            if (matcher.find()) {
                try {

                    RendezVous rdv=rdvService.selectRDV(Integer.parseInt(matcher.group(1)));
                    System.out.println(rdv);
                    if (rdv!=null) {
                        dateLabel.setValue(rdv.getDaterdv());
                        heureLabel.setText(rdv.getHeurerdv());
                        descriptionLabel.setText(rdv.getDescription());
                        if(fileList.getItems().isEmpty()==false)
                            fileList.getItems().clear();
                        fileList.getItems().add(rdv.getFile());
                    }else
                        System.out.println("error rdv not found");

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("ID not found");
            }
        });

    }

    @FXML
    void supprimerRDV(){
        String selectedItem = listeRdv.getSelectionModel().getSelectedItem();
        System.out.println("Selected item: " + selectedItem);
        Pattern pattern = Pattern.compile("ID: (\\d+)");
        Matcher matcher = pattern.matcher(selectedItem);

        if (matcher.find()) {
            try {
                rdvService.supprimer(Integer.parseInt(matcher.group(1)));
                if (listeRdv.getItems().isEmpty()==false) {
                    listeRdv.getItems().removeAll();
                    loadAndDisplayData();
                }else
                    loadAndDisplayData();

            }catch (SQLException e){
                System.out.println(e.getMessage());

            }
        } else {
            System.out.println("ID not found");
        }
    }

    @FXML
    void ajouterRDV(ActionEvent event) {
        LocalDate date = dateLabel.getValue();
        String heure = heureLabel.getText();
        String description = descriptionLabel.getText();
        LocalDate currentDate = LocalDate.now();
        String email = ("halimtrabelsi73@gmail.com");




        if (date == null || heure.isEmpty() || description.isEmpty() || fileList.getItems().isEmpty()) {
            if (date == null) {
                showAlert("Ajouter la date");
            } else if (heure.isEmpty()) {
                showAlert("Ajouter Heure");
            } else if (description.isEmpty()) {
                showAlert("Ajouter Description");
            } else if (fileList.getItems().isEmpty()) {
                showAlert("Ajouter fichier(s) de liaison");
            }
            return;
        }
        if (date.isBefore(currentDate)) {
            showAlert("La date de début ne peut pas être antérieure à aujourd'hui.");
            return;
        }


        List<String> files = fileList.getItems();

        if (rdvService != null) {
            try {

                for (String fileName : files) {
                    RendezVous rendezVous = new RendezVous(description, fileName, date, heure);
                    rdvService.ajouterRDV(rendezVous);
                    dateLabel.setValue(null);
                    heureLabel.setText("");
                    descriptionLabel.setText("");
                    fileList.setItems(null);


                }
                String content = "<p style=\\\"font-family: Arial, sans-serif; font-size: 14px;\\\">Cher patient,</p>\"\n" +
                        "                    + \"<p style=\\\"font-family: Arial, sans-serif; font-size: 14px;\\\">Votre rendez-vous a été envoyé avec succès.</p>\"\n" +
                        "                    + \"<p style=\\\"font-family: Arial, sans-serif; font-size: 14px;\\\">À bientôt,<br/>Hopital Militaire de Tunis<br/>@MediConnect</p>";

                sendEmail(email,content);
                showAlert("RendezVous ajouté avec succès!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (listeRdv.getItems().isEmpty()==false) {
                listeRdv.getItems().removeAll();
                loadAndDisplayData();
            }else
                loadAndDisplayData();
        } else {
            System.err.println("RdvService échec!");
        }

    }

    @FXML
    void importFile (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(importFile.getScene().getWindow());

        if (selectedFiles != null) {
            if(fileList.getItems().isEmpty()!=true)
                fileList.getItems().clear();
            for (File file : selectedFiles) {
                String destinationDirectory = "C:\\Users\\Mega-PC\\Desktop\\PIDEV1\\src\\main\\uploads";
                File destination = new File(destinationDirectory,file.getName());
                try{
                    Files.copy(file.toPath(),destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    fileList.getItems().add(file.getName());
                }
                catch(IOException e){ e.printStackTrace();}
            }
        }
    }
    private void loadAndDisplayData() {
        ObservableList<String> items = FXCollections.observableArrayList();
        System.out.println("loading");
        try {
            List<RendezVous> rendezVousList = rdvService.recuperer();
            for (RendezVous rendezVous : rendezVousList) {
                String item = String.format("ID: %d, Description: %s, File: %s, Date: %s, Heure: %s",
                        rendezVous.getId(), rendezVous.getDescription(), rendezVous.getFile(),
                        rendezVous.getDaterdv(), rendezVous.getHeurerdv());
                items.add(item);
            }

            listeRdv.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void modifier(ActionEvent event){
        LocalDate date = dateLabel.getValue();
        String heure = heureLabel.getText();
        String description = descriptionLabel.getText();

        if (date == null || heure.isEmpty() || description.isEmpty() || fileList.getItems().isEmpty()) {
            if (date == null) {
                showAlert("Ajouter la date");
            } else if (heure.isEmpty()) {
                showAlert("Ajouter Heure");
            } else if (description.isEmpty()) {
                showAlert("Ajouter Description");
            } else if (fileList.getItems().isEmpty()) {
                showAlert("Ajouter fichier(s) de liaison");
            }
            return;
        }

        List<String> files = fileList.getItems();

        if (rdvService != null) {
            try {
                for (String fileName : files) {
                    RendezVous rendezVous = new RendezVous(description, fileName, date, heure);
                    rdvService.modifier(rendezVous,get_List_RDV(listeRdv.getSelectionModel().getSelectedItem()));
                }
                showAlert("RendezVous modifie avec succès!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (listeRdv.getItems().isEmpty()==false) {
                listeRdv.getItems().removeAll();
                loadAndDisplayData();
            }else
                loadAndDisplayData();
        } else {
            System.err.println("RdvService échec!");
        }

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Felicitation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inforamation");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    @FXML
    public void switchToEmploi(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GestionEmploi.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchToListeRDV(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ListeRDV.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static void sendEmail(String recipientEmail, String contenue) {
        final String senderEmail = "batoutbata5@gmail.com";
        final String senderName = "Team MediConnect"; // Your desired sender name
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            System.out.println("Recipient email is null or empty. Cannot send email.");
            return;
        }
        // Password
        final String password = "ialgvzhizvvrwozy";

        // SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail, senderName));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            message.setSubject("Thank you for your feedback");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = "<p style=\"font-family: Arial, sans-serif; font-size: 14px;\">Cher patient,</p>"
                    + "<p style=\"font-family: Arial, sans-serif; font-size: 14px;\">Votre rendez-vous a été envoyé avec succès.</p>"
                    + "<p style=\"font-family: Arial, sans-serif; font-size: 14px;\">À bientôt,<br/>Hopital Militaire de Tunis<br/>@MediConnect</p>"
                    ;
            htmlPart.setContent(htmlContent, "text/html");
            multipart.addBodyPart(htmlPart);

            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource source = new FileDataSource("C:\\Users\\Mega-PC\\Desktop\\PIDEV1\\src\\main\\resources\\0dca79a4-6e06-4b14-aefe-ac46705d1113-removebg-preview.png"); // Replace with the path to your image file
            imagePart.setDataHandler(new DataHandler(source));
            imagePart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    } @FXML
    void generateAndSavePDF() {
        String selectedData = listeRdv.getSelectionModel().getSelectedItem();
        LocalDate currentDate = LocalDate.now();
        String randomUUID = UUID.randomUUID().toString();
        String filePath = "PDFfile_" + randomUUID + ".pdf";

        try {
            generatePDF(selectedData, filePath);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            showAlert("Error occurred while generating PDF.");
        }
    }

    private void generatePDF(String text, String filePath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add title to the document in the center with blue color
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD, BaseColor.BLUE);
        Paragraph title = new Paragraph("Votre rendez-vous", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        // Add text content to the document
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
        Paragraph paragraph = new Paragraph(text, textFont);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.close();

        showSuccess("PDF generated successfully!");
    }







}
