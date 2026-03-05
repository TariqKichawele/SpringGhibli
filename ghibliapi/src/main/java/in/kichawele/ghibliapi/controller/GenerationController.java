package in.kichawele.ghibliapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import in.kichawele.ghibliapi.dto.TextGenerationRequestDTO;
import in.kichawele.ghibliapi.service.GhibliArtService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RequiredArgsConstructor
public class GenerationController {

    private static final Logger logger = LoggerFactory.getLogger(GenerationController.class);
    private final GhibliArtService ghibliArtService;

    @PostMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateGhibliArt(
        @RequestParam("prompt") String prompt,
        @RequestParam("image") MultipartFile image
    ) {
        try {
            byte[] imageBytes = ghibliArtService.createGhibliArt(image, prompt);
            return ResponseEntity.ok(imageBytes);
        } catch (Exception e) {
            logger.error("Error generating Ghibli art from image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(
        value = "/generate-from-text",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> generateGhibliArtFromText(
        @RequestBody TextGenerationRequestDTO request
    ) {
        try {
            byte[] imageBytes = ghibliArtService.createGhibliArtFromText(request.getPrompt(), request.getStyle());
            return ResponseEntity.ok(imageBytes);
        } catch (Exception e) {
            logger.error("Error generating Ghibli art from text: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json")
                .body(("{\"error\": \"" + e.getMessage() + "\"}").getBytes());
        }
    }
}
