package com.example.uploadform.service;

import com.example.uploadform.binstore.StorageProvider;
import com.example.uploadform.exception.DataAlreadyExistsException;
import com.example.uploadform.exception.DataCorruptedException;
import com.example.uploadform.model.FileMetaData;
import com.example.uploadform.repository.FileMetaDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class FileService {

    private final StorageProvider storageProvider;
    private final FileMetaDataRepository fileMetaDataRepository;

    @Autowired
    public FileService(FileMetaDataRepository fileMetaDataRepository, StorageProvider storageProvider) {
        this.fileMetaDataRepository = fileMetaDataRepository;
        this.storageProvider = storageProvider;
    }

    @Transactional
    public FileMetaData storeData(MultipartFile file, String title, String details) {
        String fileName = file.getOriginalFilename();
        Optional<FileMetaData> metadataFromDb = fileMetaDataRepository.findById(fileName);
        if (metadataFromDb.isPresent()) {
            throw new DataAlreadyExistsException("File with name " + fileName + " has been already uploaded");
        }
        String location = getTargetFileLocation(file.getOriginalFilename());
        FileMetaData savedData = fileMetaDataRepository.save(extractMetadata(file, title, details, location));
        byte[] content = extractContent(file);
        storageProvider.store(file.getOriginalFilename(), content);
        return savedData;
    }


    public Page<FileMetaData> getAllMetaData(Pageable pageable) {
        return fileMetaDataRepository.findAll(pageable);
    }

    private FileMetaData extractMetadata(MultipartFile file, String title, String details, String location) {
        return FileMetaData.builder()
                .name(file.getOriginalFilename())
                .contentSize(file.getSize())
                .contentType(file.getContentType())
                .title(title)
                .details(details)
                .createdAt(System.currentTimeMillis())
                .location(location)
                .build();
    }

    private byte[] extractContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new DataCorruptedException(e);
        }
    }

    private String getTargetFileLocation(String originalFilename) {
        return storageProvider.getLocation() + File.separator + originalFilename;
    }
}
