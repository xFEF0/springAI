package com.xfef0.springAI;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.StyleEnum;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final StabilityAiImageModel imageModel;

    public ImageService(StabilityAiImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public ImageResponse generateImage(String prompt) {
        return imageModel.call(
          new ImagePrompt(prompt,
                  StabilityAiImageOptions.builder()
                          .height(1024)
                          .width(1024)
                          .N(1)
                          .stylePreset(StyleEnum.ANIME)
                          .build())
        );
    }
}
