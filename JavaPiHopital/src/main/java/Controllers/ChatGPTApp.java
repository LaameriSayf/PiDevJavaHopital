package Controllers;

import com.plexpt.chatgpt.ChatGPT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class ChatGPTApp {
    @FXML
    private TextArea chatArea;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;  // Correction : Renommé sendButton

    private ChatGPT chatGPT;

    public ChatGPTApp() {
        // Initialisez le client ChatGPT ici avec votre clé API
        chatGPT = ChatGPT.builder()
                .apiKey("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")
                .build()
                .init();
    }

    @FXML
    void sendMessage() {
        // Ajoutez le code pour envoyer le message à l'API ChatGPT et obtenir la réponse
        String userMessage = userInput.getText();
        String chatGPTResponse;

        try {
            chatGPTResponse = chatGPT.chat(userMessage);

            // Affichez ensuite la réponse dans la TextArea
            chatArea.appendText("User: " + userMessage + "\n");
            chatArea.appendText("ChatGPT: " + chatGPTResponse + "\n");
        } catch (Exception e) {
            // Gérez l'erreur de quota insuffisant ici, par exemple en affichant un message à l'utilisateur.
            chatArea.appendText("Error: Insufficient quota. Please check your plan and billing details.\n");
        }

        userInput.clear();
    }

}
