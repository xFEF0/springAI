package com.xfef0.springAI.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.StyleEnum;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    private ImageService imageService;
    @Mock
    private StabilityAiImageModel imageModel;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(imageModel);
    }

    @Test
    void shouldGenerateImage() {
            String prompt = "A dog running on the beach";
            String b64json = "12354678964321881135";
            ImageResponse imageResponse = mock(ImageResponse.class);
            when(imageModel.call(any(ImagePrompt.class))).thenReturn(imageResponse);

            assertThat(imageService.generateImage(prompt))
                    .isEqualTo(imageResponse);

            ImagePrompt promptCall = new ImagePrompt(prompt, StabilityAiImageOptions.builder()
                    .stylePreset(StyleEnum.ANIME)
                    .height(1024)
                    .width(1024)
                    .N(1)
                    .build());

            verify(imageModel, times(1)).call(eq(promptCall));
    }
}