package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRController {

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea textArea;

    private final FileChooser fileChooser = new FileChooser();

    private final Tesseract tesseract = new Tesseract();

    public void initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        tesseract.setDatapath("path/to/tesseract/tessdata");
        tesseract.setLanguage("eng");
    }

    @FXML
    private void performOCR() {
        Stage stage = (Stage) imageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                String result = tesseract.doOCR(file);
                textArea.setText(result);
            } catch (TesseractException e) {
                e.printStackTrace();
            }
        }
    }
}