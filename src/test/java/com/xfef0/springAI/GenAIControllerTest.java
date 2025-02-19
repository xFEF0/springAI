package com.xfef0.springAI;

import com.xfef0.springAI.service.ChatService;
import com.xfef0.springAI.service.ImageService;
import com.xfef0.springAI.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenAIController.class)
class GenAIControllerTest {

    private static final String API_PREFIX = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatService chatService;
    @MockitoBean
    private ImageService imageService;
    @MockitoBean
    private RecipeService recipeService;
    @Captor
    private ArgumentCaptor<String> promptCaptor;

    @Test
    void shouldReplyWithAJoke() throws Exception {
        String joke = "Ho ho ho merry christmas";
        when(chatService.getResponse(anyString())).thenReturn(joke);

        String prompt = "tell me a joke";
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(API_PREFIX + "/ask-ai")
                        .param("prompt", prompt)
        );

        verify(chatService, times(1)).getResponse(promptCaptor.capture());
        assertThat(promptCaptor.getValue()).isEqualTo(prompt);

        resultActions.andExpect(status().isOk())
                .andExpect(content().string(joke));
    }

    @Test
    void shouldGenerateImage() throws Exception {
        String prompt = "A dog running on the beach";
        String b64Json = "cAAzAAA21cgQABHQEMwAAMtLVyBu4GEBnAAAzAgIXMwN0AIgMYgAEYsJAZuBtAZAADMAADFjIDdwOIDGAABmDAQmbgb";
        ImageResponse imageResponse = mock(ImageResponse.class);
        when(imageService.generateImage(anyString())).thenReturn(imageResponse);
        ImageGeneration imageGeneration = mock(ImageGeneration.class);
        when(imageResponse.getResults()).thenReturn(List.of(imageGeneration));
        Image image = mock(Image.class);
        when(imageGeneration.getOutput()).thenReturn(image);
        when(image.getB64Json()).thenReturn(b64Json);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(API_PREFIX + "/generate-image")
                .param("prompt", prompt));

        byte[] byteArray = Base64.getDecoder().decode(b64Json);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(byteArray));
    }

    @Test
    void shouldGenerateRecipe() throws Exception {
        Map<String, String> parameters = new HashMap<>();
        String ingredients = "potatoes, tomatoes, cheese";
        parameters.put("ingredients", ingredients);
        String cuisine = "italian";
        parameters.put("cuisine", cuisine);
        String recipe = "";
        when(recipeService.createRecipe(eq(ingredients), eq(cuisine), eq("none"))).thenReturn(recipe);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(API_PREFIX + "/create-recipe")
                        .params(MultiValueMap.fromSingleValue(parameters))
        );

        resultActions.andExpect(status().isOk())
                .andExpect(content().string(recipe));
    }
}