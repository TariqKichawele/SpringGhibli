package in.kichawele.ghibliapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.kichawele.ghibliapi.client.StabilityAIClient;
import in.kichawele.ghibliapi.dto.TextToImageRequest;

@Service
public class GhibliArtService {

    private final StabilityAIClient stabilityAIClient;
    private final String apiKey;

    public GhibliArtService(StabilityAIClient stabilityAIClient, @Value("${stability.api.key}") String apiKey) {
        this.stabilityAIClient = stabilityAIClient;
        this.apiKey = apiKey;
    }

    public byte[] createGhibliArt(MultipartFile image, String prompt) {
        String finalPrompt = prompt + ", in the beautiful style of Studio Ghibli";
        String engineId = "stable-diffusion-v1-6";
        String stylePreset = "anime";

        return stabilityAIClient.generateImageFromImage(
            "Bearer " + apiKey, 
            engineId, 
            image, 
            finalPrompt, 
            stylePreset
        );
    }

    public byte[] createGhibliArtFromText(String prompt, String style) {
        String finalPrompt = prompt + ", in the beautiful style of Studio Ghibli";
        String engineId = "stable-diffusion-v1-6";
        String stylePreset = style.equals("general") ? "anime" : style.replace("_", "-");

        TextToImageRequest request = new TextToImageRequest(finalPrompt, stylePreset);

        return stabilityAIClient.generateImageFromText(
            "Bearer " + apiKey, 
            engineId,
            request
        );
    }
    
}
