package com.knoledge.backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path rootDirectory;

    public FileStorageService(@Value("${storage.upload-dir:uploads}") String uploadDir) throws IOException {
        this.rootDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.rootDirectory);
    }

    public String store(MultipartFile file, String subDirectory) throws IOException {
        Path dir = rootDirectory.resolve(subDirectory).normalize();
        Files.createDirectories(dir);
        String original = file.getOriginalFilename();
        String extension = "";
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + extension;
        Path destination = dir.resolve(filename);
        file.transferTo(destination);
        return subDirectory + "/" + filename;
    }

    public void delete(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        try {
            Path file = rootDirectory.resolve(relativePath).normalize();
            Files.deleteIfExists(file);
        } catch (IOException ignored) {
        }
    }
}
