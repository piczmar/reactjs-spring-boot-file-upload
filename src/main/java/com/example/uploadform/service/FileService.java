package com.example.uploadform.service;

import com.example.uploadform.model.FileMetaData;
import com.example.uploadform.repository.FileMetaDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service
public class FileService {

    private final String filesFolderPath;
    private final FileMetaDataRepository fileMetaDataRepository;

    @Autowired
    public FileService(FileMetaDataRepository fileMetaDataRepository, @Value("${files.folder}") String filesFolderPath) throws IOException {
        this.fileMetaDataRepository = fileMetaDataRepository;
        Path filesFolder = Paths.get(filesFolderPath);
        if (!Files.exists(filesFolder)) {
            Files.createDirectories(filesFolder);
        }
        this.filesFolderPath = filesFolderPath;
    }


    public Optional<FileMetaData> storeFile(MultipartFile file, String title, String details) {
        FileMetaData fileMetaData = fileMetaDataRepository.save(new FileMetaData(file.getName(), file.getContentType(), file.getSize(), title, details, System.currentTimeMillis()));
        try {
            Files.write(Paths.get(filesFolderPath, file.getOriginalFilename()), file.getBytes());
        } catch (IOException exc) {
            log.error("Could not store file",exc);
            return Optional.empty();
        }
        return Optional.of(fileMetaData);
    }

    public Page<FileMetaData> getAllMetaData(Pageable pageable) {
        return fileMetaDataRepository.findAll(pageable);
    }
}
