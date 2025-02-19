package com.xfef0.springAI;

import org.springframework.ai.image.ImageResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}")
public class GenAIController {

    private final ChatService chatService;
    private final ImageService imageService;
    private final RecipeService recipeService;

    public GenAIController(ChatService chatService, ImageService imageService, RecipeService recipeService) {
        this.chatService = chatService;
        this.imageService = imageService;
        this.recipeService = recipeService;
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

    @GetMapping("/create-recipe")
    public String getRecipe(@RequestParam String ingredients,
                            @RequestParam(defaultValue = "any") String cuisine,
                            @RequestParam(defaultValue = "none") String dietaryRestrictions) {
        return recipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);
    }
}
