package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.services.serviceproduct.ImageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor()
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{path}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("path") String path) {
        MediaType contentType = imageService.getMediaType(path);
        InputStream in = imageService.getImageStream(path);
        InputStreamResource resource = new InputStreamResource(new InputStreamSource() {
            @Override
            @NonNull
            public InputStream getInputStream() {
                return in;
            }
        });
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(resource);
    }

    @PostMapping()
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String encodedPath = imageService.uploadImage(file);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(encodedPath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
