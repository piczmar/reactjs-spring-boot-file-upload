package com.example.uploadform.api.v1;

import com.example.uploadform.api.UploadResponse;
import com.example.uploadform.model.FileMetaData;
import com.example.uploadform.service.FileService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController()
@Api(value = "Upload", description = "Allows uploading and listing metadata of uploaded files")
public class UploadController {

    private final FileService fileService;

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/api/v1/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Make a POST request to upload the file",
            produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The POST call is successful"),
            @ApiResponse(code = 500, message = "The POST call is failed"),
            @ApiResponse(code = 400, message = "The POST call has wrong request data, e.g. uploading file with the same name as before")
    })
    public ResponseEntity<UploadResponse> uploadFile(
            @ApiParam(name = "file", value = "Select the file to Upload", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(name = "title", value = "Title of the file", required = true)
            @RequestParam("title") String title,
            @ApiParam(name = "details", value = "Details of the file", required = true)
            @RequestParam("details") String details) {

        Assert.isTrue(!file.isEmpty(), "File cannot be empty");

        UploadResponse response = UploadResponse.builder().message("Successfully uploaded")
                .title(title)
                .details(details)
                .fileName(file.getOriginalFilename())
                .build();

        fileService.storeData(file, title, details);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "List metadata of uploaded files",
            produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The GET is successful"),
    })
    public ResponseEntity<Page<FileMetaData>> uploadFile(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(fileService.getAllMetaData(pageable), HttpStatus.OK);

    }
}
