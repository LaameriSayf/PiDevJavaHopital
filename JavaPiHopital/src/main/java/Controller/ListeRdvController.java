package Controller;

import Model.RendezVous;
import Service.RendezVousService;
import Test.HelloApplication;
import Util.MyDataBase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ListeRdvController implements Initializable {
    @FXML
    private Button generateQRCode;

    @FXML
    private VBox qrcodeVbox;

    @FXML
    private ImageView qrimageLabel;

    @FXML
    private TableColumn<RendezVous, LocalDate> dateLabel;

    @FXML
    private TableColumn<RendezVous, String> descLabel;

    @FXML
    private TableColumn<RendezVous, String> fileLabel;

    @FXML
    private TableColumn<RendezVous, String> heureLabel;

    @FXML
    private TextField keywordTextField;

    @FXML
    private TableColumn<RendezVous, Integer> rdvID;

    @FXML
    private TableView<RendezVous> rdvTableView;
    @FXML
    private Text countnbtotal;
    private Connection cnx;

@FXML
private Text nbrexpire;
    private ObservableList<RendezVous> rendezVousObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fetchRendezVousData();
        configureTableView();

        RendezVousService rds=new RendezVousService(cnx);
        int aa=  rds.nombreTotalRendezVous();
        int dd= rds.nombreRendezVousDepasseDateAujourdhui();
        countnbtotal.setText(""+aa);
        nbrexpire.setText(""+dd);
    }

    private void fetchRendezVousData() {
        final Connection connection = MyDataBase.getInstance().getConnection();
        String query = "SELECT id, daterendezvous, heurerendezvous, description, file FROM rendezvous ";
        try (Statement statement = connection.createStatement();
             ResultSet queryOutput = statement.executeQuery(query)) {
            while (queryOutput.next()) {
                Integer queryRdvID = queryOutput.getInt("id");
                String queryDesc = queryOutput.getString("description");
                LocalTime queryHeure = queryOutput.getTime("heurerendezvous").toLocalTime();
                String queryFile = queryOutput.getString("file");
                LocalDate queryDate = queryOutput.getDate("daterendezvous").toLocalDate();
                rendezVousObservableList.add(new RendezVous(queryRdvID, queryDesc, queryDate, queryHeure, queryFile));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configureTableView() {
        rdvID.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateLabel.setCellValueFactory(new PropertyValueFactory<>("daterdv"));
        heureLabel.setCellValueFactory(new PropertyValueFactory<>("heurerdv"));
        descLabel.setCellValueFactory(new PropertyValueFactory<>("description"));
        fileLabel.setCellValueFactory(new PropertyValueFactory<>("file"));

        FilteredList<RendezVous> filteredData = new FilteredList<>(rendezVousObservableList);
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(rendezVous -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String searchText = newValue.toLowerCase();
                return rendezVous.getDescription().toLowerCase().contains(searchText) ||
                        rendezVous.getFile().toLowerCase().contains(searchText);
            });
        });

        SortedList<RendezVous> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(rdvTableView.comparatorProperty());
        rdvTableView.setItems(sortedData);
    }


   /* @FXML
    void editRdv(ActionEvent event) {
        RendezVous selectedRdv = rdvTableView.getSelectionModel().getSelectedItem();
        if (selectedRdv != null) {
            // Create a custom dialog
            Dialog<RendezVous> dialog = new Dialog<>();
            dialog.setTitle("Edit Rendezvous");
            dialog.setHeaderText("Edit Rendezvous Details");

            // Set the button types (OK and Cancel)
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create text fields for each input
            TextField dateField = new TextField(selectedRdv.getDaterdv().toString());
            TextField descriptionField = new TextField(selectedRdv.getDescription());

            // Set the content of the dialog pane
            VBox content = new VBox();
            content.getChildren().addAll(
                    new Label("Date:"),
                    dateField,
                    new Label("Description:"),
                    descriptionField
            );
            dialog.getDialogPane().setContent(content);

            // Convert the result to a rendezvous object when the save button is clicked
            dialog.setResultConverter(buttonType -> {
                if (buttonType == saveButtonType) {
                    return new RendezVous(
                            selectedRdv.getId(),
                            descriptionField.getText(),
                            LocalDate.parse(dateField.getText()),
                            selectedRdv.getFile()
                    );
                }
                return null;
            });

            // Show the dialog and wait for the user response
            Optional<RendezVous> result = dialog.showAndWait();

            // If the user clicks save, update the rendezvous object and refresh the table view
            result.ifPresent(newRdv -> {
                try {
                    RendezVousService rendezVousService = new RendezVousService(MyDataBase.getInstance().getConnection());
                    rendezVousService.modifier(newRdv, newRdv.getId());
                    showAlert("Success", "Rendezvous updated successfully.");
                    refreshTableView();
                } catch (SQLException e) {
                    showAlert("Error", "Failed to update rendezvous: " + e.getMessage());
                }
            });
        } else {
            showAlert("Error", "No rendezvous selected.");
        }
    }
*/



    private void refreshTableView() {
        rendezVousObservableList.clear(); // Clear the existing data

        // Fetch the updated data from the database
        fetchRendezVousData();

        // Update the TableView with the refreshed data
        rdvTableView.setItems(rendezVousObservableList);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    @FXML
    private ImageView backHalim;
    @FXML
    void backHalim(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/GestionEmploi.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  backHalim.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private ImageView chatRoom;

    @FXML
    void chatRoom(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Chat.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  backHalim.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void handleGenerateQRCode() {


        String directoryPath = "C:\\Users\\Mega-PC\\Desktop\\PiDevJavaHopital-main\\JavaPiHopital\\src\\main\\uploads";

        RendezVous selectedRendezVous = rdvTableView.getSelectionModel().getSelectedItem();
        if (selectedRendezVous != null) {
            String qrData = selectedRendezVous.toString();

            String fileName = "QR_" + LocalDate.now() + ".png" ;
            String filePath = directoryPath + File.separator + fileName;

            Image qrImage = generateQRCode(qrData);
            if (qrImage != null) {
                qrimageLabel.setImage(qrImage);

                saveQRCodeToFile(qrData, filePath);
            }
        }
    }


    public Image generateQRCode(String data) {
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 250, 250, hintMap);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            return new Image(in);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveQRCodeToFile(String data, String filePath) {
        try {
            // Create a File object representing the file to be saved
            File file = new File(filePath);

            // Convert the JavaFX Image to a BufferedImage and write it to the file
            ImageIO.write(SwingFXUtils.fromFXImage(qrimageLabel.getImage(), null), "png", file);

            // Print the absolute path of the saved file
            System.out.println("QR Code saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            // Handle any IO exception that may occur during file writing
            e.printStackTrace();
        }

    }


}
