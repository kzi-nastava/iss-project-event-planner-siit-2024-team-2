package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.services.serviceproduct.ImageService;
import com.example.eventplanner.utils.StatusPair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor()
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{path}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("path") String path) {
        MediaType contentType = imageService.getMediaType(path);
        StatusPair sp = imageService.getImageStream(path);
        if (sp.getStatus() != HttpStatus.OK)
            return new ResponseEntity<>(sp.getStatus());
        if (sp.getValue() == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        InputStream in = (InputStream) sp.getValue();
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
}
