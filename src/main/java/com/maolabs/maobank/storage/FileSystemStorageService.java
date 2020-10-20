package com.maolabs.maobank.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    StorageProperties properties;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public void store(MultipartFile file, String subfolder, boolean comData) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            File novoDiretorio = new File(properties.getLocation(subfolder));
            novoDiretorio.mkdirs();
            Path rootLocation = Paths.get(properties.getLocation(subfolder));

            String nomeDoArquivo = null;

            if (comData) {
                nomeDoArquivo = this.dataAtualString() + file.getOriginalFilename();
            } else {
                nomeDoArquivo = file.getOriginalFilename();
            }

            Files.copy(file.getInputStream(), rootLocation.resolve(nomeDoArquivo), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Path rootLocation = Paths.get(properties.getLocation(""));
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        Path rootLocation = Paths.get(properties.getLocation(""));
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, String subfolder) {
        try {
            Path file = load(properties.getLocation(subfolder, filename));
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        Path rootLocation = Paths.get(properties.getLocation(""));
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Path rootLocation = Paths.get(properties.getLocation(""));
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    private String dataAtualString() {
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");
        Date date = new Date();
        return dt.format(date);
    }
}
