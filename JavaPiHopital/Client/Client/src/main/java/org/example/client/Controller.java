package org.example.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button button_send;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private TextField tf_message;

    @FXML
    private VBox vbox_messages;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
      client = new Client(new Socket("localhost" , 1234));
            System.out.println("Connected to server");
            client.recieveMessageFromServer(vbox_messages);

        }catch (IOException e){
            e.printStackTrace();
            showAlert("Error", "Failed to connect to the server: " + e.getMessage());

        }
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue );
            }
        });
        client.recieveMessageFromServer(vbox_messages);

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                if (!messageToSend.isEmpty()){
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5,5,5,10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239,242,255);"
                            + "-fx-background-color: rgb(15,125,242);"
                            + " -fx-background-radius: 20px");
                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));

                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);
                    client.sendMessageToServer(messageToSend);
                    tf_message.clear();

                }
            }
        });



    }

    public static void addLabel(String msgFromServer,VBox vBox){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(15,125,242)"
                + " ;-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5,10,5,10));
        hbox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hbox);
            }
        });




    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}