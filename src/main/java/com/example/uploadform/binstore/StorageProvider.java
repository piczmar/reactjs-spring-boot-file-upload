package com.example.uploadform.binstore;

public interface StorageProvider {
    void store(String fileName, byte[] content);

    String getLocation();
}
