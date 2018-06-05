package com.example.uploadform.repository;

import com.example.uploadform.model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileMetaDataRepository extends JpaRepository<FileMetaData, String> {
}