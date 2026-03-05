package in.kichawele.ghibliapi.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.kichawele.ghibliapi.client.StabilityAIClient;
import in.kichawele.ghibliapi.dto.TextToImageRequest;

@Service
public class GhibliArtService {

    private static final int[][] SDXL_DIMENSIONS = {
        {1024, 1024}, {1152, 896}, {1216, 832}, {1344, 768}, {1536, 640},
        {640, 1536},  {768, 1344}, {832, 1216}, {896, 1152}
    };

    private final StabilityAIClient stabilityAIClient;
    private final String apiKey;

    public GhibliArtService(StabilityAIClient stabilityAIClient, @Value("${stability.api.key}") String apiKey) {
        this.stabilityAIClient = stabilityAIClient;
        this.apiKey = apiKey;
    }

    public byte[] createGhibliArt(MultipartFile image, String prompt) {
        String finalPrompt = prompt + ", in the beautiful style of Studio Ghibli";
        String engineId = "stable-diffusion-xl-1024-v1-0";
        String stylePreset = "anime";

        MultipartFile resizedImage = resizeToSdxl(image);

        return stabilityAIClient.generateImageFromImage(
            "Bearer " + apiKey, 
            engineId, 
            resizedImage, 
            finalPrompt, 
            stylePreset
        );
    }

    private MultipartFile resizeToSdxl(MultipartFile file) {
        try {
            BufferedImage original = ImageIO.read(file.getInputStream());
            double aspectRatio = (double) original.getWidth() / original.getHeight();

            int[] best = SDXL_DIMENSIONS[0];
            double bestDiff = Double.MAX_VALUE;
            for (int[] dim : SDXL_DIMENSIONS) {
                double diff = Math.abs(aspectRatio - (double) dim[0] / dim[1]);
                if (diff < bestDiff) {
                    bestDiff = diff;
                    best = dim;
                }
            }

            BufferedImage resized = new BufferedImage(best[0], best[1], BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resized.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(original, 0, 0, best[0], best[1], null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "png", baos);

            return new InMemoryMultipartFile(
                file.getName(),
                file.getOriginalFilename(),
                "image/png",
                baos.toByteArray()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to resize image", e);
        }
    }

    public byte[] createGhibliArtFromText(String prompt, String style) {
        String finalPrompt = prompt + ", in the beautiful style of Studio Ghibli";
        String engineId = "stable-diffusion-xl-1024-v1-0";
        String stylePreset = style.equals("general") ? "anime" : style.replace("_", "-");

        TextToImageRequest request = new TextToImageRequest(finalPrompt, stylePreset);

        return stabilityAIClient.generateImageFromText(
            "Bearer " + apiKey, 
            engineId,
            request
        );
    }

    private static class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override public String getName() { return name; }
        @Override public String getOriginalFilename() { return originalFilename; }
        @Override public String getContentType() { return contentType; }
        @Override public boolean isEmpty() { return content.length == 0; }
        @Override public long getSize() { return content.length; }
        @Override public byte[] getBytes() { return content; }
        @Override public InputStream getInputStream() { return new ByteArrayInputStream(content); }
        @Override public void transferTo(File dest) throws IOException {
            java.nio.file.Files.write(dest.toPath(), content);
        }
    }
}
