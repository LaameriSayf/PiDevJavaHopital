package Controllers;

import com.alibaba.fastjson.JSON;
import com.plexpt.chatgpt.ChatGPT;
public class ChatGPTClient {
    private final ChatGPT chatGPT;

    public ChatGPTClient() {
        // Initialisez le client ChatGPT ici avec votre clé API
        chatGPT = ChatGPT.builder()
                .apiKey("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")
                .build()
                .init();
    }

    public String sendMessage(String message) {
        String chatGPTResponse = chatGPT.chat(message);
        ChatGPTResponse responseModel = JSON.parseObject(chatGPTResponse, ChatGPTResponse.class);
        return responseModel.getMessage();
    }
}
