package com.example.uploadform.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UploadResponse {
    private String message;
    private String title;
    private String details;
    private String fileName;

    public UploadResponse(String message) {
        this.message = message;
    }
}
