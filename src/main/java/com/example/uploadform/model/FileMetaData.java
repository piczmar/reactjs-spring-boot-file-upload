package com.example.uploadform.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FileMetaData {
    @Id
    private String name;

    private String contentType;

    private long contentSize;

    private String title;

    private String details;

    private long createdAt;

}