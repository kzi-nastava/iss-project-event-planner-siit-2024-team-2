package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.utils.StatusPair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final String UPLOADS_DIR = "uploads";
    public MediaType getMediaType(String path) {
        String decodedPath = new String(Base64.getDecoder().decode(path));
        String extension = path.substring(decodedPath.lastIndexOf(".") + 1);
        return switch (extension) {
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.IMAGE_JPEG;
        };
    }
    public StatusPair<InputStream> getImageStream(String path) {
        String decodedPath = new String(Base64.getDecoder().decode(path));
        // TODO: Add authorization
        Path imagePath = sanitizePath(decodedPath);
        if (imagePath == null)
            return new StatusPair<>(null, HttpStatus.BAD_REQUEST);
        try {
            return new StatusPair<>(new FileInputStream(imagePath.toFile()), HttpStatus.OK);
        }
        catch (FileNotFoundException e) {
            return new StatusPair<>(null, HttpStatus.NOT_FOUND);
        }
    }
    private Path sanitizePath(String decodedPath)  {
        Path normalizedPath = Paths.get(decodedPath).normalize();

        Path uploadsDir = Paths.get(System.getProperty("user.dir")).resolve(UPLOADS_DIR);
        Path absolutePath = uploadsDir.resolve(normalizedPath).normalize();

        if (!absolutePath.startsWith(uploadsDir))
            return null;
        return absolutePath;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(System.getProperty("user.dir"), UPLOADS_DIR);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename() ;

        Path targetPath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return encodePath(filename);
    }

    public static String encodePath(String imagePath) {
        if (imagePath == null || imagePath.isEmpty())
            return null;
        return new String(Base64.getEncoder().encode(imagePath.getBytes()));
    }

    public static String decodePath(String decodedPath) {
        if (decodedPath == null || decodedPath.isEmpty())
            return null;
        return new String(Base64.getDecoder().decode(decodedPath));
    }
}
