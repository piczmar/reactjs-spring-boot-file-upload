package com.example.uploadform.api;

import com.example.uploadform.model.FileMetaData;
import com.example.uploadform.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestController
public class UploadControllerV1 {


    private final FileService fileService;

    public UploadControllerV1(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/api/v1/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("title") String title,
                                                     @RequestParam("details") String details,
                                                     final HttpServletRequest request) {

        log.debug("Uploaded file");
        log.debug("fileName : " + file.getOriginalFilename());
        log.debug("contentType : " + file.getContentType());
        log.debug("contentSize : " + file.getSize());
        log.debug("title : " + title);
        log.debug("details : " + details);

        Assert.isTrue(!file.isEmpty(), "File cannot be empty");


        Optional<FileMetaData> fileMetaDataOptional = fileService.storeFile(file, title, details);

        return new ResponseEntity<>(new UploadResponse("Successfully uploaded", title, details, file.getOriginalFilename()), HttpStatus.OK);

    }

    @RequestMapping(value = "/api/v1/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Page<FileMetaData>> uploadFile(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(fileService.getAllMetaData(pageable), HttpStatus.OK);

    }
}
