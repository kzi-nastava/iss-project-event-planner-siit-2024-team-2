package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.exception.BadRequestException;
import com.example.eventplanner.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${uploads.dir}")
    private String UPLOADS_DIR;
    public MediaType getMediaType(String path) {
        String decodedPath = new String(Base64.getDecoder().decode(path));
        String extension = path.substring(decodedPath.lastIndexOf(".") + 1);
        return switch (extension) {
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.IMAGE_JPEG;
        };
    }
    public InputStream getImageStream(String path) {
        String decodedPath = new String(Base64.getDecoder().decode(path));
        // TODO: Add authorization
        Path imagePath = sanitizePath(decodedPath);
        if (imagePath == null)
            throw new BadRequestException("Invalid image path");
        try {
            return new FileInputStream(imagePath.toFile());
        }
        catch (FileNotFoundException e) {
            throw new NotFoundException("Image not found");
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
