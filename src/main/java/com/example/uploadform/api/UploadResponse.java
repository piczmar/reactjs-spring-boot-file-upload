package com.example.uploadform.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@ApiModel(description = "A response of file upload operation")
public class UploadResponse {
    @ApiModelProperty(value = "Additional information about upload result", required = true, dataType = "string")
    private final String message;
    @ApiModelProperty(value = "Title of uploaded file", required = true, dataType = "string")
    private final String title;
    @ApiModelProperty(value = "Details of uploaded file", required = true, dataType = "string")
    private final String details;
    @ApiModelProperty(value = "Uploaded file name", required = true, dataType = "string")
    private final String fileName;
}
