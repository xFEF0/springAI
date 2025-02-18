package com.xfef0.springAI;

import org.springframework.ai.image.ImageResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}")
public class GenAIController {

    private final ChatService chatService;
    private final ImageService imageService;

    public GenAIController(ChatService chatService, ImageService imageService) {
        this.chatService = chatService;
        this.imageService = imageService;
    }

    @GetMapping("/ask-ai")
    public String getResponse(@RequestParam String prompt) {
        return chatService.getResponse(prompt);
    }

    @GetMapping("/ask-ai-options")
    public String getResponseWithOptions(@RequestParam String prompt) {
        return chatService.getResponseWithOptions(prompt);
    }

    @GetMapping(value = "/generate-image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody ResponseEntity<?> generateImages(@RequestParam String prompt) throws IOException {
        ImageResponse imageResponse = imageService.generateImage(prompt);

        Optional<ByteArrayResource> image = imageResponse.getResults().stream()
                .map(result -> result.getOutput().getB64Json())
                .map(b64json -> Base64.getDecoder().decode(b64json))
                .map(ByteArrayResource::new)
                .findFirst();

        return ResponseEntity.ok(image.get());
    }
}
