package com.xfef0.springAI.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.mistralai.MistralAiChatOptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private MistralAiChatModel chatModel;
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(chatModel);
    }

    @Test
    void shouldGetResponse() {
        String prompt = "Tell me a joke";
        String joke = "How old is a bee? Nobody nose";
        when(chatModel.call(anyString())).thenReturn(joke);

        assertThat(chatService.getResponse(prompt))
                .isEqualTo(joke);

        verify(chatModel, times(1)).call(eq(prompt));
    }

    @Test
    void shouldGetResponseWithOptions() {
        String prompt = "Tell me a joke";
        String joke = "There were three pigs, one made a house";
        ChatResponse chatResponseMock = mock(ChatResponse.class);
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponseMock);
        Generation generationMock = mock(Generation.class);
        when(chatResponseMock.getResult()).thenReturn(generationMock);
        AssistantMessage assistantMessageMock = mock(AssistantMessage.class);
        when(generationMock.getOutput()).thenReturn(assistantMessageMock);
        when(assistantMessageMock.getText()).thenReturn(joke);

        assertThat(chatService.getResponseWithOptions(prompt))
                .isEqualTo(joke);

        Prompt promptCall = new Prompt(prompt, MistralAiChatOptions.builder()
                .model("mistral-large-latest")
                .temperature(0.7)
                .maxTokens(500)
                .build());

        verify(chatModel, times(1)).call(eq(promptCall));
    }
}