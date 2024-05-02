package Controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import entities.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utils.MyDataBase;

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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ListeRdvController implements Initializable {
    @FXML
    private Button generateQRCodeButton;

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

    private ObservableList<RendezVous> rendezVousObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchRendezVousData();
        configureTableView();
    }

    private void fetchRendezVousData() {
        final Connection connection = MyDataBase.getInstance().getConnection();
        String query = "SELECT id, daterendezvous, heurerendezvous, description, file FROM rendezvous ";
        try (Statement statement = connection.createStatement();
             ResultSet queryOutput = statement.executeQuery(query)) {
            while (queryOutput.next()) {
                Integer queryRdvID = queryOutput.getInt("id");
                String queryDesc = queryOutput.getString("description");
                String queryHeure = queryOutput.getString("heurerendezvous");
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
        keywordTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            filteredData.setPredicate(rendezVous -> {
                if (newvalue == null || newvalue.trim().isEmpty()) {
                    return true;
                }
                String searchText = newvalue.toLowerCase();
                return rendezVous.getHeurerdv().toLowerCase().contains(searchText) ||
                        rendezVous.getDescription().toLowerCase().contains(searchText) ||
                        rendezVous.getFile().toLowerCase().contains(searchText) ||
                        rendezVous.getDaterdv().toString().contains(searchText);
            });
        });

        SortedList<RendezVous> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(rdvTableView.comparatorProperty());
        rdvTableView.setItems(sortedData);
    }

    @FXML
    private void handleGenerateQRCode() {


        String directoryPath = "C:\\Users\\Mega-PC\\Desktop\\PIDEV1\\src\\main\\QRCodes";

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
