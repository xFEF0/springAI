package com.xfef0.springAI;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final ZhiPuAiImageModel imageModel;

    public ImageService(ZhiPuAiImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public ImageResponse generateImage(String prompt) {
        return imageModel.call(
                new ImagePrompt(prompt)
        );
    }
}
