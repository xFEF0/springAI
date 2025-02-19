package com.xfef0.springAI.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    private RecipeService recipeService;
    @Mock
    private ChatModel chatModel;
    @Captor
    private ArgumentCaptor<Prompt> promptArgumentCaptor;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeService(chatModel);
    }

    @Test
    void shouldGenerateRecipe() {
        String ingredients = "potatoes, tomatoes, cheese";
        String cuisine = "any";
        String dietaryRestrictions = "salt";
        ChatResponse chatResponse = mock(ChatResponse.class);
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);
        Generation generation = mock(Generation.class);
        when(chatResponse.getResult()).thenReturn(generation);
        AssistantMessage assistantMessage = mock(AssistantMessage.class);
        when(generation.getOutput()).thenReturn(assistantMessage);
        String recipe = "This is a recipe...";
        when(assistantMessage.getText()).thenReturn(recipe);

        assertThat(recipeService.createRecipe(ingredients, cuisine, dietaryRestrictions))
                .isEqualTo(recipe);

        verify(chatModel, times(1)).call(promptArgumentCaptor.capture());
        Prompt promptCaptured = promptArgumentCaptor.getValue();
        assertThat(promptCaptured.getContents()).contains(ingredients, cuisine, dietaryRestrictions);
    }
}