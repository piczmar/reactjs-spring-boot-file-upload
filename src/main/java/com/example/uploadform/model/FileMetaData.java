package com.example.uploadform.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileMetaData {
    @Id
    private String name;

    private String contentType;

    private long contentSize;

    private String title;

    private String details;

    private long createdAt;

    private String location;

}