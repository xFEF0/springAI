package com.xfef0.springAI;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final MistralAiChatModel chatModel;


    public ChatService(MistralAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getResponse(String prompt) {
        return chatModel.call(prompt);
    }

    public String getResponseWithOptions(String prompt) {
        ChatResponse response = chatModel.call(
                new Prompt(
                        prompt,
                        MistralAiChatOptions.builder()
                                .model("mistral-large-latest")
                                .temperature(0.7)
                                .maxTokens(500)
                                .build()
                )
        );

        return response.getResult().getOutput().getText();
    }
}
